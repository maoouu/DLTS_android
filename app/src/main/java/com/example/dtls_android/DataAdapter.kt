package com.example.dtls_android

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.card.CardUtils
import com.example.dtls_android.card.ViewCard
import com.example.dtls_android.databinding.ItemLogBinding
import com.example.dtls_android.service.response.Record

class DataAdapter(private val longClickListener: OnItemLongClickListener): RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    interface OnItemLongClickListener {
        fun showHoldMenu(record: Record, view: View)
    }

    var recordsList = mutableListOf<Record>()

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
        holder.itemView.setOnLongClickListener {
            longClickListener.showHoldMenu(recordsList[position], it)
            return@setOnLongClickListener true
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemLogBinding.bind(view)

        fun bind(record: Record) = with(itemView) {
            binding.cardTextTitle.text = record.author
            binding.cardTextStatus.text = record.status
            binding.cardTextDate.text = CardUtils.getFormattedDate(record.dateCreated)
            binding.cardTextDescription.text = record.description

            decorateStatus(this.context, record.status)
            setOnClickListener { viewItem(this, recordsList[adapterPosition]) }
        }

        private fun viewItem(view: View, record: Record) = with(view) {
            val card = LayoutInflater.from(this.context).inflate(R.layout.activity_view_log, null)
            val viewCard = ViewCard(this.context, card)
            viewCard.setCardValues(record)
            viewCard.decorateView(record.status)

            val dialogBuilder = prepareDialog(card)
            val dialog = createDialog(dialogBuilder, this)
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

        private fun decorateStatus(context: Context, status: String) {
            CardUtils.setIndicatorColor(status, binding.cardTextIndicator)
            CardUtils.setTextColor(context, status, binding.cardTextStatus)
        }
    }
}