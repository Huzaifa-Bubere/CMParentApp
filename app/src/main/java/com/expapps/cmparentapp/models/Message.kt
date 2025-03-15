package com.expapps.cmparentapp.models

// Message.kt
data class Message(
    val messageId: String? = "",
    val sender: String? = "",
    val body: String? = "",
    val timestamp: Long? = 0L
)
