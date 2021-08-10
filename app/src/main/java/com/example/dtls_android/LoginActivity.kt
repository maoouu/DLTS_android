package com.example.dtls_android

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dtls_android.account.AccountManager
import com.example.dtls_android.session.LoginPref
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var signupContract: ActivityResultLauncher<Intent>
    private lateinit var session: LoginPref

    lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        accountManager = AccountManager()
        session = LoginPref(this)
        // check if user is already logged in
        if (session.isLoggedIn()) {
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        usernameField = findViewById(R.id.usernameLoginField)
        passwordField = findViewById(R.id.passwordLoginField)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)

        signupContract = registerForActivityResult(ActivityResultContracts
            .StartActivityForResult()) { addRegisteredData(it) }

        btnLogin.setOnClickListener{ login() }
        btnSignup.setOnClickListener{ register() }
    }

    override fun onBackPressed() {
        exit()
    }

    private fun login() {
        val usernameInput = usernameField.text.toString().trim()
        val passwordInput = passwordField.text.toString().trim()

        if (usernameInput.isBlank() || passwordInput.isBlank()) {
            usernameField.setText("")
            passwordField.setText("")
            Toast.makeText(this, "Please enter your username and password.", Toast.LENGTH_LONG).show()
        } else if (accountManager.verify(usernameInput, passwordInput)) {
            session.createLoginSession(usernameInput)
            // Start Activity
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        } else {
            usernameField.setText("")
            passwordField.setText("")
            Toast.makeText(this, "Invalid login, please try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        // TODO: Pass AccountManager to RegisterActivity
        intent.putExtra("ACCOUNTMANAGER", accountManager)
        signupContract.launch(intent)
    }

    private fun addRegisteredData(result: ActivityResult?) {
        if (result?.resultCode == Activity.RESULT_OK) {
            val username = result.data?.getStringExtra("USERNAME")!!
            val password = result.data?.getStringExtra("PASSWORD")!!
            accountManager.addAccount(username, password)
            Toast.makeText(this, "$username has been added ($password)", Toast.LENGTH_LONG).show()
        }
    }

    private fun exit() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setIcon(R.drawable.ic_signout)
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") {
                dialog,_->
                this@LoginActivity.finish()
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}