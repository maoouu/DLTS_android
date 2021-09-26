package com.example.dtls_android.service.response

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("author")
    var author: String = "",
    @SerializedName("date_created")
    val dateCreated: String = "",
    @SerializedName("date_modified")
    var dateModified: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("status")
    var status: String = ""
)
