package com.example.djangoproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LogAdapter(val logList: List<Log>, valListener: (Task) -> Unit) :
    RecyclerView.Adapter<LogAdapter.LogViewHandler>() {

    override fun onBindViewHolder(holder: LogViewHandler, position: Int) {
        holder.bind(logList[position], listener)
    }

    override fun getItemCount() = logList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHandler {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHandler(rootView)
    }

    class LogViewHandler(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(log: Log, listener: (Log) -> Unit) = with(itemView) {
            cardTextTitle.text = log.title
            cardTextDescription.text = log.description
            setOnClickListener {listener(log)}
        }
    }

}