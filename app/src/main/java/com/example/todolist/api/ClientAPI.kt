package com.example.todolist.api

import android.content.Context
import com.example.todolist.data.TokenPreferences
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClientAPI {

    private var protocol: ProtocolAPI? = null

    fun getService(context: Context) : ProtocolAPI? {
        val gson = GsonBuilder().setLenient().create()

        val client = getDefaultOkHttpClient(context)

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(ProtocolAPI.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ProtocolAPI::class.java)
    }

    private fun getDefaultOkHttpClient(context: Context): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()

        client.connectTimeout(60, TimeUnit.SECONDS)
        client.writeTimeout(60, TimeUnit.SECONDS)
        client.readTimeout(60, TimeUnit.SECONDS)
        client.addInterceptor(logging)

        client.addInterceptor { chain ->
            val request = chain.request().newBuilder()
            TokenPreferences.getToken(context)?.let { accessToken ->
                request.addHeader("Authorization", accessToken.userId)
            }
            chain.proceed(request.build())
        }

        return client.build()

    }

}