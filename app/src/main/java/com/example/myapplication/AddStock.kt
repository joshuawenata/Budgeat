package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
import com.google.firebase.auth.FirebaseUser

class AddStock : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stock)
    }

    fun changeStock(view: View) {
        val intent = intent
        val userKey: String? = Function().getCurrentUserKey()
        val menuKey: String? = intent.getStringExtra("menuKey")
        val newStock: EditText? = findViewById(R.id.change_menu_stock)

        if (newStock != null) {
            if(newStock.text.toString().isEmpty()){
                newStock.error = "please fill the new stock"
            }else{
                Function().writeDB("menu", "$userKey/$menuKey/menuStock",newStock.text.toString())
                val toIntent = Intent(this, HomeMerchant::class.java)
                startActivity(toIntent)
            }
        }

    }

}
