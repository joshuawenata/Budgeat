package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.ComponentActivity

class AiRecommendation : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_recommendation)
    }

    fun toHistory(view: View) {
        val intent = Intent(this, HistoryCustomer::class.java)
        startActivity(intent)
        finish()
    }

    fun toHome(view: View) {
        val intent = Intent(this, HomeCustomer::class.java)
        startActivity(intent)
        finish()
    }

    fun toUserSetting(view: View) {
        val intent = Intent(this, UserSetting::class.java)
        startActivity(intent)
        finish()
    }

    fun sendMessage(view: View) {
        val messagesEditText: EditText = findViewById(R.id.messages)
        val intent = Intent(this, ShowAnswer::class.java)
        intent.putExtra("messages", messagesEditText.text.toString())
        startActivity(intent)
    }
}
