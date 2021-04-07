package com.training.foodrunner.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.training.foodrunner.R

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmNewPassword: EditText
    lateinit var btnSubmit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnSubmit)

        val mobileNo = intent.getStringExtra("mobile_number")

        btnSubmit.setOnClickListener {
            
        }
    }
}
