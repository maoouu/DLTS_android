package com.example.dtls_android

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.databinding.ItemLogBinding
import com.example.dtls_android.resources.MyResources
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
        private val binding = ItemLogBinding.bind(view)
        private val style = MyResources
        private lateinit var viewAuthor: TextView
        private lateinit var viewDesc: TextView
        private lateinit var viewDate: TextView
        private lateinit var viewStatusIndicator: ImageView
        private lateinit var viewStatusText: TextView

        fun bind(record: RecordsResponse) = with(itemView) {
            binding.cardTextTitle.text = record.author
            binding.cardTextStatus.text = record.status
            binding.cardTextDate.text = getFormattedDate(record.dateCreated)
            binding.cardTextDescription.text = record.description

            decorateStatus(this.context, record.status)
            setOnClickListener { viewItem(this, record) }
            setOnLongClickListener {
                //TODO: Create pop-up menu for Edit and Delete
                return@setOnLongClickListener true
            }
        }

        private fun viewItem(view: View, record: RecordsResponse) {
            val card = LayoutInflater.from(view.context).inflate(R.layout.activity_view_log, null)
            initializeCard(card)
            setCardValues(view.context, record)
            decorateViewStatus(view.context, record.status)

            val dialogBuilder = prepareDialog(view)
            val dialog = createDialog(dialogBuilder, view)
            dialog.show()
        }

        private fun prepareDialog(view: View): AlertDialog.Builder {
            val viewDialogBuilder = AlertDialog.Builder(view.context)
            viewDialogBuilder.setView(view)

            return viewDialogBuilder
        }

        private fun createDialog(viewDialogBuilder: AlertDialog.Builder, view: View): AlertDialog {
            val colorDrawable = ColorDrawable(ContextCompat.getColor(view.context, R.color.light_grey))
            val viewDialog = viewDialogBuilder.create()
            viewDialog.window?.setBackgroundDrawable(colorDrawable)
            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view)
            }
            viewDialog.setContentView(view)

            return viewDialog
        }

        private fun decorateViewStatus(context: Context, status: String, ) {
            setIndicatorColor(status, viewStatusIndicator)
            setTextColor(context, status, viewStatusText)
        }

        private fun setCardValues(context: Context, record: RecordsResponse) {
            viewAuthor.text = record.author
            viewDesc.text = record.description
            viewDate.text = context.getString(R.string.viewDateTextString, getFormattedDate(record.dateModified))
            viewStatusText.text = record.status
        }

        private fun initializeCard(card: View) {
            viewAuthor = card.findViewById(R.id.viewCardAuthor)
            viewDesc = card.findViewById(R.id.viewCardDesc)
            viewDate = card.findViewById(R.id.viewCardDate)
            viewStatusIndicator = card.findViewById(R.id.viewCardStatusCircle)
            viewStatusText = card.findViewById(R.id.viewCardStatusText)
        }

        private fun decorateStatus(context: Context, status: String) {
            setIndicatorColor(status, binding.cardTextIndicator)
            setTextColor(context, status, binding.cardTextStatus)
        }

        private fun setIndicatorColor(status: String, cardTextIndicator: ImageView) {
            style.indicator[status]?.let { cardTextIndicator.setImageResource(it) }
        }

        private fun setTextColor(context: Context, status: String, cardTextStatus: TextView) {
            style.color[status]?.let {
                cardTextStatus.setTextColor(ContextCompat.getColor(context, it))
            }
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