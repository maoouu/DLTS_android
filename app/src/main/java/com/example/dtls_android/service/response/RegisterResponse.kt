package com.example.dtls_android.service.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val successMsg: String
)
