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
import com.example.dtls_android.session.LoginPref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.util.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var recyclerViewAdapter: DataAdapter
    private lateinit var viewModel: DashboardActivityViewModel
    private lateinit var rvProgressBar: ProgressBar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mNavigationView: NavigationView
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var addButton: FloatingActionButton
    private lateinit var textNoTask: TextView
    private lateinit var resultContract: ActivityResultLauncher<Intent>
    private lateinit var searchView: SearchView
    private lateinit var mheaderView: View
    private lateinit var mheaderUsername: TextView
    private lateinit var mheaderDesc: TextView
    lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupToolbar()
        session = LoginPref(this)
        session.checkLogin()

        initMain()
        initNavigationView()
        initRecyclerView()
        initViewModel()

        addButton.setOnClickListener { redirectToAdd() }
    }

    private fun initMain() {
        addButton = findViewById(R.id.fab)
        textNoTask = findViewById(R.id.textNoTask)
        mRecyclerView = findViewById(R.id.recyclerView)
        rvProgressBar = findViewById(R.id.rvProgressBar)
        textNoTask.visibility = View.GONE
    }

    private fun initNavigationView() {
        mNavigationView = findViewById(R.id.nav_view)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mheaderView = mNavigationView.getHeaderView(0)
        mheaderUsername = mheaderView.findViewById(R.id.nav_header_username)
        mheaderDesc = mheaderView.findViewById(R.id.nav_header_desc)
        mheaderUsername.text = session.getUserDetails()
        mheaderDesc.text = "Hello World!~"

        val toggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            binding.toolbar,
            0,
            0
        )
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
            recyclerViewAdapter = DataAdapter()
            adapter = recyclerViewAdapter
        }
    }

    private fun initViewModel() {
        rvProgressBar.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this).get(DashboardActivityViewModel::class.java)
        viewModel.getRecordListObservable().observe(this, {
            if (it == null) {
                Toast.makeText(this@DashboardActivity, "No results found", Toast.LENGTH_LONG).show()
            } else {
                recyclerViewAdapter.recordsList = it.toMutableList()
                recyclerViewAdapter.notifyDataSetChanged()
            }
        })
        viewModel.getRecordList()
        rvProgressBar.visibility = View.GONE
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
                    viewModel.searchRecord(searchString)
                } else {
                    viewModel.getRecordList()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.trim()?.isBlank() == true) {
                    viewModel.getRecordList()
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
}