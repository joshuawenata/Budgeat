package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toRegister(view: View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }
    fun toLogin(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}