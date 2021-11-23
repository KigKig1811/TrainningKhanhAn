package com.vnpay.anlmk.networks

import android.util.Log
import com.vnpay.anlmk.data.constants.DatasourceProperties
import com.vnpay.anlmk.networks.entities.restful.CreateRequest
import com.vnpay.anlmk.utils.AESService
import com.vnpay.anlmk.utils.extensions.getSafeString
import com.vnpay.anlmk.utils.extensions.safeLog
import okhttp3.*
import okio.Buffer
import okio.ByteString
import org.json.JSONObject
import java.io.IOException
import java.net.UnknownHostException
import java.util.*


class SSLInterceptor : Interceptor {

    private val certs = ArrayList<String>()

    init {
        certs.add(DatasourceProperties.CERT)
    }

    override fun intercept(chain: Interceptor.Chain): Response? {
        var response: Response? = null
        try {
            val mediaType = MediaType.parse("application/json")
            val oldBody = chain.request().body()
            val buffer = Buffer()
            oldBody!!.writeTo(buffer)
            val strOldBody = buffer.readUtf8()
            Log.wtf("DATA_REQ", strOldBody)
            val dataRealRequest = CreateRequest.create(strOldBody)
            Log.wtf("URRRL", "URL: "+chain.request().url())
            val body = RequestBody.create(mediaType, dataRealRequest)

            val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Connection","keep-alive")
                .addHeader("Content-Length","${dataRealRequest.length}")
                .url(chain.request().url())
                .post(body).build()

            response = chain.proceed(request)
//                    checkSSLCertPinning(response)
            if (response.isSuccessful) {
                var responseServer = response.body()?.string()
                Log.wtf("DATA_RES", "RES: $responseServer")
                val data = JSONObject(responseServer)
                val mac = data.getString("m")

                responseServer = AESService.getInstance().decrypt(DatasourceProperties.KEY_DEFAULT,
                    data.getString("e"))
                Log.wtf("DATA_RES", "Decrypted: $responseServer")


                val resJson: JSONObject?
                resJson = try {
                    JSONObject(responseServer)
                } catch (e: Exception) {
                    null
                }

                if (resJson == null) {
                    response = response.newBuilder().code(503)
                        .body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                responseServer ?: ""
                            )
                        )
//                        .addHeader("mid", getMid(strOldBody))
                        .addHeader("code", "503")
                        .build()
                } else if ("00" != resJson.getSafeString("code")) {
                    response = response.newBuilder().code(401).message(resJson.getSafeString("des"))
                        .addHeader("mid", resJson.getSafeString("mid"))
                        .addHeader("code", resJson.getSafeString("code"))
                        .body(
                            ResponseBody.create(
                                MediaType.parse("application/json"),
                                responseServer ?: ""
                            )
                        ).build()
                } else {
                    response =
                        response.newBuilder().code(200)
                            .addHeader("mid", resJson.getSafeString("mid"))
                            .addHeader("code", "00")
                            .body(
                                ResponseBody.create(
                                    MediaType.parse("application/json"),
                                    responseServer ?: ""
                                )
                            ).build()


                }
            }
            return response
        } catch (unknown: UnknownHostException){
            throw unknown
        }
        catch (e: java.lang.Exception) {
            e.safeLog()
            e.printStackTrace()
            return Response.Builder()
                .code(503)
                .message("IOException")
//                .addHeader("mid", getMid(strOldBody))
                .addHeader("code", "IO")
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), "{\"code\":\"-1\"}"))
                .request(
                    Request.Builder()
                        .url(DatasourceProperties.SERVER_URL)
                        .addHeader("Connection","keep-alive")
                        .get()
                        .build()
                )
                .build()
        }
    }

    private fun getMid(strOldBody: String?): String {
        try {
            val json = JSONObject(strOldBody)
            return json.getSafeString("mid")
        } catch (e: java.lang.Exception) {

        }
        return ""
    }

    @Throws(IOException::class)
    private fun checkSSLCertPinning(response: Response?) {
        if (response != null) {
            val handshake = response.handshake()

            if (handshake != null) {
                if (handshake.peerCertificates() == null || handshake.peerCertificates().isEmpty()) {
                    throw SSLEmptyException()
                } else {
                    for (c in handshake.peerCertificates()) {
                        val b = ByteString.of(*c.publicKey.encoded).sha256()
                        for (cert in certs) {
                            val orig = ByteString.decodeBase64(cert)
                            if (orig == b) {
                                return
                            }
                        }
                    }
                    throw SSLEmptyException()
                }
            }
        }
    }

}

data class AppException(val code: String, val des: String, val source: String) : RuntimeException()