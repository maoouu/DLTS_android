package com.example.dtls_android

import java.time.LocalDateTime

data class Log(
    val id: Int,
    var author: String,
    var status: String,
    var createdAt: LocalDateTime,
    var description: String = "None.",
    )