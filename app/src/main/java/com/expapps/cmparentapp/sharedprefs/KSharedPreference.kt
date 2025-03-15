package com.expapps.cmparentapp.sharedprefs

import android.content.Context
import com.expapps.cmparentapp.Constants
import com.expapps.cmparentapp.R

object KSharedPreference {

    fun setString(context: Context?, key: String, value: String) {
        val sharedPrefs = context?.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(context: Context?, key: String): String {
        val sharedPrefs = context?.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        return sharedPrefs?.getString(key, "") ?: ""
    }

    fun setBoolean(context: Context?, key: String, value: Boolean) {
        val sharedPrefs = context?.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBoolean(context: Context?, key: String, defaultValue: Boolean): Boolean {
        val sharedPrefs = context?.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        return sharedPrefs?.getBoolean(key, defaultValue) ?: false
    }

    fun getCurrentUserId(context: Context?): String {
        return getString(context, Constants.USER_ID)
    }

    fun setCurrentUserId(context: Context?, uid: String) {
        setString(context, Constants.USER_ID, uid)
    }
}