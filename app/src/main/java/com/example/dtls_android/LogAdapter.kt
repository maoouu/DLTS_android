package com.example.dtls_android

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.databinding.ItemLogBinding
import com.example.dtls_android.resources.MyResources
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class LogAdapter(private val tempLogList: ArrayList<Log>, private val newLogList: ArrayList<Log>) :
    RecyclerView.Adapter<LogAdapter.LogViewHandler>() {

    private val style = MyResources

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHandler {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHandler(rootView)
    }

    override fun getItemCount(): Int {
        return tempLogList.size
    }

    override fun onBindViewHolder(holder: LogViewHandler, position: Int) {
        holder.bind(tempLogList[position])
    }

    inner class LogViewHandler(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemLogBinding.bind(itemView)

        fun bind(log: Log) = with(itemView) {
            val logDate = log.createdAt
            val formatter = formatBasedOnTimeDifference(logDate)
            val formattedDate = logDate.format(formatter)

            binding.cardTextTitle.text = log.author
            binding.cardTextStatus.text = log.status
            binding.cardTextDate.text = formattedDate
            binding.cardTextDescription.text = log.description

            style.indicator[log.status]?.let { binding.cardTextIndicator.setImageResource(it) }
            style.color[log.status]?.let { binding.cardTextStatus
                .setTextColor(ContextCompat.getColor(itemView.context, it)) }

            setOnClickListener {
                //TODO: view the card
                toastShort(itemView,"Item clicked!")
                viewCard(itemView, tempLogList[adapterPosition])
            }

            setOnLongClickListener {
                //TODO: pop-up menu for edit and delete
                popupMenu(adapterPosition, itemView, it)
                return@setOnLongClickListener true
            }
        }
    }

    private fun popupMenu(adapterPosition: Int, itemView: View, view: View) {
        val tempLogData = tempLogList[adapterPosition]
        val newLogData = newLogList[adapterPosition]
        val options = PopupMenu(itemView.context, view)
        options.inflate(R.menu.card_menu)
        options.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.editCard -> {
                    //TODO: edit prompt
                    toastShort(itemView,"Edit Button is clicked")
                    editCard(itemView, tempLogData, newLogData)
                    true
                }
                R.id.deleteCard -> {
                    //TODO: delete prompt
                    toastShort(itemView,"Delete Button is clicked")
                    deleteCard(itemView, tempLogList, newLogList, adapterPosition)
                    true
                }
                else -> true
            }
        }
        options.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(options)
        menu.javaClass
            .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)
    }

    private fun viewCard(view: View, position: Log) {
        val card = LayoutInflater.from(view.context).inflate(R.layout.activity_view_log, null)
        val author: TextView = card.findViewById(R.id.viewCardAuthor)
        val description: TextView = card.findViewById(R.id.viewCardDesc)
        val date: TextView = card.findViewById(R.id.viewCardDate)
        val statusOutline: LinearLayout = card.findViewById(R.id.viewCardStatusOutline)
        val statusCircle: ImageView = card.findViewById(R.id.viewCardStatusCircle)
        val statusText: TextView = card.findViewById(R.id.viewCardStatusText)

        val logDate = position.createdAt
        val formatter = formatBasedOnTimeDifference(logDate)
        val formattedDate = logDate.format(formatter)

        author.text = position.author
        description.text = position.description
        statusText.text = position.status
        date.text = view.context.getString(R.string.viewDateTextString, formattedDate)

        style.outline[position.status]?.let { statusOutline.setBackgroundResource(it) }
        style.indicator[position.status]?.let { statusCircle.setImageResource(it) }
        style.color[position.status]?.let { statusText.setTextColor(
            ContextCompat.getColor(view.context, it)
        ) }

        val infoDialogBuilder = AlertDialog.Builder(card.context)
        infoDialogBuilder.setView(card)

        val infoDialog = infoDialogBuilder.create()
        infoDialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat
            .getColor(card.context, R.color.light_grey)))
        infoDialog.setContentView(card)
        infoDialog.show()
    }

    private fun deleteCard(view: View, tempLogList: ArrayList<Log>, newLogList: ArrayList<Log>,
                           adapterPosition: Int) {
        AlertDialog.Builder(view.context)

            .setTitle("Delete")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Are you sure you want to permanently delete this item?")
            .setPositiveButton("Yes") {
                    dialog,_->
                tempLogList.removeAt(adapterPosition)
                newLogList.removeAt(adapterPosition)
                notifyDataSetChanged()
                toastShort(view, "Item has been deleted.")
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                    dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun editCard(view: View, tempLogData: Log, newLogData: Log) {
        val editView = LayoutInflater.from(view.context).inflate(R.layout.activity_edit_log, null)
        val authorField: TextInputEditText = editView.findViewById(R.id.authorInput)
        val descField: TextInputEditText = editView.findViewById(R.id.descriptionInput)
        val status: Array<String> = editView.resources.getStringArray(R.array.status)
        val dropdownAdapter = ArrayAdapter(editView.context, R.layout.dropdown_item, status)
        val statusFieldAuto: MaterialAutoCompleteTextView = editView.findViewById(R.id.statusFieldAuto)

        authorField.setText(tempLogData.author)
        statusFieldAuto.setText(tempLogData.status)
        descField.setText(tempLogData.description)
        statusFieldAuto.setAdapter(dropdownAdapter)

        val infoDialogBuilder = AlertDialog.Builder(editView.context)
        infoDialogBuilder.setView(editView)
            .setPositiveButton("Save") {
                dialog,_->
                tempLogData.author = authorField.text.toString()
                tempLogData.description = descField.text.toString()
                tempLogData.status = statusFieldAuto.text.toString()
                tempLogData.createdAt = LocalDateTime.now()

                newLogData.author = tempLogData.author
                newLogData.description = tempLogData.description
                newLogData.status = tempLogData.status
                newLogData.createdAt = tempLogData.createdAt

                notifyDataSetChanged()
                toastShort(editView, "Entry has been modified successfully.")
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") {
                dialog,_->
                dialog.dismiss()
            }
        val infoDialog = infoDialogBuilder.create()
        infoDialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat
            .getColor(editView.context, R.color.light_grey)))
        infoDialog.setContentView(editView)
        infoDialog.show()

        //Disable save button when there are no changes
        infoDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

        authorField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                val isModified: Boolean = !(
                        authorField.text.toString() == tempLogData.author &&
                        descField.text.toString() == tempLogData.description &&
                        statusFieldAuto.text.toString() == tempLogData.status
                )
                infoDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isModified
            }
        })

        // TODO: Add a listener for StatusField
        statusFieldAuto.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                val isModified: Boolean = !(
                        authorField.text.toString() == tempLogData.author &&
                                descField.text.toString() == tempLogData.description &&
                                statusFieldAuto.text.toString() == tempLogData.status
                        )
                infoDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isModified
            }
        })

        descField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                val isModified: Boolean = !(
                        authorField.text.toString() == tempLogData.author &&
                                descField.text.toString() == tempLogData.description &&
                                statusFieldAuto.text.toString() == tempLogData.status
                        )
                infoDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isModified
            }
        })
    }

    private fun formatBasedOnTimeDifference(recordedDate: LocalDateTime): DateTimeFormatter {
        val now = LocalDateTime.now()
        val days = recordedDate.until(now, ChronoUnit.DAYS)
        val years = recordedDate.until(now, ChronoUnit.YEARS)

        return if (years < 1) {
            if (days <= 7) {
                if (days <= 1) {
                    DateTimeFormatter.ofPattern("HH:mm")
                } else {
                    DateTimeFormatter.ofPattern("EEE HH:mm")
                }
            } else {
                DateTimeFormatter.ofPattern("EEE L/dd")
            }
        } else {
            DateTimeFormatter.ofPattern("L/dd/yy")
        }
    }

    private fun toastShort(view: View, str: String) {
        Toast.makeText(view.context, str, Toast.LENGTH_SHORT).show()
    }

}