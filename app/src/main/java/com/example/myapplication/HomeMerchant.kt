package com.example.myapplication

import Adapter
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.`object`.Menu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeMerchant : ComponentActivity() {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_merchant)

        Function().fetchMenuData { menuList ->
            // Now you can use the menuList or pass it to your RecyclerView setup code
            val recyclerView = findViewById<RecyclerView>(R.id.menu_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapter = Adapter(
                this,
                menuList,
                R.layout.card_menu
            ) { itemView, item ->
                val menuNameTextView = itemView.findViewById<TextView>(R.id.card_menu_name)
                val menuDescriptionTextView = itemView.findViewById<TextView>(R.id.card_menu_description)
                val menuStockTextView = itemView.findViewById<TextView>(R.id.card_menu_stock)

                menuNameTextView.text = item.menuName
                menuDescriptionTextView.text = item.menuDescription
                menuStockTextView.text = item.menuStock
            }

            recyclerView.adapter = newAdapter
        }

    }

    fun toAddMenu(view: View){
        val intent = Intent(this, AddMenu::class.java)
        startActivity(intent)
    }

}
