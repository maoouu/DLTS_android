package com.example.dtls_android.service.response

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String,
)
