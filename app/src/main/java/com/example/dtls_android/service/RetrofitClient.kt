package com.example.dtls_android.service

import com.google.gson.GsonBuilder
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RetrofitClient {

    companion object {
        val baseURL = "http://10.0.2.2:8000/"

        val webservice: RecordsApi by lazy {

            val logging = HttpLoggingInterceptor()
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
            client.addInterceptor(logging)

            Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(Json.asConverterFactory(contentType))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(RecordsApi::class.java)
        }
    }

}