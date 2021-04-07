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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodrunner.R
import com.training.foodrunner.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

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
            else if(etMobileNo.text.isEmpty()){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a valid Mobile No", Toast.LENGTH_SHORT).show()
            }
            else if(etDelAddress.text.isEmpty()){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a valid Delivery Address", Toast.LENGTH_SHORT).show()
            }
            else if(etPassword.text.isEmpty()){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a password", Toast.LENGTH_SHORT).show()
            }
            else if(etConfirmPassword.text.isEmpty()){
                Toast.makeText(this@SignupActivity,
                    "Please Enter a password", Toast.LENGTH_SHORT).show()
            }
            else if(etPassword.text.isNotEmpty() && etConfirmPassword.text.isNotEmpty()){
                if(etPassword.text.toString() != etConfirmPassword.text.toString()){
                    Toast.makeText(this@SignupActivity,
                        "Password ${etPassword.text} and Confirm Password ${etConfirmPassword.text} must be same", Toast.LENGTH_SHORT).show()
                }
                else{
                    sendRegisterDetails(this@SignupActivity)
                }
            }

        }
    }

    fun sendRegisterDetails(activity: SignupActivity){
        if(ConnectionManager().checkConnectivity(activity)){
            val queue = Volley.newRequestQueue(activity)
            val url = "http://13.235.250.119/v2/register/fetch_result"
            val params = HashMap<String, String>()
            params["name"] = "John Doe"
            params["mobile_number"] = "9998886666"
            params["password"] = "123456"
            params["address"] = "Gurugram"
            params["email"] = "john@doe.com"
            val jsonObject = JSONObject(params as Map<*, *>)

            val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener {
                try{
                    val rootData = it.getJSONObject("data")
                    val success = rootData.getBoolean("success")
                    val data = rootData.getJSONObject("data")
                    if(success){
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                catch (e:JSONException){
                    Toast.makeText(activity, "JSON Exception", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(activity, "Error Listener", Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val header = HashMap<String, String>()
                    header["Content-type"] = "application/json"
                    header["token"] = "cad0f404a650a8"
                    return header
                }
            }
            queue.add(jsonObjectRequest)
        }else{

        }
    }
}
