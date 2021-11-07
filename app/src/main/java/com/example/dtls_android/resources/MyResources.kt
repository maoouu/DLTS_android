package com.example.dtls_android.resources

import com.example.dtls_android.R
import java.time.LocalDateTime

class MyResources {
    companion object {
        val indicator: Map<String, Int> = mapOf(
            "Approved" to R.drawable.status_approved,
            "Pending" to R.drawable.status_pending,
            "Recommended to President" to R.drawable.status_recommended,
            "Denied" to R.drawable.status_denied,
            "Endorsed" to R.drawable.status_endorsed,
            "Returned" to R.drawable.status_returned,
            "Acknowledged" to R.drawable.status_acknowledged,
        )
        val outline: Map<String, Int> = mapOf(
            "Approved" to R.drawable.outlined_approved,
            "Pending" to R.drawable.outlined_pending,
            "Recommended to President" to R.drawable.outlined_recommended,
            "Denied" to R.drawable.outlined_denied,
            "Endorsed" to R.drawable.outlined_endorsed,
            "Returned" to R.drawable.outlined_returned,
            "Acknowledged" to R.drawable.outlined_acknowledged,
        )
        val color: Map<String, Int> = mapOf(
            "Approved" to R.color.approved,
            "Pending" to R.color.pending,
            "Recommended to President" to R.color.recommended,
            "Denied" to R.color.denied,
            "Endorsed" to R.color.endorsed,
            "Returned" to R.color.returned,
            "Acknowledged" to R.color.acknowledged,
        )

        val author: Array<String> = arrayOf(
            "Corey",
            "Alvin",
            "Jake",
            "Bob",
            "John Doe",
        )

        val status: Array<String> = arrayOf(
            "Approved",
            "Pending",
            "Acknowledged",
            "Denied",
            "Recommended to President",
        )

        val date: Array<LocalDateTime> = arrayOf(
            LocalDateTime.of(2020, 7, 15, 10, 30),
            LocalDateTime.of(2020, 8, 1, 12, 25),
            LocalDateTime.of(2020, 11, 23, 16, 6),
            LocalDateTime.of(2020, 12, 30, 13, 1),
            LocalDateTime.of(2020, 10, 30, 12, 5)
        )

        val description: Array<String> = arrayOf(
            "Awaiting Request",
            "Info on Project",
            "Hello World!",
            "Travel Budget",
            "Request to Promote",
        )
    }
}