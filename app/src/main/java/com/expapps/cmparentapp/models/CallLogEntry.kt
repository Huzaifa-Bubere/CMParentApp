package com.expapps.cmparentapp.models

data class CallLogEntry(
    val callId: String? = "",
    val phoneNumber: String? = "",
    val callType: Int? = 0,
    val callDate: Long? = 0L,
    val callDuration: Long? = 0L
)
