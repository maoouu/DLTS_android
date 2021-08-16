package com.example.dtls_android.resources

import com.example.dtls_android.R

class MyResources {
    companion object {
        val indicator: Map<String, Int> = mapOf(
            "Approved" to R.drawable.status_approved,
            "Pending" to R.drawable.status_pending,
            "Recommended to the President" to R.drawable.status_recommended,
            "Denied" to R.drawable.status_denied,
            "Endorsed" to R.drawable.status_endorsed,
            "Returned" to R.drawable.status_returned,
            "Acknowledged" to R.drawable.status_acknowledged,
        )
        val outline: Map<String, Int> = mapOf(
            "Approved" to R.drawable.outlined_approved,
            "Pending" to R.drawable.outlined_pending,
            "Recommended to the President" to R.drawable.outlined_recommended,
            "Denied" to R.drawable.outlined_denied,
            "Endorsed" to R.drawable.outlined_endorsed,
            "Returned" to R.drawable.outlined_returned,
            "Acknowledged" to R.drawable.outlined_acknowledged,
        )
        val color: Map<String, Int> = mapOf(
            "Approved" to R.color.approved,
            "Pending" to R.color.pending,
            "Recommended to the President" to R.color.recommended,
            "Denied" to R.color.denied,
            "Endorsed" to R.color.endorsed,
            "Returned" to R.color.returned,
            "Acknowledged" to R.color.acknowledged,
        )
    }
}