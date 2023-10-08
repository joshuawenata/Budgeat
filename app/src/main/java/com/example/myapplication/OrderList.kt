package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
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

        Function().fetchMerchantOngoingData { ongoingCustomerDataList, ongoingMenuDataList, ongoingOrderList ->
            Function().fetchMerchantHistoryData { historyCustomerDataList, historyMenuDataList, historyOrderList, statusList ->

                // Merge data from ongoing and history sources
                val mergedCustomerDataList = historyCustomerDataList + ongoingCustomerDataList

                val newAdapterHistoryCustomer = AdapterHistoryCustomer(
                    this,
                    mergedCustomerDataList,
                    R.layout.card_user_with_status,
                    { itemView, item, position ->
                        val customerNameTextView = itemView.findViewById<TextView>(R.id.card_user_name_status)
                        val customerImage = itemView.findViewById<ImageView>(R.id.image_user_status)
                        val customerStatusTextView = itemView.findViewById<TextView>(R.id.card_user_status_status)
                        customerNameTextView.text = item.name
                        if (position >= statusList.size) {
                            customerStatusTextView.text = statusList[position - statusList.size]
                            if(statusList[position - statusList.size]=="canceled"||statusList[position - statusList.size]=="declined"){
                                customerStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
                            }else if(statusList[position - statusList.size]=="completed"){
                                customerStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.dark_grey))
                            }
                        }
                        Picasso.get().load(item.imageDownloadUrl).into(customerImage)
                    },
                    { item, position ->
                        if(statusList.size > position){
                            val intent = Intent(this, FinishOrder::class.java)
                            intent.putExtra("menu", ongoingMenuDataList[position])

                            // Fetch count order
                            Function().fetchCountMerchantOrder(position) { countList ->
                                intent.putExtra("count", countList)
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
