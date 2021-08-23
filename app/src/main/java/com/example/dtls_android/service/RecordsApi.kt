package com.example.dtls_android.service

import com.example.dtls_android.service.response.RecordsResponse
import retrofit2.http.GET
import io.reactivex.Observable

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
    fun getRecordsList(): Observable<List<RecordsResponse>>
}