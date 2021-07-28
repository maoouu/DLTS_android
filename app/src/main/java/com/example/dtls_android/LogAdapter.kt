package com.example.dtls_android

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.item_log.view.*

class LogAdapter(private val logList: ArrayList<Log>) :
    RecyclerView.Adapter<LogAdapter.LogViewHandler>() {

    private lateinit var authorField: TextInputEditText
    private lateinit var descField: TextInputEditText
    private lateinit var authorTitle: TextView
    private lateinit var description: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHandler {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHandler(rootView)
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    override fun onBindViewHolder(holder: LogViewHandler, position: Int) {
        holder.bind(logList[position])
    }

    inner class LogViewHandler(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(log: Log) = with(itemView) {
            cardTextTitle.text = log.author
            cardTextDescription.text = log.description

            setOnClickListener {
                //TODO: view the card
                toastShort(itemView,"Item clicked!")
                viewCard(itemView, logList[adapterPosition])
            }

            setOnLongClickListener {
                //TODO: pop-up menu for edit and delete
                popupMenu(adapterPosition, itemView, it)
                return@setOnLongClickListener true
            }
        }
    }

    private fun popupMenu(adapterPosition: Int, itemView: View, view: View) {
        val position = logList[adapterPosition]
        val options = PopupMenu(itemView.context, view)
        options.inflate(R.menu.card_menu)
        options.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.editCard -> {
                    //TODO: edit prompt
                    toastShort(itemView,"Edit Button is clicked")
                    editCard(itemView, position)
                    true
                }
                R.id.deleteCard -> {
                    //TODO: delete prompt
                    toastShort(itemView,"Delete Button is clicked")
                    deleteCard(itemView, logList, adapterPosition)
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
        authorTitle = card.findViewById(R.id.viewCardAuthor)
        description = card.findViewById(R.id.viewCardDesc)

        authorTitle.text = position.author
        description.text = position.description

        val infoDialogBuilder = AlertDialog.Builder(card.context)
        infoDialogBuilder.setView(card)

        val infoDialog = infoDialogBuilder.create()
        infoDialog.window?.setBackgroundDrawable(ColorDrawable(ContextCompat
            .getColor(card.context, R.color.light_grey)))
        infoDialog.setContentView(card)
        infoDialog.show()
    }

    private fun editCard(view: View, position: Log) {
        val editView = LayoutInflater.from(view.context).inflate(R.layout.activity_edit_log, null)
        authorField = editView.findViewById(R.id.authorInput)
        descField = editView.findViewById(R.id.descriptionInput)

        authorField.setText(position.author)
        descField.setText(position.description)

        val infoDialogBuilder = AlertDialog.Builder(editView.context)
        infoDialogBuilder.setView(editView)
            .setPositiveButton("Save") {
                dialog,_->
                position.author = authorField.text.toString()
                position.description = descField.text.toString()
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
                val isModified: Boolean = !(authorField.text.toString() == position.author &&
                        descField.text.toString() == position.description)

                infoDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isModified
            }
        })

        descField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                val isModified: Boolean = !(authorField.text.toString() == position.author &&
                        descField.text.toString() == position.description)

                infoDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isModified
            }
        })
    }

    private fun deleteCard(view: View, logList: ArrayList<Log>, adapterPosition: Int) {
        AlertDialog.Builder(view.context)
            .setTitle("Delete")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Are you sure you want to permanently delete this item?")
            .setPositiveButton("Yes") {
                    dialog,_->
                logList.removeAt(adapterPosition)
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

    private fun toastShort(view: View, str: String) {
        Toast.makeText(view.context, str, Toast.LENGTH_SHORT).show()
    }

}