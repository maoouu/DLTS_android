package com.example.DTLS_android

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : AppCompatActivity() {

    var id: Long = 0
    private val logList = ArrayList<Log>()
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab)

        val log1 = Log(id++,"CSMA", "To be approved by the President.", Date())
        val log2 = Log(id++,"ECE", "Pending.", Date())
        val log3 = Log(id++,"BED", "Approved", Date())
        val log4 = Log(id++,"CAFA", "Approved", Date())

        val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val newLog = Log(
                    id++,
                    result.data?.getStringExtra("AUTHOR").toString(),
                    result.data?.getStringExtra("DESC").toString(),
                    Date()
                )
                logList.add(newLog)
                recyclerView.adapter?.notifyDataSetChanged()
                toast("A new entry has been added!")
            }
        }

        logList.add(log1)
        logList.add(log2)
        logList.add(log3)
        logList.add(log4)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LogAdapter(logList)

        fab.setOnClickListener {
            val intent = Intent(this, AddLogActivity::class.java)
            resultContract.launch(intent)
        }

        // If there are no entries
        textNoTask.visibility = if (logList.size > 0) View.GONE else View.VISIBLE
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}