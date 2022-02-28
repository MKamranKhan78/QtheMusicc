package com.techswivel.qthemusic.source.remote.retrofit

import android.annotation.SuppressLint
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException

/**
 * Created by Nasar Iqbal on 2/27/2018.
 */
class DataInterceptor : Interceptor {
    private external fun getStagingApiKey(): String?
    private external fun getDevelopmentApiKey(): String?
    private external fun getAcceptanceApiKey(): String?
    private external fun getProductionApiKey(): String?

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        var requestLog = String.format(
            "Sending request %s on %s%n%s",
            request.url(), chain.connection(), request.headers()
        )
        if (request.method().compareTo("post", ignoreCase = true) == 0) {
            requestLog = "\n" + requestLog + "\n" + bodyToString(request)
        }
        Log.i(TAG, "request\n$requestLog")
        val response: Response
        if (PrefUtils.getString(QTheMusicApplication.getContext(), CommonKeys.KEY_JWT) != null) {
            val jwt = "Bearer " + PrefUtils.getString(
                QTheMusicApplication.getContext(),
                CommonKeys.KEY_JWT
            )
            Log.d(TAG, "JWT : ".plus(jwt))
            val requestBuilder = request.newBuilder()
                .header("api-key", getApiKey())
                .header("Authorization", jwt)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
            val request2 = requestBuilder.build()
            response = chain.proceed(request2)
            Log.d(TAG, "intercept: with Token ".plus(jwt))
        } else {
            Log.d(TAG, "intercept: with out token ")
            response = chain.proceed(request)
        }
        val t2 = System.nanoTime()
        @SuppressLint("DefaultLocale") val responseLog = String.format(
            "Received response for %s in %.1fms%n%s",
            response.request().url(), (t2 - t1) / 1e6, response.headers()
        )
        assert(response.body() != null)
        val bodyString = response.body()!!.string()
        Log.i(TAG, "response\n$responseLog\n$bodyString")
        return response.newBuilder()
            .body(ResponseBody.create(response.body()!!.contentType(), bodyString))
            .build()
    }

    private fun getApiKey(): String? {
        return when (BuildConfig.FLAVOR) {
            Constants.STAGING -> {
                getStagingApiKey()
            }
            Constants.DEVELOPMENT -> {
                getDevelopmentApiKey()
            }
            Constants.ACCEPTANCE -> {
                getAcceptanceApiKey()
            }
            else -> {
                getProductionApiKey()
            }
        }
    }

    companion object {
        private const val TAG = "DataInterceptor"
        private fun bodyToString(request: Request): String {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body()?.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: Exception) {
                "did not work"
            }
        }
    }
}