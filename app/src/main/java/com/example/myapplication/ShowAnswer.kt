package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ShowAnswer : ComponentActivity() {

    private val apiKey = "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"
    val apiUrl = "https://api.openai.com/v1/engines/gpt-3.5-turbo/completions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_answer)
    }

    fun toHome(view: View) {
        val intent = Intent(this, ShowAnswer::class.java)
        startActivity(intent)
        finish()
    }

}
