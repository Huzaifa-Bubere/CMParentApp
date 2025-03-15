package com.expapps.cmparentapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.persistableBundleOf
import com.expapps.cmparentapp.Utils.openActivity
import com.expapps.cmparentapp.auth.LoginActivity
import com.expapps.cmparentapp.databinding.ActivityMainBinding
import com.expapps.cmparentapp.listing.DataListingActivity
import com.expapps.cmparentapp.listing.LocationActivity
import com.expapps.cmparentapp.models.RegisteredTokens
import com.expapps.cmparentapp.sharedprefs.KSharedPreference
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import com.onesignal.notifications.IPermissionObserver

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseSource: FirebaseSource
    private lateinit var firebaseAuth: FirebaseAuth
    private var mCode = ""
    private var notificationPermissionGranted = false
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        OneSignal.Notifications.addPermissionObserver(object : IPermissionObserver {
            override fun onNotificationPermissionChange(permission: Boolean) {
                notificationPermissionGranted = permission
                val id = OneSignal.User.pushSubscription.id
                val pushToken = OneSignal.User.pushSubscription.token
                if (isLoggedIn) {
                    setRegisteredToken(id, pushToken)
                }
            }
        })
        init()
        setCardsClickListener()
    }

    private fun setRegisteredToken(id: String, pushToken: String) {
        val userId = KSharedPreference.getString(this, Constants.USER_ID)
        val registeredTokens = RegisteredTokens(userId, id, pushToken)
        firebaseSource.registerNotificationUser(registeredTokens)
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInStatus()
    }

    private fun init() {
        firebaseSource = FirebaseSource()
        firebaseAuth = FirebaseAuth.getInstance()
    }


    private fun fetchData() {
    }

    private fun checkLoggedInStatus() {
        if(KSharedPreference.getString(this, Constants.USER_ID).isBlank()) {
            openActivity(LoginActivity::class.java, finishPrev = true)
        } else {
            isLoggedIn = true
            if (OneSignal.User.pushSubscription.optedIn) {
                val id = OneSignal.User.pushSubscription.id
                val pushToken = OneSignal.User.pushSubscription.token
                setRegisteredToken(id, pushToken)
            }
            fetchData()
        }
    }

    private fun setCardsClickListener() {
        binding.cardLayout.contactsCard.setOnClickListener {
            val intents = Intent(this, DataListingActivity::class.java)
            intents.putExtra(Constants.TO_OPEN, "contacts")
            startActivity(intents)
        }
        binding.cardLayout.callLogsCard.setOnClickListener {
            val intents = Intent(this, DataListingActivity::class.java)
            intents.putExtra(Constants.TO_OPEN, "callLogs")
            startActivity(intents)
        }
        binding.cardLayout.messageCard.setOnClickListener {
            val intents = Intent(this, DataListingActivity::class.java)
            intents.putExtra(Constants.TO_OPEN, "messages")
            startActivity(intents)
        }
        binding.cardLayout.locationCard.setOnClickListener {
            val intents = Intent(this, LocationActivity::class.java)
            intents.putExtra(Constants.TO_OPEN, "location")
            startActivity(intents)
        }
    }
}