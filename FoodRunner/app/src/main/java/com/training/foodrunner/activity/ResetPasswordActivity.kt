package com.training.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodrunner.R
import com.training.foodrunner.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmNewPassword: EditText
    lateinit var btnSubmit: Button
    var mobileNo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnSubmit)

        if (intent != null) {
            mobileNo = intent.getStringExtra("mobile_number")
        }


        btnSubmit.setOnClickListener {
            if (etOtp.text.isEmpty() or (etOtp.text.toString().length != 4)) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Please enter a valid OTP",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (etNewPassword.text.isEmpty() or (etNewPassword.text.toString().length < 4)) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Please enter a valid password",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (etConfirmNewPassword.text.toString() != etNewPassword.text.toString()) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Password ${etNewPassword.text} and Confirm Password ${etConfirmNewPassword.text} must be same",
                    Toast.LENGTH_SHORT
                ).show()

            }
            else if(ConnectionManager().checkConnectivity(this@ResetPasswordActivity)){
                val queue = Volley.newRequestQueue(this@ResetPasswordActivity)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val params = HashMap<String, String>()
                params["mobile_number"] = mobileNo
                params["password"] = etConfirmNewPassword.text.toString()
                params["otp"] = etOtp.text.toString()
                val jsonObject = JSONObject(params as Map<*, *>)
                var errorMsg = ""
                val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    Response.Listener {
                        try{
                            val rootData = it.getJSONObject("data")
                            val success = rootData.getBoolean("success")
                            errorMsg = rootData.getString("errorMessage")
                            if(success){
                                val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this@ResetPasswordActivity, "${errorMsg}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch (e: JSONException){
                            println(e)
                            Toast.makeText(this@ResetPasswordActivity, "No data Found!!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this@ResetPasswordActivity, "No Response from the server", Toast.LENGTH_SHORT).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String, String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "cad0f404a650a8"
                        return header
                    }
                }
                queue.add(jsonObjectRequest)
            }
            else{
                val dialog = AlertDialog.Builder(this@ResetPasswordActivity)
                dialog.setTitle("Error")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open Setting") { text, Listner ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, Listner ->
                    ActivityCompat.finishAffinity(this@ResetPasswordActivity)
                }
                dialog.create()
                dialog.show()
            }
        }

    }

}


