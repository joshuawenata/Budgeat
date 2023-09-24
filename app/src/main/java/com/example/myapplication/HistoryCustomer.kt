package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
import com.example.myapplication.adapter.Adapter
import com.example.myapplication.adapter.AdapterHistoryCustomer
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.squareup.picasso.Picasso

class HistoryCustomer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_customer)

        Function().fetchRestaurantHistoryData { restaurantDataList, menuDataList, countList ->
            val recyclerView = findViewById<RecyclerView>(R.id.history_customer_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapterHistoryCustomer = AdapterHistoryCustomer(
                this,
                restaurantDataList,
                R.layout.card_restaurant,
                { itemView, item ->
                    val restaurantNameTextView = itemView.findViewById<TextView>(R.id.card_restaurant_name)
                    val restaurantImage = itemView.findViewById<ImageView>(R.id.image_restaurant)
                    restaurantNameTextView.text = item.name
                    Picasso.get().load(item.imageDownloadUrl).into(restaurantImage)
                },
                { item, position ->
                    val intent = Intent(this, HistoryCustomerDetail::class.java)
                    intent.putExtra("menu", menuDataList[position])
                    intent.putExtra("count", countList)
                    intent.putExtra("restaurantName", item.name)
                    startActivity(intent)
                }
            )

            recyclerView.adapter = newAdapterHistoryCustomer
        }
    }

    fun toHome(view: View){
        val intent = Intent(this, HomeCustomer::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun toUserSetting(view: View){
        val intent = Intent(this, UserSetting::class.java)
        startActivity(intent)
        finish()
    }
}
