package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
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

class UserSetting : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usersetting)

        init()
    }

    private fun init() {
        val name: TextView = findViewById(R.id.user_name)
        val email: TextView = findViewById(R.id.user_email)

        Function().fetchSearchUserData(Function().getCurrentUserKey().toString()) { userDataList ->
            name.text = userDataList[0].name
        }
        email.text = Function().currentUser()?.email
    }

    fun toHome(view: View){
        val intent = Intent(this, HomeCustomer::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun toHistory(view: View){
        val intent = Intent(this, HistoryCustomer::class.java)
        startActivity(intent)
        finish()
    }
}
