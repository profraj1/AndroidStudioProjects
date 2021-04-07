package com.training.myportfolio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class LoginActivity : AppCompatActivity() {

    lateinit var txtText: TextView
    //private val intentObj = Intent(this@LoginActivity, MainActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (! Python.isStarted()) {
            Python.start( AndroidPlatform(this));
        }
        val name = "Rahul Raj"
        var py = Python.getInstance()
        var pyf = py.getModule("pyScript")
        var pyObj = pyf.callAttr("test", name)

        txtText = findViewById(R.id.txtText)
        txtText.setText(pyObj.toString())




    }

    override fun onPause() {
        super.onPause()
        finish()
    }


}
