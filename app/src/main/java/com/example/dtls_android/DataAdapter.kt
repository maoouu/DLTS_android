package com.example.dtls_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.databinding.ItemLogBinding
import com.example.dtls_android.service.response.RecordsResponse
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DataAdapter(val context: Context, private val recordsList: List<RecordsResponse>):
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_log,
            parent, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recordsList[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(record: RecordsResponse) = with(itemView) {
            val binding = ItemLogBinding.bind(this)
            binding.cardTextTitle.text = record.author
            binding.cardTextStatus.text = record.status
            binding.cardTextDate.text = getFormattedDate(record.dateCreated)
            binding.cardTextDescription.text = record.description

        }

        private fun getFormattedDate(date: CharSequence): String {
            val from = LocalDate.parse(date)
            val now = LocalDate.now()
            val pattern = getDateStringFormat(from, now)
            val formatter = DateTimeFormatter.ofPattern(pattern)

            return from.format(formatter)
        }

        private fun getDateStringFormat(from: LocalDate, now: LocalDate): String {
            val timeDifference = Period.between(from, now)
            val days = timeDifference.days
            val years = timeDifference.years
            val WEEK = 7

            return if (years < 1) {
                if (days <= WEEK) {
                    "EEE"
                } else {
                    "L/dd"
                }
            } else {
                "L/dd/yy"
            }
        }
    }
}