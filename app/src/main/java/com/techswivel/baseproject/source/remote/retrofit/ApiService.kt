package com.techswivel.baseproject.source.remote.retrofit

import com.google.gson.GsonBuilder
import com.techswivel.baseproject.BuildConfig
import com.techswivel.baseproject.constant.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * This class is entroy point for retorfit framework.
 */
object ApiService {


    /**
     * Base url for API
     */
//  private static final String BASE_URL = "http://192.168.10.7/dev/trade/public/api/";
    /**
     * OkHttpClient handle server Communication with Retrofit framework
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(DataInterceptor())
        .connectTimeout(3, TimeUnit.MINUTES)
        .readTimeout(3, TimeUnit.MINUTES)
        .writeTimeout(3, TimeUnit.MINUTES)
        .build()

    /**
     * Its a Gson Library is used to convert Gson to Model Class
     */
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    /**
     * Retrofit Framework work With OkHttpClient, Gson and Rxjava to handle server request and parce into Model classes
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()


    /**
     * Retrofit Framework work With OkHttpClient, Gson and Rxjava to handle server request and parce into Model classes
     */
    private val directionRetrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/directions/")
        // .addConverterFactory(ResponseConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()

    /**
     * Retrofit Framework work With OkHttpClient, Gson and Rxjava to handle server request and parce into Model classes
     */
    private val googleRetrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/oauth2/v4/")
        // .addConverterFactory(ResponseConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()


    /**
     * This method is used to get Instance of Api Class
     *
     * @return <Api> return Instance of Api class
    </Api> */
    @JvmStatic
    fun get(): ApiResponse {
        return retrofit.create(ApiResponse::class.java)
    }

    /**
     * This method is used to get Instance of Api Class
     *
     * @return <Api> return Instance of Api class
    </Api> */
    @JvmStatic
    fun getDirectionResponse(): ApiResponse {
        return directionRetrofit.create(ApiResponse::class.java)
    }

    /**
     * This method is used to get Instance of Api Class
     *
     * @return <Api> return Instance of Api class
    </Api> */
    @JvmStatic
    fun getGoogleResponse(): ApiResponse {
        return googleRetrofit.create(ApiResponse::class.java)
    }

    /**
     * This method is used to get Instance of Api Class
     *
     * @return return Instance of Retrofit
    </Api> */
    @JvmStatic
    fun getRetrofitObject(): Retrofit {
        return retrofit
    }

    private fun getBaseUrl(): String {
        return when {
            BuildConfig.FLAVOR.equals(Constants.STAGING) -> {
                Constants.STAGING_SERVER_URL
            }
            BuildConfig.FLAVOR.equals(Constants.PRODUCTION) -> {
                Constants.PRODUCTION_SERVER_URL
            }
            BuildConfig.FLAVOR.equals(Constants.ACCEPTANCE) -> {
                Constants.ACCEPTANCE_SERVER_URL
            }
            else -> {
                Constants.DEVELOPMENT_SERVER_URL
            }
        }
    }
}