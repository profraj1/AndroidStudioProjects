package com.training.foodrunner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import com.training.foodrunner.R

class OrderPlacedActivity : AppCompatActivity() {

    lateinit var btnOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)


        btnOk = findViewById(R.id.btnOk)
        btnOk.setOnClickListener {
            val strAct = Intent(this@OrderPlacedActivity, MainActivity::class.java)
            startActivity(strAct)
            finish()
        }
    }

    override fun onPause() {
        finish()
        super.onPause()
    }

    override fun onBackPressed() {
        val strAct = Intent(this@OrderPlacedActivity, MainActivity::class.java)
        startActivity(strAct)
    }
}
