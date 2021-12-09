package com.ducnn17.movieCGV.api

import android.content.Context
import com.ducnn17.movieCGV.R
import com.google.gson.GsonBuilder
import com.ducnn17.movieCGV.utils.core.AppInfo
import com.ducnn17.movieCGV.utils.core.NetworkUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ApiClient {
    private val requestTimeout = 60
    private var token = ""
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun okHttpClient(context: Context): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
            .addInterceptor {
                if (!NetworkUtils.isConnected(context))
                    throw NoConnectionException(context.getString(R.string.msg_no_network))
                val request = it.request().newBuilder()
                it.proceed(request.build())
            }.addInterceptor(logging)

        return httpClient.build()
    }

    fun withToken(t: String?): ApiClient {
        token = t.toString()
        Timber.d("! $token")
        return this
    }

    fun client(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppInfo.apiUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .client(okHttpClient(context))
            .build()
    }
}
