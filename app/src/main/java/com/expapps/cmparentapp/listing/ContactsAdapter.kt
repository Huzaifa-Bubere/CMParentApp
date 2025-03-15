package com.expapps.cmparentapp.listing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.expapps.cmparentapp.databinding.LayoutDataListItemBinding
import com.expapps.cmparentapp.models.Contact

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private var contacts: ArrayList<Contact> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutDataListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindItems(contacts[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(contacts: ArrayList<Contact>) {
        this.contacts.clear()
        this.contacts.addAll(contacts)
        notifyDataSetChanged()
    }

    inner class ContactsViewHolder(val binding: LayoutDataListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(contact: Contact) {
            binding.detailsTv.text =
                    "Name = ${contact.name}\n" +
                    "Phone numbers = ${contact.phoneNumbers}"
        }
    }
}