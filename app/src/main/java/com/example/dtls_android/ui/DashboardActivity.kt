package com.example.dtls_android.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.dtls_android.DataAdapter
import com.example.dtls_android.R
import com.example.dtls_android.ViewModel.DashboardActivityViewModel
import com.example.dtls_android.databinding.ActivityDashboardBinding
import com.example.dtls_android.service.response.Record
import com.example.dtls_android.session.AccountPref
import com.example.dtls_android.session.LoginPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.util.*

class DashboardActivity : AppCompatActivity(), DataAdapter.OnItemLongClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var recyclerViewAdapter: DataAdapter
    private lateinit var viewModel: DashboardActivityViewModel
    private lateinit var rvProgressBar: ProgressBar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mNavigationView: NavigationView
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var addButton: FloatingActionButton
    private lateinit var textNoTask: TextView
    private lateinit var searchView: SearchView
    private lateinit var mheaderView: View
    private lateinit var mheaderUsername: TextView
    private lateinit var mheaderDesc: TextView
    private lateinit var accountSession: AccountPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()
        accountSession = AccountPref(this)

        if (!accountSession.isLoggedIn()) {
            accountSession.logoutUser()
        }

        initMain()
        initNavigationView()
        initRecyclerView()
        initViewModel()
        deleteRecordObservable()
        logoutAccountObservable()

        addButton.setOnClickListener { redirectToAdd() }
    }

    private fun initMain() {
        addButton = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)
        mRecyclerView = findViewById(R.id.recyclerView)
        rvProgressBar = findViewById(R.id.rvProgressBar)
        textNoTask.visibility = View.GONE
        rvProgressBar.visibility = View.VISIBLE
    }

    private fun initNavigationView() {
        mNavigationView = findViewById(R.id.nav_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mheaderView = mNavigationView.getHeaderView(0)
        mheaderUsername = mheaderView.findViewById(R.id.nav_header_username)
        mheaderDesc = mheaderView.findViewById(R.id.nav_header_desc)
        mheaderUsername.text = accountSession.getUsername()
        mheaderDesc.text = "Hello World!~"

        val toggle = ActionBarDrawerToggle(this, mDrawerLayout, binding.toolbar, 0, 0)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mNavigationView.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclerView() {
        val linear = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        linear.stackFromEnd = true
        val rvBinding: RecyclerView = findViewById(R.id.recyclerView)
        rvBinding.apply {
            layoutManager = linear
            val decoration = DividerItemDecoration(this@DashboardActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            recyclerViewAdapter = DataAdapter(this@DashboardActivity)
            adapter = recyclerViewAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(DashboardActivityViewModel::class.java)
        viewModel.getRecordListObservable().observe(this, {
            if (it == null) {
                Toast.makeText(this@DashboardActivity, "No results found", Toast.LENGTH_LONG).show()
            } else {
                recyclerViewAdapter.recordsList = it.toMutableList()
                recyclerViewAdapter.notifyDataSetChanged()
            }
        })
        viewModel.getRecordList(accountSession.getToken())
        rvProgressBar.visibility = View.GONE
    }

    private fun logoutAccountObservable() {
        viewModel.getLogoutAccountDataObservable().observe(this, {
            if (it != null) {
                Toast.makeText(this@DashboardActivity, "Account has been logged out successfully.", Toast.LENGTH_LONG).show()
                accountSession.logoutUser()
            } else {
                Toast.makeText(this@DashboardActivity, "Unable to log out.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun deleteRecordObservable() {
        viewModel.getDeleteRecordDataObservable().observe(this, {
            if (it != null) {
                Toast.makeText(this@DashboardActivity, "Unable to delete record.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@DashboardActivity, "A record has been deleted.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun redirectToAdd() {
        val intent = Intent(this, AddLogActivity::class.java)
        startActivity(intent)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.action_search)
        searchView = search?.actionView as SearchView
        searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        searchView.queryHint = "Search"

        initSearch()
        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearch() {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchString = query!!.toLowerCase(Locale.getDefault()).trim()
                if (searchString.isNotBlank()) {
                    viewModel.searchRecord(searchString, accountSession.getToken())
                } else {
                    viewModel.getRecordList(accountSession.getToken())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.trim()?.isBlank() == true) {
                    viewModel.getRecordList(accountSession.getToken())
                }
                return true
            }
        })
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

    override fun showHoldMenu(record: Record, view: View) {
        val holdMenu = PopupMenu(this, view)
        holdMenu.inflate(R.menu.card_menu)
        holdMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.editCard -> {
                    val intent = Intent(this@DashboardActivity, EditRecordActivity::class.java)
                    intent.putExtra("record_id", record.id.toString())
                    startActivity(intent)
                    true
                }
                R.id.deleteCard -> {
                    confirmDelete(record.id.toString())
                    true
                }
                else -> true
            }
        }
        holdMenu.show()
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setIcon(R.drawable.ic_signout)
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Yes") {
                    dialog,_->
                viewModel.logout(accountSession.getToken())
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                    dialog,_->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun confirmDelete(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setIcon(R.drawable.ic_delete)
            .setMessage("Are you sure you want to delete? This action cannot be undone.")
            .setPositiveButton("Yes") {
                dialog,_->
                viewModel.deleteRecord(id, accountSession.getToken())
                recreate()
                dialog.dismiss()
            }
            .setNegativeButton("No") {
                dialog,_->
                dialog.cancel()
            }
            .create()
            .show()
    }
}