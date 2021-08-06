package com.example.dtls_android

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.session.LoginPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.time.LocalDateTime
import java.util.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var id = 0

    private lateinit var newLogList: ArrayList<Log>
    private lateinit var tempLogList: ArrayList<Log>

    private lateinit var adapter: LogAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mNavigationView: NavigationView
    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var addButton: FloatingActionButton
    private lateinit var textNoTask: TextView
    private lateinit var resultContract: ActivityResultLauncher<Intent>

    lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        session = LoginPref(this)
        session.checkLogin()

        addButton = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)
        mRecyclerView = findViewById(R.id.recyclerView)
        mNavigationView = findViewById(R.id.nav_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar, 0, 0
        )
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mNavigationView.setNavigationItemSelectedListener(this)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true

        newLogList = arrayListOf()
        tempLogList = arrayListOf()
        getSampleData()

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter


        resultContract = registerForActivityResult(ActivityResultContracts
            .StartActivityForResult()) { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val newLog = Log(
                    id++,
                    result.data?.getStringExtra("AUTHOR").toString(),
                    result.data?.getStringExtra("DESC").toString(),
                    LocalDateTime.now()
                )
                newLogList.add(newLog)
                tempLogList.add(newLog)
                mRecyclerView.adapter?.notifyDataSetChanged()
                Toast.makeText(this, "A new entry has been added", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty(tempLogList)
            }
        })

        addButton.setOnClickListener {
            val intent = Intent(this, AddLogActivity::class.java)
            resultContract.launch(intent)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView

        searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempLogList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault()).trim()

                if (searchText.isNotBlank()) {
                    newLogList.forEach {
                        val itemAuthor = it.author.toLowerCase(Locale.getDefault())
                        val itemDesc = it.description.toLowerCase(Locale.getDefault())

                        val queryFound: Boolean =
                            itemAuthor.contains(searchText) || itemDesc.contains(searchText)

                        if (queryFound) {
                            tempLogList.add(it)
                        }
                    }

                    mRecyclerView.adapter?.notifyDataSetChanged()
                } else {
                    tempLogList.addAll(newLogList)
                    mRecyclerView.adapter?.notifyDataSetChanged()
                }

                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_menu_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_menu_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_menu_logout -> {
                confirmLogout()
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setIcon(R.drawable.ic_signout)
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Yes") {
                dialog,_->
                session.logoutUser()
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                dialog,_->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    private fun getSampleData() {
        // Remove empty text upon generating sample data
        textNoTask.visibility = View.GONE

        for (item in sampleAuthor.indices) {
            val sampleData = Log(id++, sampleAuthor[item], sampleDesc[item], sampleDate[item])
            newLogList.add(sampleData)
        }

        tempLogList.addAll(newLogList)
        adapter = LogAdapter(tempLogList, newLogList)
    }


    private fun checkIsEmpty(logList: ArrayList<Log>) {
        textNoTask.visibility = if (logList.size > 0) View.GONE else View.VISIBLE
    }

    companion object {
        val sampleAuthor: Array<String> = arrayOf(
        "MSEUF",
        "AMA",
        "DLL",
        "FEU"
        )

        val sampleDesc: Array<String> = arrayOf(
        "Approved",
        "Pending",
        "Declined",
        "Acknowledged"
        )

        val sampleDate: Array<LocalDateTime> = arrayOf(
        LocalDateTime.of(2020, 7, 15, 10, 30),
        LocalDateTime.of(2020, 8, 1, 12, 25),
        LocalDateTime.of(2020, 11, 23, 16, 6),
        LocalDateTime.of(2020, 12, 30, 13, 1)
        )
    }
}