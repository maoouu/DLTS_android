package com.example.dtls_android.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.dtls_android.ui.LoginActivity

class LoginPref {

    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor
    var context: Context

    val PRIVATEMODE: Int = 0

    constructor(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, PRIVATEMODE)
        editor = sharedPreferences.edit()
    }

    companion object {
        const val SHARED_PREF = "Login_Preferences"
        const val IS_LOGGED_IN = "isLoggedIn"
        const val USERNAME_KEY = "username_key"
    }

    fun createLoginSession(username: String) {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putString(USERNAME_KEY, username)
        editor.apply()
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun getUserDetails(): String? {
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

}