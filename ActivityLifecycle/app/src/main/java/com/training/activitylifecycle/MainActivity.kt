package com.training.activitylifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(){

    lateinit var homeRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: AdapterClass
    var resList: ArrayList<String> = arrayListOf("abac", "list2","list3","list4", "list 5",
        "abac", "list2","list3","list4", "list 5",
        "abac", "list2","list3","list4", "list 5")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Log In"
        homeRecyclerView = findViewById(R.id.homeRecyclerView)
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = AdapterClass(this@MainActivity, resList)

        homeRecyclerView.adapter = adapter
        homeRecyclerView.layoutManager = layoutManager

    }




}
