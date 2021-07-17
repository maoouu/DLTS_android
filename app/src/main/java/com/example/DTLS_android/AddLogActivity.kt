package com.example.DTLS_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class AddLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_log)

        val authorField: TextInputEditText = findViewById(R.id.authorInput)
        val descriptionField: TextInputEditText = findViewById(R.id.descriptionInput)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        authorField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (authorField.toString().trim { it <= ' ' }.isEmpty()) {
                    markButtonDisable(btnSave)
                }
            }
        })

        btnSave.setOnClickListener {
            val author = authorField.text.toString().replace("\\s".toRegex(), "")
            val description = descriptionField.text.toString().replace("\\s".toRegex(), "")
            val newLogEntry = Log(author, description, Date())
            //TODO: find out how to return this as a result
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun markButtonDisable(button: Button) {
        button?.isEnabled = false
        button?.setTextColor(ContextCompat.getColor(textView.context, R.color.white))
        button?.setBackgroundColor(ContextCompat.getColor(textView.context, R.color.light_red))
    }
}