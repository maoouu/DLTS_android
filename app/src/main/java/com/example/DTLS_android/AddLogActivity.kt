package com.example.DTLS_android

import android.app.Activity
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

        btnSave.isEnabled = false

        authorField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                val author: String = authorField.text.toString().trim {it <= ' '}
                val description: String = descriptionField.text.toString().trim {it <= ' '}
                btnSave.isEnabled = author.isNotEmpty() && description.isNotEmpty()
            }
        })

        descriptionField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                val author: String = authorField.text.toString().trim {it <= ' '}
                val description: String = descriptionField.text.toString().trim {it <= ' '}
                btnSave.isEnabled = author.isNotEmpty() && description.isNotEmpty()
            }
        })

        btnSave.setOnClickListener {
            val author = authorField.text.toString().trim()
            val description = descriptionField.text.toString().trim()

            val data = Intent()
            data.putExtra("AUTHOR", author)
            data.putExtra("DESC", description)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}