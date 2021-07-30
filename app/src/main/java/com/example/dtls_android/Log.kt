package com.example.dtls_android

import java.time.LocalDateTime

data class Log(
    val id: Long,
    var author: String,
    var description: String,
    var createdAt: LocalDateTime
    )