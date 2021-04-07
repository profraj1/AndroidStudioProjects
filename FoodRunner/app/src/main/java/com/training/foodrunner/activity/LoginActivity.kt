package com.training.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import com.training.foodrunner.R
import com.training.foodrunner.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etUsername: EditText
    lateinit var etLoginPassword: EditText
    lateinit var btnLogIn: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtSignUp: TextView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        var validUserName = sharedPreferences.getString("MobileNo", null)
        var validPassword = sharedPreferences.getString("ConfirmPassword", null)
        var isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            var mainAct = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(mainAct)
            finish()
        }

        etUsername = findViewById(R.id.etUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)

        btnLogIn.setOnClickListener {
            if(etUsername.text.isEmpty() or (etUsername.text.toString().length < 10)){
                Toast.makeText(this@LoginActivity, "Please enter your registered Mobile No", Toast.LENGTH_SHORT).show()
            }
            else if(etLoginPassword.text.isEmpty()){
                Toast.makeText(this@LoginActivity, "Please enter your valid password", Toast.LENGTH_SHORT).show()
            }

            else if (etUsername.text.toString() == validUserName && etLoginPassword.text.toString() == validPassword) {
                sharedPreferencesFunc()
                var mainAct = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(mainAct)
                this@LoginActivity.finishAffinity()

            } else {
                Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }

        }

        txtForgotPassword.setOnClickListener {
            var forgotPassAct = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(forgotPassAct)
        }

        txtSignUp.setOnClickListener {
            var signUpAct = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(signUpAct)
        }


    }

    fun sharedPreferencesFunc() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }


}
