package com.expapps.cmparentapp

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object Utils {
    fun checkEmptyOrNullString(vararg strings: String?): Boolean {
        if (strings.contains(null) || strings.contains("null") || strings.contains("")) {
            return false
        }
        return true
    }

    fun checkPasswordLength(str: String, length: Int = 6): Boolean {
        if (str.length > length) {
            return true
        }
        return false
    }

    fun isAllStringsEqual(vararg strings: String?): Boolean {
        var str1 = ""
        if (strings.isNotEmpty()) {
            str1 = strings[0] ?: ""
        }
        strings.forEach {
            if (str1 != it) {
                return false
            }
        }
        return true
    }

    fun getMCodeFromUserId(userId: String): String {
        if (checkEmptyOrNullString(userId)) {
            val uidLen = userId.length
            if (uidLen > 6) {
                return userId.substring(uidLen - 6, uidLen)
            }
        }
        return ""
    }

    fun getEmailFromEmailId(str: String): String {
        val emailId = str.split("@")
        if (emailId.isNotEmpty()) {
            return emailId[0]
        }
        return ""
    }

    fun Context?.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }

    fun FragmentActivity?.openActivity(clazz: Class<*>, finishPrev: Boolean = false) {
        val intent = Intent(this, clazz)
        if (finishPrev) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        this?.startActivity(intent)
        if (finishPrev) {
            this?.finish()
        }
    }


    fun RecyclerView.setVerticalLayoutManager(context: Context, adapter: RecyclerView.Adapter<*>, reverseLayout: Boolean = false, stackFromEnd: Boolean = false) {
        val ll = LinearLayoutManager(context, RecyclerView.VERTICAL, reverseLayout)
        ll.stackFromEnd = stackFromEnd
        this.layoutManager = ll
        this.adapter = adapter
    }

    fun RecyclerView.setVerticalLayoutManager(context: Context) {
        this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    fun RecyclerView.setLayoutManager(context: Context, orientation: Int) {
        this.layoutManager = LinearLayoutManager(context, orientation, false)
    }

    fun RecyclerView.setLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) {
        this.layoutManager = LinearLayoutManager(context, orientation, reverseLayout)
    }

    fun convertSecondsToString(seconds: Long): String {
        if (seconds >= 3600) {
            val hours = seconds / 3600
            val remainingSeconds = seconds % 3600
            val minutes = remainingSeconds / 60
            val remainingSecs = remainingSeconds % 60
            return String.format("%02d:%02d:%02d", hours, minutes, remainingSecs)
        } else if (seconds >= 60) {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("%02d:%02d", minutes, remainingSeconds)
        } else {
            return String.format("%02ds", seconds)
        }
    }
}