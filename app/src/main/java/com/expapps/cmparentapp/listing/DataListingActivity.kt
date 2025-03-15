package com.expapps.cmparentapp.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.expapps.cmparentapp.Constants
import com.expapps.cmparentapp.FirebaseSource
import com.expapps.cmparentapp.R
import com.expapps.cmparentapp.Utils.setVerticalLayoutManager
import com.expapps.cmparentapp.databinding.ActivityDataListingBinding
import com.expapps.cmparentapp.sharedprefs.KSharedPreference

class DataListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataListingBinding
    private var contactsAdapter: ContactsAdapter? = null
    private var messagesAdapter: MessagesAdapter? = null
    private var callLogsAdapter: CallLogsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        val intent = intent
        val toOpen = intent.getStringExtra(Constants.TO_OPEN)

        when (toOpen) {
            "contacts" -> {
                binding.recyclerView.adapter = contactsAdapter
                fetchContactsData()
                setTitle("Contacts")
            }
            "messages" -> {
                binding.recyclerView.adapter = messagesAdapter
                fetchMessagesData()
                setTitle("Messages")
            }
            "callLogs" -> {
                binding.recyclerView.adapter = callLogsAdapter
                fetchCallLogsData()
                setTitle("Call Logs")
            }
        }
    }

    private fun setTitle(title: String) {
        binding.titleTv.text = title
    }

    private fun init() {
        initAdapter()
    }

    private fun initAdapter() {
        contactsAdapter = ContactsAdapter()
        messagesAdapter = MessagesAdapter()
        callLogsAdapter = CallLogsAdapter()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.setVerticalLayoutManager(this)
    }

    private fun fetchContactsData() {
        FirebaseSource().getAllContacts(KSharedPreference.getCurrentUserId(this)).observe(this) {
            if (it != null) {
                contactsAdapter?.setData(it)
                hideProgressBar()
                showRecyclerView()
            }
        }
    }

    private fun fetchMessagesData() {
        FirebaseSource().getAllMessages(KSharedPreference.getCurrentUserId(this)).observe(this) {
            if (it != null) {
                messagesAdapter?.setData(it)
                hideProgressBar()
                showRecyclerView()
            }
        }
    }
    private fun fetchCallLogsData() {
        FirebaseSource().getAllCallLogs(KSharedPreference.getCurrentUserId(this)).observe(this) {
            if (it != null) {
                callLogsAdapter?.setData(it)
                hideProgressBar()
                showRecyclerView()
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
    private fun showRecyclerView() {
        binding.recyclerView.visibility = View.VISIBLE
    }
}