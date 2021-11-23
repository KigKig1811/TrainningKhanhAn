package com.vnpay.anlmk.utils

import android.content.Context
import android.net.Uri
import android.os.StatFs
import com.squareup.picasso.Downloader
import com.squareup.picasso.Downloader.ResponseException
import com.squareup.picasso.NetworkPolicy
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class OkHttp3Downloader : Downloader {

    override fun load(uri: Uri?, networkPolicy: Int): Downloader.Response {
        var cacheControl: CacheControl? = null
        if (networkPolicy != 0) {
            if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                cacheControl = CacheControl.FORCE_CACHE
            } else {
                val builder = CacheControl.Builder()
                if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                    builder.noCache()
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                    builder.noStore()
                }
                cacheControl = builder.build()
            }
        }

        val builder = Request.Builder()
            .addHeader("Connection","keep-alive")
            .url(uri.toString())
        if (cacheControl != null) {
            builder.cacheControl(cacheControl)
        }

        val response = client.newCall(builder.build()).execute()
        val responseCode = response.code()
        if (responseCode >= 300) {
            response.body()!!.close()
            throw ResponseException(
                "${responseCode} ${response.message()}", networkPolicy,
                responseCode
            )
        }

        val fromCache = response.cacheResponse() != null

        val responseBody = response.body()
        return Downloader.Response(responseBody!!.byteStream(), fromCache, responseBody!!.contentLength())
    }

    private val client: Call.Factory
    private val cache: Cache?

    /**
     * Create new downloader that uses OkHttp. This will install an image cache into your application
     * cache directory.
     */
    constructor(context: Context) : this(defaultCacheDir(context)) {}

    /**
     * Create new downloader that uses OkHttp. This will install an image cache into your application
     * cache directory.
     *
     * @param maxSize The size limit for the cache.
     */
    constructor(context: Context, maxSize: Long) : this(defaultCacheDir(context), maxSize) {}

    /**
     * Create new downloader that uses OkHttp. This will install an image cache into the specified
     * directory.
     *
     * @param cacheDir The directory in which the cache should be stored
     * @param maxSize  The size limit for the cache.
     */
    @JvmOverloads
    constructor(cacheDir: File, maxSize: Long = calculateDiskCacheSize(cacheDir)) : this(
        createOkHttpClient(
            cacheDir,
            maxSize
        )
    ) {
    }

    constructor(client: OkHttpClient) {
        this.client = client
        this.cache = client.cache()
    }

    constructor(client: Call.Factory) {
        this.client = client
        this.cache = null
    }


    override fun shutdown() {
        if (cache != null) {
            try {
                cache!!.close()
            } catch (ignored: IOException) {
            }

        }
    }

    companion object {
        private val PICASSO_CACHE = "picasso-cache"
        private val MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024 // 5MB
        private val MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024 // 50MB

        private fun defaultCacheDir(context: Context): File {
            val cache = File(context.getApplicationContext().getCacheDir(), PICASSO_CACHE)
            if (!cache.exists()) {

                cache.mkdirs()
            }
            return cache
        }

        private fun calculateDiskCacheSize(dir: File): Long {
            var size = MIN_DISK_CACHE_SIZE.toLong()

            try {
                val statFs = StatFs(dir.getAbsolutePath())
                val available = statFs.blockCount.toLong() * statFs.blockSize
                // Target 2% of the total space.
                size = available / 50
            } catch (ignored: IllegalArgumentException) {
            }

            // Bound inside min/max size for disk cache.
            return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE.toLong()), MIN_DISK_CACHE_SIZE.toLong())
        }

        /**
         * Creates a [Cache] that would have otherwise been created by calling
         * [.OkHttp3Downloader]. This allows you to onStart your own [OkHttpClient]
         * while still getting the default disk cache.
         */
        fun createDefaultCache(context: Context): Cache {
            val dir = defaultCacheDir(context)
            return Cache(dir, calculateDiskCacheSize(dir))
        }

        private fun createOkHttpClient(cacheDir: File, maxSize: Long): OkHttpClient {
            return OkHttpClient.Builder()
                .cache(Cache(cacheDir, maxSize))
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
        }
    }
}
/**
 * Create new downloader that uses OkHttp. This will install an image cache into the specified
 * directory.
 *
 * @param cacheDir The directory in which the cache should be stored
 */
