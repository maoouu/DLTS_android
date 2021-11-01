package com.example.dtls_android.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.dtls_android.R
import com.example.dtls_android.ViewModel.AddLogActivityViewModel
import com.example.dtls_android.service.response.Record
import com.example.dtls_android.session.AccountPref
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface AddLogWatcher: TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
}

class AddLogActivity : AppCompatActivity(), AddLogWatcher {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var authorField: TextInputEditText
    private lateinit var descField: TextInputEditText
    private lateinit var dropdownAdapter: ArrayAdapter<String>
    private lateinit var status: Array<String>
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var viewModel: AddLogActivityViewModel
    private lateinit var accountSession: AccountPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_log)
        accountSession = AccountPref(this)

        initActivity()
        initViewModel()
        addLogObservable()

        btnSave.setOnClickListener {  save() }
        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun initActivity() {
        authorField = findViewById(R.id.authorInput)
        descField = findViewById(R.id.descriptionInput)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        status = resources.getStringArray(R.array.status)
        dropdownAdapter = ArrayAdapter(this, R.layout.dropdown_item, status)
        autoCompleteTextView = findViewById(R.id.statusFieldAuto)
        autoCompleteTextView.setAdapter(dropdownAdapter)

        btnSave.isEnabled = false
        authorField.addTextChangedListener(this)
        descField.addTextChangedListener(this)
        autoCompleteTextView.addTextChangedListener(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AddLogActivityViewModel::class.java)
    }

    private fun addLogObservable() {
        viewModel.getAddLogObservable().observe(this, {
            if (it == null) {
                Toast.makeText(this@AddLogActivity, "Failed to create new record.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@AddLogActivity, DashboardActivity::class.java)
                Toast.makeText(this@AddLogActivity, "A new record has been created.", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        })
    }

    override fun afterTextChanged(s: Editable?) {
        val author: String = authorField.text.toString().trim()
        val description: String = descField.text.toString().trim()
        val status: String = autoCompleteTextView.editableText.toString()
        btnSave.isEnabled = author.isNotEmpty() && description.isNotEmpty() && status.isNotEmpty()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun save() {
        val author = authorField.text.toString().trim()
        val dateCreated = LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd"))
        val description = descField.text.toString().trim()
        val status = autoCompleteTextView.editableText.toString()

        val record = Record(null, author, dateCreated, dateCreated, description, status)
        viewModel.addNewRecord(record, accountSession.getToken())
    }
}