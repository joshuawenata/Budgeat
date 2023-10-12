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

class Support : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
    }

    fun Thankyou(view: View) {
        Function().getCurrentUserKey()?.let {
            Function().fetchSearchUserData(it){ user ->
                if(user[0].role=="customer"){
                    val intent = Intent(this, HomeCustomer::class.java)
                    startActivity(intent)
                    finishAffinity()
                }else{
                    val intent = Intent(this, HomeMerchant::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }
}