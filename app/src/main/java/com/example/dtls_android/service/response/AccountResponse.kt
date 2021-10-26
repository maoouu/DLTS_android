package com.example.dtls_android.service.response

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("expiry")
    val expiry: String,
    @SerializedName("token")
    val token: String
)