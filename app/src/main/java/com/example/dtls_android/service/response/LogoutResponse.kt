package com.example.dtls_android.service.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("success")
    val successMsg: String
)
