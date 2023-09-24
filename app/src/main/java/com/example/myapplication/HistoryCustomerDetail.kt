package com.example. myapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.AdapterCheckout
import com.example.myapplication.`object`.Menu

class HistoryCustomerDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_customer_detail)

        init()
    }

    private fun init() {
        val restaurantName: String? = intent.getStringExtra("user|restaurantName")
        val temp: ArrayList<Menu>? = intent.getSerializableExtra("menu") as? ArrayList<Menu>
        val countList: ArrayList<Int>? = intent.getIntegerArrayListExtra("count")

        val restaurantNameView: TextView = findViewById(R.id.historycustomerdetail_restaurantName)
        val recyclerView: RecyclerView = findViewById(R.id.historycustomerdetail_recyclerview)

        val menuList: ArrayList<Menu> = ArrayList()

        restaurantNameView.text = restaurantName

        if (temp != null) {
            if (countList != null) {
                for (i in 0 until countList.size) {
                    if(countList[i]!=0){
                        menuList.add(temp[i])
                    }
                }
            }
        }

        if (countList != null) {
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapter = AdapterCheckout(
                this,
                menuList,
                R.layout.card_menu,
                { itemView, item, position ->
                    val menuNameTextView = itemView.findViewById<TextView>(R.id.card_menu_name)
                    val menuQuantityTextView = itemView.findViewById<TextView>(R.id.card_menu_quantity)
                    val menuImageView = itemView.findViewById<ImageView>(R.id.card_menu_image)

                    menuNameTextView.text = item.menuName
                    val quantity = countList[position]
                    menuQuantityTextView.text = quantity.toString()
                    Picasso.get().load(item.menuImageUrl).into(menuImageView)
                }
            ) { item ->
                // Handle item click here
            }

            recyclerView.adapter = newAdapter
        }

    }
}

