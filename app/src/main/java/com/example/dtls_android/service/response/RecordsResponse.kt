package com.example.dtls_android.service.response


import com.google.gson.annotations.SerializedName

data class RecordsResponse(
    @SerializedName("author")
    val author: String = "",
    @SerializedName("date_created")
    val dateCreated: String = "",
    @SerializedName("date_modified")
    val dateModified: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("status")
    val status: String = ""
)