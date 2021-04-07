package com.training.foodrunner.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.training.foodrunner.R
import com.training.foodrunner.fragment.*

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var etSerachBox: EditText

    lateinit var txtName: TextView
    lateinit var txtPhoneNo: TextView
    lateinit var drawerHeaderLayout: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        etSerachBox = findViewById(R.id.etSearchBox)

        drawerHeaderLayout = navigationView.inflateHeaderView(R.layout.drawer_header)

        txtName = drawerHeaderLayout.findViewById(R.id.txtName)
        txtPhoneNo = drawerHeaderLayout.findViewById(R.id.txtPhoneNo)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        txtName.text = sharedPreferences.getString("Name", "Username")
        txtPhoneNo.text = sharedPreferences.getString("MobileNo", "Mobile Number")
        setUpToolbar()
        openHomeFragment()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()



        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home ->{
                    openHomeFragment()
                }
                R.id.myProfile -> {
                    etSerachBox.visibility = View.GONE
                    openProfileFragment()
                }
                R.id.favouriteRestaurants -> {
                    etSerachBox.visibility = View.GONE
                    openFavouriteFragment()
                }
                R.id.orderHistory -> {
                    etSerachBox.visibility = View.GONE
                   openOrderHistoryFragment()
                }
                R.id.faq -> {
                    etSerachBox.visibility = View.GONE
                    openFaqFragment()
                }
                R.id.logout -> {
                    sharedPreferences.edit().clear().apply()
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to Logout?")
                    dialog.setPositiveButton("Yes"){text, Listner ->
                        val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(loginIntent)
                        finish()
                    }
                    dialog.setNegativeButton("No"){text, Listner ->
                        drawerLayout.closeDrawer(navigationView, true)
                    }

                    dialog.create()
                    dialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openHomeFragment(){
        etSerachBox.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                HomeFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }
    fun openProfileFragment(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                ProfileFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "My Profile"
        navigationView.setCheckedItem(R.id.myProfile)
    }
    fun openFavouriteFragment(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                FavouriteRestaurantsFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "Favourite Restaurants"
        navigationView.setCheckedItem(R.id.favouriteRestaurants)
    }
    fun openOrderHistoryFragment(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                OrderHistoryFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "My Previous Order"
        navigationView.setCheckedItem(R.id.orderHistory)
    }
    fun openFaqFragment(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                FaqFragment()
            )
            .commit()
        drawerLayout.closeDrawer(navigationView)
        supportActionBar?.title = "Frequently Asked Question"
        navigationView.setCheckedItem(R.id.faq)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is HomeFragment -> openHomeFragment()
            else -> super.onBackPressed()
        }

    }

}
