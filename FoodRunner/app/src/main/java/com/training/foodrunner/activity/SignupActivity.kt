package com.training.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.training.foodrunner.R

class SignupActivity : AppCompatActivity() {

    lateinit var btnArrowLeft: Button
    lateinit var txtRegisterYourself: TextView
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobileNo: EditText
    lateinit var etDelAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        btnArrowLeft = findViewById(R.id.btnArrowLeft)
        txtRegisterYourself = findViewById(R.id.txtRegisterYourself)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobileNo = findViewById(R.id.etMobileNo)
        etDelAddress = findViewById(R.id.etDelAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnArrowLeft.setOnClickListener {
            var loginAct = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(loginAct)
        }

        btnRegister.setOnClickListener {
            if(etName.text.isEmpty()){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a valid Name", Toast.LENGTH_SHORT).show()
            }
            else if(etEmail.text.isEmpty()){
                    Toast.makeText(this@SignupActivity,
                        "Please Enter a valid Email Address", Toast.LENGTH_SHORT).show()
            }
            else if(etMobileNo.text.isEmpty() or (etMobileNo.text.toString().length < 10)){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a valid Mobile No", Toast.LENGTH_SHORT).show()
            }
            else if(etDelAddress.text.isEmpty()){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a valid Delivery Address", Toast.LENGTH_SHORT).show()
            }
            else if(etPassword.text.isEmpty() or (etPassword.text.toString().length < 4)){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a valid password", Toast.LENGTH_SHORT).show()
            }
            else if(etConfirmPassword.text.isEmpty()){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a valid password", Toast.LENGTH_SHORT).show()
            }
            else if(etPassword.text.isNotEmpty() && etConfirmPassword.text.isNotEmpty()){
                if(etPassword.text.toString() != etConfirmPassword.text.toString()){
                    Toast.makeText(this@SignupActivity,
                        "Password ${etPassword.text} and Confirm Password ${etConfirmPassword.text} must be same", Toast.LENGTH_SHORT).show()
                }
                else{
                    sharedPreferences.edit().putString("Name", etName.text.toString()).apply()
                    sharedPreferences.edit().putString("EmailAddress", etEmail.text.toString()).apply()
                    sharedPreferences.edit().putString("MobileNo", etMobileNo.text.toString()).apply()
                    sharedPreferences.edit().putString("DeliveryAddress", etDelAddress.text.toString()).apply()
                    sharedPreferences.edit().putString("Password", etPassword.text.toString()).apply()
                    sharedPreferences.edit().putString("ConfirmPassword", etConfirmPassword.text.toString()).apply()

                    var loginAct = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(loginAct)
                    Toast.makeText(this@SignupActivity,
                        "You have successfully registered yourself. Please log in.", Toast.LENGTH_SHORT).show()

                    this@SignupActivity.finishAffinity()

                }
            }

        }
    }
}
