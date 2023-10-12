package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.Adapter
import com.example.myapplication.adapter.AdapterHistoryCustomer
import com.example.myapplication.`object`.Menu
import com.example.myapplication.`object`.Order
import com.example.myapplication.`object`.User
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.squareup.picasso.Picasso

class HistoryCustomer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_customer)

        val recyclerView = findViewById<RecyclerView>(R.id.history_customer_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Fetch customer ongoing data
        Function().fetchCustomerOngoingData { ongoingRestaurantDataList, ongoingMenuDataList, ongoingOrderList ->
            // Fetch customer history data
            Function().fetchCustomerHistoryData { historyRestaurantDataList, _, _, statusList ->

                // Create an adapter for the RecyclerView
                val newAdapterHistoryCustomer = AdapterHistoryCustomer(
                    this,
                    ongoingRestaurantDataList + historyRestaurantDataList,
                    R.layout.card_restaurant_with_status,
                    { itemView, item, position ->
                        val restaurantNameTextView =
                            itemView.findViewById<TextView>(R.id.card_restaurant_name_status)
                        val restaurantImage =
                            itemView.findViewById<ImageView>(R.id.image_restaurant_status)
                        val restaurantStatusTextView =
                            itemView.findViewById<TextView>(R.id.card_restaurant_status_status)
                        restaurantNameTextView.text = item.name

                        val mergedRestaurantDataList = ongoingRestaurantDataList + historyRestaurantDataList

                        if (mergedRestaurantDataList.size - position - 1 < statusList.size) {
                            restaurantStatusTextView.text = statusList[position + statusList.size - mergedRestaurantDataList.size]
                            if (statusList[position + statusList.size - mergedRestaurantDataList.size] == "canceled") {
                                restaurantStatusTextView.setTextColor(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.red
                                    )
                                )
                            } else if (statusList[position + statusList.size - mergedRestaurantDataList.size] == "completed") {
                                restaurantStatusTextView.setTextColor(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.dark_grey
                                    )
                                )
                            }
                        }

                        Picasso.get().load(item.imageDownloadUrl).into(restaurantImage)
                    },
                    { item, position ->
                        if (position < statusList.size) {
                            val intent = Intent(this, HistoryCustomerDetail::class.java)
                            intent.putExtra("menu", ongoingMenuDataList[position])

                            // Fetch count order
                            Function().fetchCountOrder(position) { countList ->
                                intent.putExtra("count", countList)
                                intent.putExtra("user|restaurantKey", item.userKey)
                                intent.putExtra("user|restaurantName", item.name)
                                intent.putExtra("orderKey", ongoingOrderList[position])
                                startActivity(intent)
                            }
                        }
                    }
                )

                recyclerView.adapter = newAdapterHistoryCustomer
            }
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

    fun toAi(view: View){
        val intent = Intent(this, AiRecommendation::class.java)
        startActivity(intent)
        finish()
    }
}
