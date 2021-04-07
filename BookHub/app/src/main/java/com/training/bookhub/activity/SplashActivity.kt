package com.training.bookhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.training.bookhub.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val startActivity = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(startActivity)
        }, 3000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
