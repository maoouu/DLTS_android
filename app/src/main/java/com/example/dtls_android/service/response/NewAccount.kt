package com.example.dtls_android.service.response

import com.google.gson.annotations.SerializedName

data class NewAccount(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("email")
    var email: String
)
