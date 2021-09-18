package com.example.dtls_android.service

import com.example.dtls_android.service.response.Record
import com.example.dtls_android.service.response.RecordResponse
import com.example.dtls_android.service.response.RecordsList
import retrofit2.Call
import retrofit2.http.*

interface RecordsApi {

    /**
    @GET("api/records/")
    suspend fun getRecordsList(
        @Query("author") author: String,
        @Query("description") description: String,
        @Query("date_created") dateCreated: String,
        @Query("date_modified") dateModified: String,
        @Query("status") status: String
    ): Observable<List<RecordsResponseItem>>
    **/

    @GET("api/records/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    //fun getRecordsList(): Call<RecordsList>
    fun getRecordsList(): Call<List<Record>>

    @POST("api/records/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun createRecord(@Body params: Record): Call<RecordResponse>

    @PATCH("api/records/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun updateRecord(@Path("id") id: String, @Body params: Record): Call<RecordResponse>

    @DELETE("api/records/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun deleteRecord(@Path("id")id: String): Call<RecordResponse>

    // TODO: Handle HTTP GET, POST, PATCH, DELETE
    // https://www.youtube.com/watch?v=TJpk7ezvtGo
}