package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.Adapter
import java.util.ArrayList

class Checkout : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val intent = intent
        val buyList: ArrayList<Int>? = intent.getIntegerArrayListExtra("buyList")
        val userKey = intent.getStringExtra("userKey")
        Log.d("test", userKey.toString())
        Function().fetchMenuData(userKey) { menuList ->
            // Now you can use the menuList or pass it to your RecyclerView setup code
            val recyclerView = findViewById<RecyclerView>(R.id.checkout_recyclerview)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapter = Adapter(
                this,
                menuList,
                R.layout.card_menu,
                { itemView, item ->
                    val menuNameTextView =
                        itemView.findViewById<TextView>(R.id.card_menu_name)
                    val menuDescriptionTextView =
                        itemView.findViewById<TextView>(R.id.card_menu_description)
                    val menuStockTextView =
                        itemView.findViewById<TextView>(R.id.card_menu_stock)

                    menuNameTextView.text = item.menuName
                    menuDescriptionTextView.text = item.menuDescription
                    menuStockTextView.text = item.menuStock
                },
                { item ->
                    // Handle item click here
                }
            )

            recyclerView.adapter = newAdapter
        }
    }
}
