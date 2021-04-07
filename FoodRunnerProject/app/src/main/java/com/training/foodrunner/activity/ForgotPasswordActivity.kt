package com.training.foodrunner.activity

import android.app.AlertDialog
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

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var etForgotPwdMobileNo: EditText
    lateinit var etForgotPwdEmail: EditText
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etForgotPwdEmail = findViewById(R.id.etForgotPwdEmail)
        etForgotPwdMobileNo = findViewById(R.id.etForgotPwdMobileNo)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            if(ConnectionManager().checkConnectivity(this@ForgotPasswordActivity)){
                val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
                val params = HashMap<String, String>()
                params["mobile_number"] = etForgotPwdMobileNo.text.toString()
                params["email"] = etForgotPwdEmail.text.toString()
                val jsonObject = JSONObject(params as Map<*, *>)

                val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Response.Listener {
                    try{
                        val rootData = it.getJSONObject("data")
                        val success = rootData.getBoolean("success")
                        val firstTry = rootData.getBoolean("first_try")
                        if(success){
                            val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                            intent.putExtra("mobile_number", etForgotPwdMobileNo.text.toString())
                            startActivity(intent)
                        }
                    }
                    catch (e: JSONException){
                        Toast.makeText(this@ForgotPasswordActivity, "Json Exception", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this@ForgotPasswordActivity, "Response Error", Toast.LENGTH_SHORT).show()
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
                val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                dialog.setTitle("Error")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open Setting") { text, Listner ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, Listner ->
                    ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                }
                dialog.create()
                dialog.show()
            }
        }


    }
}
