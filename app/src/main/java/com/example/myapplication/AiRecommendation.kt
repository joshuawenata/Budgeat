package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

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


}
