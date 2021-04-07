package com.training.foodrunner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.training.foodrunner.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val strAct = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(strAct)
        }, 2000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
