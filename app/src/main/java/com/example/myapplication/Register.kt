package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
            emailEditText.error = "Please fill the email field"
            flag = false
        }else if (password.isEmpty()) {
            passwordEditText.error = "Please fill the password field"
            flag = false
        }else{
            flag = true
        }

        if(flag){
            registerAuth(userKey, name, email, password)
        }
    }

    private fun registerAuth(userKey: String, name: String, email: String, password:String){
        val auth: FirebaseAuth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration successful, now write user data to the database
                    Function().writeDB("user", "$userKey/name", name)
                    Function().writeDB("user", "$userKey/email", email)

                    // Start the login activity
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // User registration failed, display an error message
                    val errorMessage = task.exception?.message
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }


}