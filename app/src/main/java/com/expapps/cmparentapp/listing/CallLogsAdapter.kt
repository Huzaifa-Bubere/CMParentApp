package com.expapps.cmparentapp.listing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.expapps.cmparentapp.Utils
import com.expapps.cmparentapp.databinding.LayoutDataListItemBinding
import com.expapps.cmparentapp.models.CallLogEntry
import com.expapps.cmparentapp.models.Contact
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CallLogsAdapter : RecyclerView.Adapter<CallLogsAdapter.CallLogsViewHolder>() {

    private var callLogEntry: ArrayList<CallLogEntry> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogsViewHolder {
        val view =
            LayoutDataListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallLogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return callLogEntry.size
    }

    override fun onBindViewHolder(holder: CallLogsViewHolder, position: Int) {
        holder.bindItems(callLogEntry[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(callLogEntry: ArrayList<CallLogEntry>) {
        this.callLogEntry.clear()
        this.callLogEntry.addAll(callLogEntry)
        notifyDataSetChanged()
    }

    inner class CallLogsViewHolder(val binding: LayoutDataListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: CallLogEntry) {
            val timestamp = item.callDate ?: 0L
            val date = Date(timestamp)
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = sdf.format(date)

            binding.detailsTv.text =
                        "Phone Number = ${item.phoneNumber}\n" +
                        "Call Date = ${formattedDate}\n" +
                        "Call Duration = ${Utils.convertSecondsToString(item.callDuration ?: 0L)}\n" +
                        "Call Type = ${item.callType}"
        }
    }
}