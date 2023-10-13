package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class Settings : ComponentActivity() {

    val name: EditText = findViewById(R.id.change_username)
    val phone: EditText = findViewById(R.id.change_phonenumber)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        Function().fetchSearchUserData(Function().getCurrentUserKey().toString()) { userDataList ->
            name.setText(userDataList[0].name)
            phone.setText(userDataList[0].phone)
        }
    }

    fun editProfile() {
        val userKey = Function().getCurrentUserKey()
        val nameValue = name.text.toString()
        val phoneValue = phone.text.toString()

        Function().writeDB("user", "$userKey/name", nameValue)
        Function().writeDB("user", "$userKey/phone", phoneValue)

        val intent = Intent(this, HomeCustomer::class.java)
        startActivity(intent)
        finish()
    }


}

