//package com.vnpay.base.networks
//
//import kotlinx.coroutines.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.HttpException
//import retrofit2.Response
//import kotlin.coroutines.resume
//import kotlin.coroutines.resumeWithException
//
//public suspend fun <T : Any> Call<T>.await(): T {
//    return suspendCancellableCoroutine { continuation ->
//        enqueue(object : Callback<T> {
//            override fun onResponse(call: Call<T>?, response: Response<T?>) {
//                continuation.resumeWith(runCatching {
//                    if (response.isSuccessful) {
//                        response.body()
//                            ?: throw NullPointerException("Response body is null: $response")
//                    } else {
//                        throw HttpException(response)
//                    }
//                })
//            }
//
//            override fun onFailure(call: Call<T>, t: Throwable) {
//                // Don't bother with resuming the continuation if it is already cancelled.
//                if (continuation.isCancelled) return
//                continuation.resumeWithException(t)
//            }
//        })
//
//        registerOnCompletion(continuation)
//    }
//}
//
//suspend fun <T : Any?> Call<T>.awaitResponse(): Response<T> {
//    return suspendCancellableCoroutine { continuation ->
//        enqueue(object : Callback<T> {
//            override fun onResponse(call: Call<T>?, response: Response<T>) {
//                continuation.resume(response)
//            }
//
//            override fun onFailure(call: Call<T>, t: Throwable) {
//                // Don't bother with resuming the continuation if it is already cancelled.
//                if (continuation.isCancelled) return
//                continuation.resumeWithException(t)
//            }
//        })
//
//        registerOnCompletion(continuation)
//    }
//}
//
//
//suspend fun <T : Any> Deferred<Response<T>>.awaitResult(): Result<T> {
//    return suspendCancellableCoroutine { continuation ->
//
//        GlobalScope.launch {
//            try {
//                val response = await()
//                continuation.resume(
//                    if (response.isSuccessful) {
//                        val body = response.body()
//                        body?.let {
//                            Result.Ok(it, response.raw())
//                        } ?: "error".let {
//                            if (response.code() ==200){
//                                Result.Exception(Exception("body is empty"))
//                            }
//                            else{
//                                Result.Exception(NullPointerException("Response body is null"))
//                            }
//                        }
//                    } else {
//                        Result.Error(HttpException(response), response.raw())
//                    }
//                )
//            }
//            catch (e:Throwable){
//                //  Log.e("DeferredAwait",e.message)
//                continuation.resume(Result.Exception(e))
//            }
//
//        }
//
//        registerOnCompletion(continuation)
//    }
//}
//
//private fun Deferred<Response<*>>.registerOnCompletion(continuation: CancellableContinuation<*>) {
//    continuation.invokeOnCancellation {
//        if (continuation.isCancelled)
//            try {
//                cancel()
//            } catch (ex: Throwable) {
//                //Ignore cancel exception
//                ex.printStackTrace()
//            }
//    }
//}
//
//private fun Call<*>.registerOnCompletion(continuation: CancellableContinuation<*>) {
//    continuation.invokeOnCancellation {
//        try {
//            cancel()
//        } catch (ex: Throwable) {
//            //Ignore cancel exception
//        }
//    }
//}