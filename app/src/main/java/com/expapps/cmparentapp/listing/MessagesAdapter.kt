package com.expapps.cmparentapp.listing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.expapps.cmparentapp.databinding.LayoutDataListItemBinding
import com.expapps.cmparentapp.models.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    private var messages: ArrayList<Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val view = LayoutDataListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bindItems(messages[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(messages: ArrayList<Message>) {
        this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }
    inner class MessagesViewHolder(private val binding: LayoutDataListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(message: Message) {
            val timestamp = message.timestamp ?: 0L
            val date = Date(timestamp)
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = sdf.format(date)

            binding.detailsTv.text =
                "Timestamp = ${formattedDate}\n" +
                "Sender = ${message.sender}\n" +
                "Body = ${message.body}"
        }
    }

}