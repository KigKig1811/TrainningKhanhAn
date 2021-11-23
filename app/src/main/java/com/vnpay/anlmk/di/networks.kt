package com.vnpay.anlmk.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vnpay.anlmk.data.Service
import com.vnpay.anlmk.data.constants.DatasourceProperties
import com.vnpay.anlmk.data.remote.MainRemote
import com.vnpay.anlmk.networks.SSLInterceptor
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

val networks = module {
    single { createHostname(DatasourceProperties.getDomain()) }
    single { createCertificatePinner(DatasourceProperties.getDomain(), DatasourceProperties.CERT) }
    single { createOkHttpCache(get()) }
    single { createGson() }
    single { createOkHttpClient(get(), get(), get()) }
    factory<OkHttpClient>(named("newOkHttp"))
    {
        newBuildOkHttp(
            get(),
            DatasourceProperties.TIMEOUT_CONNECT,
            DatasourceProperties.TIMEOUT_READ
        )
    }
    single { createMediaType() }
    single<Service> { createService(get(named("newOkHttp"))) }


}

fun createOkHttpClient(
    cache: Cache,
    hostname: HostnameVerifier,
    certificatePinner: CertificatePinner
): OkHttpClient {
    return OkHttpClient.Builder()
        .cache(cache)
        .hostnameVerifier(hostname)
        .certificatePinner(certificatePinner)
        .build()
}

fun createOkHttpCache(context: Context): Cache {
    return Cache(context.cacheDir, Long.MAX_VALUE)
}

fun createCertificatePinner(domain: String, cert: String): CertificatePinner {
    return CertificatePinner.Builder()
        .add(domain, "sha256/$cert")
        .build()
}

fun createHostname(domain: String): HostnameVerifier {
    return VerifyHostName(domain)
}


fun newBuildOkHttp(
    client: OkHttpClient,
    timeoutConnect: Long,
    timeoutReader: Long): OkHttpClient {

    return client.newBuilder()
        .connectTimeout(timeoutConnect, TimeUnit.SECONDS)
        .addInterceptor(SSLInterceptor())
        .readTimeout(timeoutReader, TimeUnit.SECONDS).build()
}

fun createGson(): Gson {
    return GsonBuilder().create()
}

fun createMediaType(): MediaType? {
    return MediaType.parse("application/json")
}


fun createService(client: OkHttpClient): Service {
    return MainRemote(client).getService()
}

class VerifyHostName(var hn: String) : HostnameVerifier {
    override fun verify(hostname: String, session: SSLSession): Boolean {
        val hv = HttpsURLConnection.getDefaultHostnameVerifier()
        return hv.verify(hn, session) && hn == hostname
    }
}


