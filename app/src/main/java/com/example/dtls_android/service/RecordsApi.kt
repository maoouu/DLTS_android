package com.example.dtls_android.service

import com.example.dtls_android.service.response.Account
import com.example.dtls_android.service.response.LoginResponse
import com.example.dtls_android.service.response.LogoutResponse
import com.example.dtls_android.service.response.Record
import retrofit2.Call
import retrofit2.http.*

interface RecordsApi {

    /**
     * Login endpoint for User. Returns an account response
     * containing the API-token and expiry date.
     */
    @POST("api/login/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun login(@Body params: Account): Call<LoginResponse>

    /**
     * Logout endpoint for User. Accepts token as authorization
     */
    @POST("api/logout/")
    fun logout(@Header("Authorization") token: String): Call<LogoutResponse>

    /**
     * Gets all the records from the API
     */
    @GET("api/records/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getRecordsList(@Header("Authorization") token: String): Call<List<Record>>
    //fun getRecordsList(): Call<List<Record>>

    /**
     * Creates a new record to the database
     */
    @POST("api/records/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun createRecord(
        @Body params: Record,
        @Header("Authorization") token: String
    ): Call<Record>

    /**
     * Get record by ID number.
     */
    @GET("api/records/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun getRecordById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<Record>

    /**
     * Accepts record ID and body parameters to update existing record data.
     */
    @PUT("api/records/{id}/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun updateRecord(
        @Path("id") id: String,
        @Body params: Record,
        @Header("Authorization") token: String
    ): Call<Record>

    /**
     * Accepts a record ID to delete from the database.
     */
    @DELETE("api/records/{id}/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun deleteRecord(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<Record>

}