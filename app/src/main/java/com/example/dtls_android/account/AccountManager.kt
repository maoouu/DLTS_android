package com.example.dtls_android.account

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.mindrot.jbcrypt.BCrypt

@Parcelize
class AccountManager: Parcelable {

    @IgnoredOnParcel
    private var accounts: HashMap<String, String> = HashMap()

    companion object {
        const val ADMIN_USER = "admin"
        const val ADMIN_PASS = "password"
    }

    init {
        accounts[ADMIN_USER] = hash(ADMIN_PASS)
    }

    fun addAccount(username: String, password: String) {
        accounts[username] = hash(password)
    }

    fun getAccountDetails(username: String): String? {
        return if (exists(username)) accounts[username] else null
    }

    fun verify(username: String, password: String): Boolean {
        if (!exists(username)) { return false }
        return BCrypt.checkpw(password, accounts[username])
    }

    fun exists(username: String): Boolean {
        return accounts.containsKey(username)
    }

    fun clear() {
        accounts.clear()
    }

    private fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}