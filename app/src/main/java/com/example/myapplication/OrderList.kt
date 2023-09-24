package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AdapterHistoryCustomer
import com.squareup.picasso.Picasso

class OrderList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        val recyclerView = findViewById<RecyclerView>(R.id.order_list_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        Function().fetchOrderHistoryData { restaurantDataList, menuDataList ->
            val newAdapterHistoryCustomer = AdapterHistoryCustomer(
                this,
                restaurantDataList,
                R.layout.card_user,
                { itemView, item ->
                    val userNameTextView = itemView.findViewById<TextView>(R.id.card_user_name)
                    val userImage = itemView.findViewById<ImageView>(R.id.image_user)
                    userNameTextView.text = item.name
                    Picasso.get().load(item.imageDownloadUrl).into(userImage)
                },
                { item, position ->
                    val intent = Intent(this, HistoryCustomerDetail::class.java)
                    intent.putExtra("menu", menuDataList[position])

                    // Fetch count order
                    Function().fetchCountOrder(position) { countList ->
                        intent.putExtra("count", countList)
                        intent.putExtra("user|restaurantName", item.name)
                        startActivity(intent)
                    }
                }
            )

            recyclerView.adapter = newAdapterHistoryCustomer
        }
    }

    fun toHome(view: View) {
        val intent = Intent(this, HomeMerchant::class.java)
        startActivity(intent)
        finishAffinity()
    }
    fun toUserSetting(view: View) {
        val intent = Intent(this, UserSettingMerchant::class.java)
        startActivity(intent)
        finish()
    }
}
