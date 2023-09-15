package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.NonNull
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun LoginToApp(view: View) {
        val emailEditText: EditText = findViewById(R.id.login_email)
        val passwordEditText: EditText = findViewById(R.id.login_password)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        var flag = true
        if (email.isEmpty()) {
            emailEditText.error = "Please fill the email field"
            flag = false
        }else if (password.isEmpty()) {
            passwordEditText.error = "Please fill the password field"
            flag = false
        }else{
            flag = true
        }

        if(flag){
            loginAuth(email, password)
        }

    }

    private fun loginAuth(email: String, password:String){
        val auth: FirebaseAuth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    // Login failed
                    val errorMessage = task.exception?.message
                    Toast.makeText(this, errorMessage ?: "Login failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}