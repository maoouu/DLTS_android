package com.example.DTLS_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_log.view.*

class LogAdapter(val logList: List<Log>, val listener: (Log) -> Unit) :
    RecyclerView.Adapter<LogAdapter.LogViewHandler>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHandler {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHandler(rootView)
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    override fun onBindViewHolder(holder: LogViewHandler, position: Int) {
        holder.bind(logList[position], listener)
    }

    class LogViewHandler(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(log: Log, listener: (Log) -> Unit) = with(itemView) {
            cardTextTitle.text = log.title
            cardTextDescription.text = log.description
            setOnClickListener {listener(log)}
        }
    }

}