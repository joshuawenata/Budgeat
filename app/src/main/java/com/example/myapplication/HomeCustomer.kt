package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.Adapter
import com.squareup.picasso.Picasso

class HomeCustomer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_customer)

        Function().fetchRestaurantData { userDataList ->
            // Outside the function
            val searchEditText: EditText = findViewById(R.id.search_bar)
            val recyclerView = findViewById<RecyclerView>(R.id.home_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            val adapter = Adapter(
                this,
                userDataList,
                R.layout.card_restaurant,
                { itemView, item ->
                    val restaurantNameTextView = itemView.findViewById<TextView>(R.id.card_restaurant_name)
                    val restaurantImage = itemView.findViewById<ImageView>(R.id.image_restaurant)
                    restaurantNameTextView.text = item.name
                    Picasso.get().load(item.imageDownloadUrl).into(restaurantImage)
                },
                { item ->
                    val intent = Intent(this, MenuList::class.java)
                    intent.putExtra("userKey", item.userKey)
                    intent.putExtra("userName", item.name)
                    intent.putExtra("userAddress", item.address)
                    intent.putExtra("imageDownloadUrl", item.imageDownloadUrl)
                    startActivity(intent)
                }
            )

            recyclerView.adapter = adapter

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val filterText = s.toString().toLowerCase()
                    val filteredData = userDataList.filter { it.name.toLowerCase().contains(filterText) }
                    adapter.filterList(filteredData)
                }
            })

            adapter.notifyDataSetChanged()
        }

    }

    fun toHistory(view: View){
        val intent = Intent(this, HistoryCustomer::class.java)
        startActivity(intent)
    }

    fun toUserSetting(view: View){
        val intent = Intent(this, UserSetting::class.java)
        startActivity(intent)
    }

    fun toAi(view: View){
        val intent = Intent(this, AiRecommendation::class.java)
        startActivity(intent)
    }

}

