package com.expapps.cmparentapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.expapps.cmparentapp.models.CallLogEntry
import com.expapps.cmparentapp.models.Contact
import com.expapps.cmparentapp.models.Locations
import com.expapps.cmparentapp.models.Message
import com.expapps.cmparentapp.models.RegisteredTokens
import com.expapps.cmparentapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class FirebaseSource {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference = firebaseDatabase.reference

    fun getUserList(): LiveData<ArrayList<User>?> {
        val users = MutableLiveData<ArrayList<User>?>()
        databaseReference.child("Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                val userArray = ArrayList<User>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val user = it.getValue(User::class.java)
                        user?.let { u ->
                            userArray.add(u)
                        }
                    }
                    users.value = userArray
                }

                override fun onCancelled(error: DatabaseError) {
                    users.value = null
                }
            })
        return users
    }

    fun addUser(user: User): LiveData<Boolean?> {
        val isSuccess = MutableLiveData<Boolean>()
        databaseReference.child("Users")
            .child(user.userId ?: "")
            .setValue(user)
            .addOnSuccessListener {
                addMCode(user.email ?: "", Utils.getMCodeFromUserId(user.userId ?: ""))
                isSuccess.value = true
            }
            .addOnFailureListener {
                isSuccess.value = false
            }
        return isSuccess
    }

    fun addMCode(email: String, code: String) {
        databaseReference.child("UsersMCode")
            .updateChildren(
                hashMapOf<String, Any>(
                    Pair(
                        Utils.getEmailFromEmailId(email), code
                    )
                )
            )
            .addOnSuccessListener {}
            .addOnFailureListener {}
    }

    fun getMCodeByEmail(email: String): LiveData<Pair<String?, String?>> {
        val isSuccess = MutableLiveData<Pair<String?, String?>>()
        val emailStr = Utils.getEmailFromEmailId(email)
        databaseReference.child("UsersMCode/$emailStr")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val child = snapshot.value.toString()
                        isSuccess.value = Pair(Utils.getMCodeFromUserId(child), child)
                    } else {
                        isSuccess.value = null
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    isSuccess.value = null
                }
            })
        return isSuccess
    }

    fun addContacts(contact: List<Contact>) {
        databaseReference.child("Users/${firebaseAuth.currentUser?.uid}/data/contacts")
            .setValue(contact)
    }

    fun addCallLogs(callLogEntry: List<CallLogEntry>) {
        databaseReference.child("Users/${firebaseAuth.currentUser?.uid}/data/callLogs")
            .setValue(callLogEntry)
    }
    fun addMessages(messages: List<Message>) {
        databaseReference.child("Users/${firebaseAuth.currentUser?.uid}/data/messages")
            .setValue(messages)
    }
    fun addCurrentLocation(locations: Locations) {
        databaseReference.child("Users/${firebaseAuth.currentUser?.uid}/data/locations")
            .setValue(locations)
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getAllContacts(uid: String): LiveData<ArrayList<Contact>?> {
        val _contacts: MutableLiveData<ArrayList<Contact>?> = MutableLiveData()
        databaseReference.child("Users/$uid/data/contacts")
            .limitToFirst(150)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contacts = ArrayList<Contact>()
                    snapshot.children.forEach {
                        if (it != null) {
                            Log.d("TAG", "onDataChange: ${it.value}")
                            if (it.value != null) {
                                val contact = it.getValue(Contact::class.java)
                                if (contact != null) {
                                    contacts.add(contact)
                                }
                            }
                        }
                    }
                    _contacts.value = contacts
                }

                override fun onCancelled(error: DatabaseError) {
                    _contacts.value = null
                }
            })
        return _contacts
    }
    fun getAllMessages(uid: String): LiveData<ArrayList<Message>?> {
        val _messages: MutableLiveData<ArrayList<Message>?> = MutableLiveData()
        databaseReference.child("Users/$uid/data/messages")
            .limitToFirst(150)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = ArrayList<Message>()
                    snapshot.children.forEach {
                        if (it != null) {
                            Log.d("TAG", "onDataChange: ${it.value}")
                            if (it.value != null) {
                                val message = it.getValue(Message::class.java)
                                if (message != null) {
                                    messages.add(message)
                                }
                            }
                        }
                    }
                    _messages.value = messages
                }

                override fun onCancelled(error: DatabaseError) {
                    _messages.value = null
                }
            })
        return _messages
    }
    fun getAllCallLogs(uid: String): LiveData<ArrayList<CallLogEntry>?> {
        val _callLogs: MutableLiveData<ArrayList<CallLogEntry>?> = MutableLiveData()
        databaseReference.child("Users/$uid/data/callLogs")
            .limitToFirst(150)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val callLogs = ArrayList<CallLogEntry>()
                    snapshot.children.forEach {
                        if (it != null) {
                            Log.d("TAG", "onDataChange: ${it.value}")
                            if (it.value != null) {
                                val message = it.getValue(CallLogEntry::class.java)
                                if (message != null) {
                                    callLogs.add(message)
                                }
                            }
                        }
                    }
                    _callLogs.value = callLogs
                }

                override fun onCancelled(error: DatabaseError) {
                    _callLogs.value = null
                }
            })
        return _callLogs
    }

    fun getLocation(uid: String): LiveData<Locations?> {
        val _location = MutableLiveData<Locations?>()
            databaseReference.child("Users/$uid/data/locations")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val location = snapshot.getValue(Locations::class.java)
                        location?.let {
                            _location.value = it
                        }
                    } else {
                        _location.value = null
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _location.value = null
                }
            })
        return _location
    }
    fun registerNotificationUser(registeredTokens: RegisteredTokens) {
        databaseReference.child("RegisteredParents/${registeredTokens.uid}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val registeredParents = ArrayList<RegisteredTokens>()
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            val parent = it.getValue(RegisteredTokens::class.java)
                            if (parent != null) {
                                registeredParents.add(parent)
                            }
                        }
                    }
                    if (!registeredParents.contains(registeredTokens)) {
                        val key = databaseReference.push().key
                        databaseReference.child("RegisteredParents/${registeredTokens.uid}/$key")
                            .setValue(registeredTokens)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }
}