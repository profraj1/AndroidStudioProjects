package com.training.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.training.bookhub.R
import com.training.bookhub.fragment.AboutAppFragment
import com.training.bookhub.fragment.DashboardFragment
import com.training.bookhub.fragment.FavouritesFragment
import com.training.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem:MenuItem? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigation_view)

        setUpToolbar()
        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem =it

            when(it.itemId){
                R.id.dashboard -> {
                    openDashboard()
                }
                R.id.favourites -> {
                    openFavourites()
                }
                R.id.profile ->{
                    openProfile()
                }
                R.id.about_app ->{
                   openAboutApp()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Hub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

       if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                DashboardFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    fun openFavourites(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                FavouritesFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "Favourites"
    }

    fun openProfile(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, ProfileFragment())
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "Profile"
    }

    fun openAboutApp(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                AboutAppFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "About App"
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }

    }
}
