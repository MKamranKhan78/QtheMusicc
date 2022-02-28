package com.techswivel.qthemusic.source.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*


class NetworkConnectionInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        if (!internetConnectionAvailable()) {
            throw NoConnectivityException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private fun internetConnectionAvailable(): Boolean {
        var inetAddress: InetAddress? = null
        try {
            val future: Future<InetAddress> =
                Executors.newSingleThreadExecutor().submit(Callable<InetAddress> {
                    try {
                        InetAddress.getByName("google.com")
                    } catch (e: UnknownHostException) {
                        null
                    }
                })
            inetAddress = future.get(6000, TimeUnit.MILLISECONDS);
            future.cancel(true);

        } catch (e: InterruptedException) {
        } catch (e: ExecutionException) {
        } catch (e: TimeoutException) {
        }
        return inetAddress != null && !inetAddress.equals("")
    }

}