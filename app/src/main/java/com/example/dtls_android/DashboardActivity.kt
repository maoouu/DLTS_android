package com.example.dtls_android

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime

class DashboardActivity : AppCompatActivity() {

    var id: Long = 0
    private val logList = ArrayList<Log>()
    private lateinit var fab: FloatingActionButton
    private lateinit var textNoTask: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)

        val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val newLog = Log(
                    id++,
                    result.data?.getStringExtra("AUTHOR").toString(),
                    result.data?.getStringExtra("DESC").toString(),
                    LocalDateTime.now()
                )
                logList.add(newLog)
                recyclerView.adapter?.notifyDataSetChanged()
                toast("A new entry has been added!")
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        logList.reverse()
        //TODO: reverse the layout, recent ones go on top
        recyclerView.adapter = LogAdapter(logList)
        val adapter = recyclerView.adapter

        adapter?.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty(logList)
            }
        })

        fab.setOnClickListener {
            val intent = Intent(this, AddLogActivity::class.java)
            resultContract.launch(intent)
        }


    }

    private fun checkIsEmpty(logList: ArrayList<Log>) {
        textNoTask.visibility = if (logList.size > 0) View.GONE else View.VISIBLE
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}