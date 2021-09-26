package com.example.dtls_android.service

import com.example.dtls_android.service.response.Record
import retrofit2.Call
import retrofit2.http.*

interface RecordsApi {
    @GET("api/records/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getRecordsList(): Call<List<Record>>

    @POST("api/records/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun createRecord(@Body params: Record): Call<Record>

    @GET("api/records/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getRecordById(@Path("id") id: String): Call<Record>

    @PUT("api/records/{id}/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun updateRecord(@Path("id") id: String, @Body params: Record): Call<Record>

    @DELETE("api/records/{id}/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun deleteRecord(@Path("id")id: String): Call<Record>

    // TODO: Handle HTTP GET, POST, PATCH, DELETE
    // https://www.youtube.com/watch?v=TJpk7ezvtGo
}