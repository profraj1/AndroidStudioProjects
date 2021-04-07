package com.training.echomusicapp.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.training.echomusicapp.R

class SplashActivity : AppCompatActivity() {

    var permissionSets = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.PROCESS_OUTGOING_CALLS,
        android.Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (hasPermissions(this@SplashActivity, *permissionSets)) {
            startMainActivity()
        } else {
            ActivityCompat.requestPermissions(this@SplashActivity, permissionSets, 131)
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            131 ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED)
                {
                    startMainActivity()
                }
                else{
                    Toast.makeText(this@SplashActivity, "Please Grant all permissions to continue", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                return
            }
            else ->{
                Toast.makeText(this@SplashActivity, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                this.finish()
                return
            }
        }
    }


    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        var hasAllPermission = true
        for (permission in permissions) {
            val res = context.checkCallingOrSelfPermission(permission)
            if (res != PackageManager.PERMISSION_GRANTED) {
                hasAllPermission = false
            }
        }
        return hasAllPermission
    }

    fun startMainActivity(){
        Handler().postDelayed({
            val strAct = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(strAct)
            this.finish()
        }, 1000)
    }


}
