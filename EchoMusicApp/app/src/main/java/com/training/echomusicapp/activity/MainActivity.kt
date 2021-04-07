package com.training.echomusicapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.training.echomusicapp.R
import com.training.echomusicapp.adapter.NavMenuAdapterClass
import com.training.echomusicapp.model.NavMenuItem

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var navRecyclerView: RecyclerView
    lateinit var navMenuAdapter: NavMenuAdapterClass

    var menuItemList:ArrayList<NavMenuItem> = arrayListOf(
        NavMenuItem("All Songs", R.drawable.ic_all_songs),
        NavMenuItem("Favourites", R.drawable.ic_favourites),
        NavMenuItem("Setting", R.drawable.ic_settings),
        NavMenuItem("About Us", R.drawable.ic_about_us)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        navRecyclerView = findViewById(R.id.navRecyclerView)


        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "All Songs"
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        navRecyclerView.itemAnimator = DefaultItemAnimator()
        navMenuAdapter = NavMenuAdapterClass(this@MainActivity, menuItemList)
        navRecyclerView.adapter = navMenuAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}
