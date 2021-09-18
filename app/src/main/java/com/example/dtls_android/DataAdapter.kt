package com.example.dtls_android

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.card.CardUtils
import com.example.dtls_android.card.ViewCard
import com.example.dtls_android.databinding.ItemLogBinding
import com.example.dtls_android.service.response.Record
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText

class DataAdapter: RecyclerView.Adapter<DataAdapter.ViewHolder>() {

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
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemLogBinding.bind(view)

        private lateinit var authorEditField: TextInputEditText
        private lateinit var descEditField: TextInputEditText
        private lateinit var statusFieldAuto: MaterialAutoCompleteTextView

        fun bind(record: Record) = with(itemView) {
            binding.cardTextTitle.text = record.author
            binding.cardTextStatus.text = record.status
            binding.cardTextDate.text = CardUtils.getFormattedDate(record.dateCreated)
            binding.cardTextDescription.text = record.description

            decorateStatus(this.context, record.status)
            setOnClickListener { viewItem(this, recordsList[adapterPosition]) }
            setOnLongClickListener {
                //TODO: Create pop-up menu for Edit and Delete
                createPopupMenu(this, it, adapterPosition)
                return@setOnLongClickListener true
            }
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

        private fun createPopupMenu(itemView: View, view: View, adapterPosition: Int) {
            val popupMenu = PopupMenu(itemView.context, view)
            popupMenu.inflate(R.menu.card_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editCard -> {
                        editItem(itemView, adapterPosition)
                        true
                    }
                    R.id.deleteCard -> {
                        deleteItem(itemView, adapterPosition)
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }

        private fun editItem(itemView: View, adapterPosition: Int) {
            val record = recordsList[adapterPosition]
            val editView = LayoutInflater.from(itemView.context).inflate(R.layout.activity_edit_log, null)
            setupView(editView, record)

            val dialogBuilder = prepareDialog(editView)
            val dialog = createEditDialog(editView, dialogBuilder, record)
        }

        private fun setupView(editView: View, record: Record) {
            authorEditField = editView.findViewById(R.id.authorInput)
            descEditField = editView.findViewById(R.id.descriptionInput)
            val status: Array<String> = editView.resources.getStringArray(R.array.status)
            val dropdownAdapter = ArrayAdapter(editView.context, R.layout.dropdown_item, status)
            statusFieldAuto = editView.findViewById(R.id.statusFieldAuto)

            authorEditField.setText(record.author)
            statusFieldAuto.setText(record.status)
            descEditField.setText(record.description)
            statusFieldAuto.setAdapter(dropdownAdapter)
        }

        private fun deleteItem(itemView: View, adapterPosition: Int) {
            val dialogBuilder = prepareDialog(itemView)
            val dialog = createDeleteDialog(itemView, dialogBuilder, adapterPosition)
            dialog.show()
        }

        private fun createEditDialog(view:View, viewDialogBuilder: AlertDialog.Builder, record: Record): AlertDialog {
            val editDialog = viewDialogBuilder.setView(view)
                .setPositiveButton("Save") {
                    dialog,_ ->
                    record.author = authorEditField.text.toString()
                    //TODO: Let HTTP requests do the work
                }
                .setNegativeButton("Cancel") {
                    dialog,_ -> dialog.dismiss()
                }
                .create()

            return editDialog
        }

        private fun createDeleteDialog(view:View, viewDialogBuilder: AlertDialog.Builder, adapterPosition: Int): AlertDialog {
            val deleteDialog = viewDialogBuilder
                .setTitle("Delete")
                .setIcon(R.drawable.ic_warning)
                .setMessage ("Are you sure you want to permanently delete this item?")
                .setPositiveButton("Yes") {
                    dialog,_->
                    recordsList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    Toast.makeText(view.context, "Item has been deleted", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("No") {
                    dialog,_->
                    dialog.dismiss()
                }
                .create()

            return deleteDialog
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