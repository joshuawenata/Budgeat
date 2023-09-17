package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AddMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu)
    }

    fun addMenuDB(view: View) {
        val menuNameEditText: EditText = findViewById(R.id.add_menu_name)
        val menuDescriptionEditText: EditText = findViewById(R.id.add_menu_description)
        val menuStockEditText: EditText = findViewById(R.id.add_menu_stock)

        val menuName = menuNameEditText.text.toString()
        val menuDescription = menuDescriptionEditText.text.toString()
        val menuStock = menuStockEditText.text.toString()

        var flag = true

        if (menuName.isEmpty()) {
            menuNameEditText.error = "Please fill the menu name field"
            flag = false
        }else if (menuDescription.isEmpty()) {
            menuDescriptionEditText.error = "Please fill the menu description field"
            flag = false
        }else if (menuStock.isEmpty()) {
            menuStockEditText.error = "Please fill the menu stock field"
            flag = false
        }else{
            flag = true
        }

        if(flag){
            val myRef = Function().getDBRef("user")
            val menuKey: String = myRef.push().key.toString()
            val userKey: String? = Function().getCurrentUserKey()

            Function().writeDB("menu", "$userKey/$menuKey/menuName",menuName)
            Function().writeDB("menu", "$userKey/$menuKey/menuDescription",menuDescription)
            Function().writeDB("menu", "$userKey/$menuKey/menuStock",menuStock)
            Function().writeDB("menu", "$userKey/$menuKey/menuKey",menuKey)

            val intent = Intent(this, HomeMerchant::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
