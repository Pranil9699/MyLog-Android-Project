package com.pranil9699.mylog

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class LogAdapter(
    private val context: Context,
    private val logList: List<LogItem>,
    private val onFavoriteToggle: (LogItem) -> Unit // callback to update Firebase
) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logText: TextView = itemView.findViewById(R.id.textLog)
        val emojiText: TextView = itemView.findViewById(R.id.textEmoji)
        val dateTimeText: TextView = itemView.findViewById(R.id.textDateTime)
        val cardView: View = itemView

        private var lastClickTime = 0L
        init {
            cardView.setOnClickListener {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < 300) {
                    val logItem = logList[adapterPosition]
                    onFavoriteToggle(logItem)
                } else {
                    lastClickTime = clickTime
                }
            }
            // Long press to open EditLogActivity
            cardView.setOnLongClickListener {
                val logItem = logList[adapterPosition]
                val intent = Intent(context, EditLogActivity::class.java).apply {
                    putExtra("logId", logItem.id)
                    putExtra("logText", logItem.text)
                    putExtra("emoji", logItem.emoji)
                    putExtra("timestamp", logItem.timestamp)
                    putExtra("favorite", logItem.favorite)
                }
                context.startActivity(intent)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val logItem = logList[position]
        holder.logText.text = logItem.text
        holder.emojiText.text = logItem.emoji
        holder.dateTimeText.text = "Date: ${formatDateTime(logItem.timestamp)}"
    }

    private fun formatDateTime(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMMM, hh:mm a", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    override fun getItemCount(): Int = logList.size
}
