package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity

class PickRole : ComponentActivity() {
    private var role: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_role)
    }

    fun pickCustomer(view: View) {
        customerPicked()
    }
    fun pickMerchant(view: View) {
        merchantPicked()
    }

    private fun customerPicked(){
        val imageView1 = findViewById<ImageView>(R.id.pickrole_checkcustomer)
        imageView1.visibility = View.VISIBLE
        val imageView2 = findViewById<ImageView>(R.id.pickrole_checkmerchant)
        imageView2.visibility = View.INVISIBLE
        role = "customer"
    }

    private fun merchantPicked(){
        val imageView1 = findViewById<ImageView>(R.id.pickrole_checkcustomer)
        imageView1.visibility = View.INVISIBLE
        val imageView2 = findViewById<ImageView>(R.id.pickrole_checkmerchant)
        imageView2.visibility = View.VISIBLE
        role = "merchant"
    }

    fun continueRegistration(view: View) {
        if(role==""){
            Toast.makeText(this, "Please pick one of role above.", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(this, Register::class.java)
            intent.putExtra("role",role)
            startActivity(intent)
            finish()
        }
    }
}
