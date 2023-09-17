package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.Adapter

class HomeCustomer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_customer)

        Function().fetchRestaurantData { userDataList ->
            val recyclerView = findViewById<RecyclerView>(R.id.home_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapterWithButton = Adapter(
                this,
                userDataList,
                R.layout.card_restaurant,
                { itemView, item ->
                    val restaurantNameTextView = itemView.findViewById<TextView>(R.id.card_restaurant_name)
                    restaurantNameTextView.text = item.name
                },
                { item ->
                    val intent = Intent(this, MenuList::class.java)
                    intent.putExtra("userKey", item.userKey)
                    intent.putExtra("userName", item.name)
                    startActivity(intent)
                }
            )

            recyclerView.adapter = newAdapterWithButton
        }

    }
}

