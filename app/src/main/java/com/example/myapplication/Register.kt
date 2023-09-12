package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.ComponentActivity

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun CreateAccount(view: View) {
        val fullnameEditText: EditText = findViewById(R.id.register_fullname)
        val emailEditText: EditText = findViewById(R.id.register_email)
        val passwordEditText:EditText = findViewById(R.id.register_password)

        val name = fullnameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        val myRef = Function().getDBRef("user")
        val userKey: String = myRef.push().key.toString()

        var flag = true
        if (name.isEmpty()) {
            fullnameEditText.error = "Please fill the name field"
            flag = false
        }else if (email.isEmpty()) {
            fullnameEditText.error = "Please fill the email field"
            flag = false
        }else if (password.isEmpty()) {
            fullnameEditText.error = "Please fill the password field"
            flag = false
        }else{
            flag = true
        }

        if(flag){
            Function().writeDB("user", "$userKey/name",name)
            Function().writeDB("user", "$userKey/email",email)
            Function().writeDB("user", "$userKey/password",password)

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }


}