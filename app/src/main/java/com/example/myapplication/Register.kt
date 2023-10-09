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
        val phoneEditText: EditText = findViewById(R.id.register_phone)
        val passwordEditText:EditText = findViewById(R.id.register_password)
        val confirmPasswordEditText: EditText = findViewById(R.id.register_confirm_password)

        val name = fullnameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        val myRef = Function().getDBRef("user")

        var flag = true
        if (name.isEmpty()) {
            fullnameEditText.error = "Please fill the name field"
            flag = false
        }else if (phone.isEmpty()) {
            phoneEditText.error = "Please fill the phone number field"
            flag = false
        }else if (email.isEmpty()) {
            emailEditText.error = "Please fill the email field"
            flag = false
        }else if (password.isEmpty()) {
            passwordEditText.error = "Please fill the password field"
            flag = false
        }else if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.error = "Please fill the confirm password field"
            flag = false
        }else if (confirmPassword != password) {
            passwordEditText.error = "Password need to be the same with confirmation password"
            flag = false
        }else{
            flag = true
        }

        if(flag){
            registerAuth(name, phone, email, password)
        }
    }

    private fun registerAuth(name: String, phone: String, email: String, password:String){
        val intent = intent
        val role = intent.getStringExtra("role")
        val auth: FirebaseAuth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userKey: String? = auth.uid
                    // User registration successful, now write user data to the database
                    Function().writeDB("user", "$userKey/name", name)
                    Function().writeDB("user", "$userKey/phone", phone)
                    Function().writeDB("user", "$userKey/email", email)
                    Function().writeDB("user", "$userKey/address", "Jl. Pasir Kaliki no. 25")
                    Function().writeDB("user", "$userKey/latitude", "0.0")
                    Function().writeDB("user", "$userKey/longitude", "0.0")
                    if(role=="customer"){
                        Function().writeDB("user", "$userKey/imageDownloadUrl", "https://firebasestorage.googleapis.com/v0/b/budgeat-25e02.appspot.com/o/images%2Fcustomer_dummy.jpg?alt=media&token=a3c8d195-7b9e-4977-b07f-d5ba4335a3f9&_gl=1*g5mut3*_ga*NzgwODU3NDU4LjE2OTQ1MjcyOTE.*_ga_CW55HF8NVT*MTY5NjczODExNi4zNy4xLjE2OTY3Mzk3NTAuNDAuMC4w")
                    }else{
                        Function().writeDB("user", "$userKey/imageDownloadUrl", "https://firebasestorage.googleapis.com/v0/b/budgeat-25e02.appspot.com/o/images%2Fmerchant_dummy.jpg?alt=media&token=5f35150c-4392-4543-a60a-0e3147aa86e8&_gl=1*1dih2yk*_ga*NzgwODU3NDU4LjE2OTQ1MjcyOTE.*_ga_CW55HF8NVT*MTY5NjczODExNi4zNy4xLjE2OTY3MzkzMjUuMTkuMC4w")
                    }
                    if (userKey != null) {
                        Function().writeDB("user", "$userKey/userKey", userKey)
                    }
                    if (role != null) {
                        Function().writeDB("user", "$userKey/role", role)
                    }


                    if (role == "merchant"){
                        val intent = Intent(this, HomeMerchant::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }else{
                        val intent = Intent(this, HomeCustomer::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }

                } else {
                    // User registration failed, display an error message
                    val errorMessage = task.exception?.message
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }


}