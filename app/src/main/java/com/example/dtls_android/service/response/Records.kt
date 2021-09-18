package com.example.dtls_android.service.response

import com.google.gson.annotations.SerializedName


//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable

data class RecordsList(
    var data: List<Record>
)

data class Record(
    @SerializedName("id")
    val id: Int = 0,
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

data class RecordResponse(
    var data: Record
)
