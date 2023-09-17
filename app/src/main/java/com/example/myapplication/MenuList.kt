package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AdapterWithButton
import com.example.myapplication.ui.theme.MyApplicationTheme

class MenuList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        val intent = intent

        val restaurantName: TextView? = findViewById(R.id.restaurant_name)
        if (restaurantName != null) {
            restaurantName.text = intent.getStringExtra("userName")
        }
        val userKey = intent.getStringExtra("userKey")

        Function().fetchMenuData(userKey) { menuList ->
            // Now you can use the menuList or pass it to your RecyclerView setup code
            val recyclerView = findViewById<RecyclerView>(R.id.menulist_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapterWithButton = AdapterWithButton(
                this,
                menuList,
                R.layout.card_menu_with_button,
                { itemView, item ->
                    val menuNameTextView = itemView.findViewById<TextView>(R.id.card_menu_name)
                    val menuDescriptionTextView =
                        itemView.findViewById<TextView>(R.id.card_menu_description)
                    val menuStockTextView = itemView.findViewById<TextView>(R.id.card_menu_stock)

                    menuNameTextView.text = item.menuName
                    menuDescriptionTextView.text = item.menuDescription
                    menuStockTextView.text = item.menuStock
                },
                { item ->
                    // Handle item click here
                },
                { item ->
                    // Handle button click here
                    val intent = Intent(this, Checkout::class.java)
                    intent.putExtra("menuKey", item.menuKey)
                    startActivity(intent)
                }
            )

            recyclerView.adapter = newAdapterWithButton
        }
    }
}

