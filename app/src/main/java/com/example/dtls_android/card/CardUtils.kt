package com.example.dtls_android.card

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.dtls_android.resources.MyResources
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class CardUtils {

    companion object {
        val style = MyResources

        fun getFormattedDate(date: String, pattern: String): String {
            val from = LocalDate.parse(date)
            val formatter = DateTimeFormatter.ofPattern(pattern)

            return from.format(formatter)
        }

        fun getFormattedDate(date: String): String {
            val from = LocalDate.parse(date)
            val now = LocalDate.now()
            val pattern = formatBasedOnDifference(from, now)
            val formatter = DateTimeFormatter.ofPattern(pattern)

            return from.format(formatter)
        }

        fun formatBasedOnDifference(from: LocalDate, to: LocalDate): String {
            val timeDifference = Period.between(from, to)
            val DAYS = timeDifference.days
            val YEARS = timeDifference.years
            val WEEK = 7

            return when (YEARS < 1) {
                true -> if(DAYS <= WEEK) "EEE" else "L/dd"
                false -> "L/dd/yy"
            }
        }

        fun setIndicatorColor(status: String, imageView: ImageView) {
            style.indicator[status]?.let {imageView.setImageResource(it)}
        }

        fun setTextColor(context: Context, status: String, textView: TextView) {
            style.color[status]?.let {
                textView.setTextColor(ContextCompat.getColor(context, it))
            }
        }

    }
}