package com.example.dtls_android

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.collections.ArrayList
import android.content.Intent
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
import com.example.dtls_android.databinding.ActivityDashboardBinding
import com.example.dtls_android.session.LoginPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.time.LocalDateTime
import java.util.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var id = 0

    private lateinit var binding: ActivityDashboardBinding

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

    private lateinit var mheaderView: View
    private lateinit var mheaderUsername: TextView
    private lateinit var mheaderDesc: TextView

    lateinit var session: LoginPref

    private val sampleAuthor: Array<String> = arrayOf(
        "Corey",
        "Alvin",
        "Jake",
        "Bob",
        "John Doe",
    )

    private val sampleStatus: Array<String> = arrayOf(
        "Approved",
        "Pending",
        "Acknowledged",
        "Denied",
        "Recommended to the President",
    )

    private val sampleDate: Array<LocalDateTime> = arrayOf(
        LocalDateTime.of(2020, 7, 15, 10, 30),
        LocalDateTime.of(2020, 8, 1, 12, 25),
        LocalDateTime.of(2020, 11, 23, 16, 6),
        LocalDateTime.of(2020, 12, 30, 13, 1),
        LocalDateTime.of(2020, 10, 30, 12, 5)
    )

    private val sampleDesc: Array<String> = arrayOf(
        "Awaiting Request",
        "Info on Project",
        "Hello World!",
        "Travel Budget",
        "Request to Promote",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()

        session = LoginPref(this)
        session.checkLogin()

        addButton = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)
        mRecyclerView = findViewById(R.id.recyclerView)
        mNavigationView = findViewById(R.id.nav_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)

        mheaderView = mNavigationView.getHeaderView(0)
        mheaderUsername = mheaderView.findViewById(R.id.nav_header_username)
        mheaderDesc = mheaderView.findViewById(R.id.nav_header_desc)

        mheaderUsername.text = session.getUserDetails()
        mheaderDesc.text = "Hello World!~"

        val toggle = ActionBarDrawerToggle(
            this, mDrawerLayout, binding.toolbar, 0, 0
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
            if (result?.resultCode == RESULT_OK) {
                val newLog = Log(
                    id++,
                    result.data?.getStringExtra("AUTHOR").toString(),
                    result.data?.getStringExtra("STATUS").toString(),
                    LocalDateTime.now(),
                    result.data?.getStringExtra("DESC").toString(),
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
                        val itemStatus = it.status.toLowerCase(Locale.getDefault())

                        val queryFound: Boolean = itemAuthor.contains(searchText) ||
                                itemDesc.contains(searchText) || itemStatus.contains(searchText)

                        if (queryFound) { tempLogList.add(it) }
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
                finish()
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
            val sampleData = Log(
                id++,
                sampleAuthor[item],
                sampleStatus[item],
                sampleDate[item],
                sampleDesc[item]
            )
            newLogList.add(sampleData)
        }

        tempLogList.addAll(newLogList)
        adapter = LogAdapter(tempLogList, newLogList)
    }

    private fun checkIsEmpty(logList: ArrayList<Log>) {
        textNoTask.visibility = if (logList.size > 0) View.GONE else View.VISIBLE
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}