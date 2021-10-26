package com.example.dtls_android.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.dtls_android.ui.LoginActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.ChronoLocalDate
import java.util.*

class AccountPref {
    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor
    var context: Context
    val PRIVATEMODE: Int = 0

    companion object {
        const val SHARED_PREF = "Account_Preferences"
        const val EXPIRY_DATE = "Expiry_Date"
        const val IS_LOGGED_IN = "Is_Logged_In"
        const val USER_TOKEN = "User_Token"
    }

    constructor(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, PRIVATEMODE)
        editor = sharedPreferences.edit()
    }

    fun saveTokenData(expiry: String, token: String) {
        editor.putString(EXPIRY_DATE, expiry)
        editor.putString(USER_TOKEN, token)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun tokenExpired(): Boolean {
        val tokenDateString = sharedPreferences.getString(EXPIRY_DATE, null)
        val tokenDate = Instant.parse(tokenDateString).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val now = LocalDateTime.now()

        return tokenDate.isAfter(now)
    }
}