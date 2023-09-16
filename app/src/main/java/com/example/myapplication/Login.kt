package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class Login : ComponentActivity() {

    override fun onStart() {
        super.onStart()

        val auth: FirebaseAuth = Firebase.auth
        val user: FirebaseUser? = auth.currentUser

        if(user!=null){
            val myRef = Function().getDBRef("user")
            val valueEventListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val email = ds.child("email").getValue(String::class.java)
                        val role = ds.child("role").getValue(String::class.java)

                        if(user.email==email){
                            if (role == "customer") {
                                val intent = Intent(applicationContext, HomeCustomer::class.java)
                                startActivity(intent)
                                finishAffinity()
                            } else {
                                val intent = Intent(applicationContext, HomeMerchant::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, databaseError.message)
                }

            }
            myRef.addListenerForSingleValueEvent(valueEventListener)
        }

    }

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
                    val myRef = Function().getDBRef("user")
                    val valueEventListener = object : ValueEventListener {

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (ds in dataSnapshot.children) {
                                val emailDB = ds.child("email").getValue(String::class.java)
                                val role = ds.child("role").getValue(String::class.java)

                                if(email==emailDB){
                                    if (role == "customer") {
                                        val intent = Intent(applicationContext, HomeCustomer::class.java)
                                        startActivity(intent)
                                        finishAffinity()
                                    } else {
                                        val intent = Intent(applicationContext, HomeMerchant::class.java)
                                        startActivity(intent)
                                        finishAffinity()
                                    }
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.d(TAG, databaseError.message)
                        }

                    }
                    myRef.addListenerForSingleValueEvent(valueEventListener)
                } else {
                    // Login failed
                    val errorMessage = task.exception?.message
                    Toast.makeText(this, errorMessage ?: "Login failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}