package com.example.DTLS_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_log.view.*

// TODO: pass another argument to 'listener'
class LogAdapter(private val logList: List<Log>) :
    RecyclerView.Adapter<LogAdapter.LogViewHandler>() {

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

    class LogViewHandler(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(log: Log) = with(itemView) {
            cardTextTitle.text = log.author
            cardTextDescription.text = log.description

            fun popupMenu(view: View) {
                val options = PopupMenu(itemView.context, view)
                options.inflate(R.menu.card_menu)
                options.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.editCard -> {
                            //TODO: edit prompt
                            toastShort(itemView,"Edit Button is clicked")
                            true
                        }
                        R.id.deleteCard -> {
                            //TODO: delete prompt
                            toastShort(itemView,"Delete Button is clicked")
                            true
                        }
                        else -> true
                    }
                }
                options.show()

                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(options)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)
            }

            setOnClickListener {
                //TODO: view the card
                toastShort(itemView,"Item clicked!")
            }

            setOnLongClickListener {
                //TODO: pop-up menu for edit and delete
                popupMenu(it)

                return@setOnLongClickListener true
            }
        }

        private fun toastShort(view: View, str: String) {
            Toast.makeText(view.context, str, Toast.LENGTH_SHORT).show()
        }
    }

}