package com.example.myapplication

import com.example.myapplication.adapter.AdapterWithButton
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HomeMerchant : ComponentActivity() {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_merchant)

        val restaurantNameTextView: TextView = findViewById(R.id.restaurant_name_label)
        restaurantNameTextView.text = Function().currentUser()?.displayName

        Function().fetchMenuData(Function().getCurrentUserKey()) { menuList ->
            // Now you can use the menuList or pass it to your RecyclerView setup code
            val recyclerView = findViewById<RecyclerView>(R.id.menu_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapterWithButton = AdapterWithButton(
                this,
                menuList,
                R.layout.card_menu_with_button,
                { itemView, item ->
                    val menuNameTextView = itemView.findViewById<TextView>(R.id.card_menu_name_button)
                    val menuDescriptionTextView = itemView.findViewById<TextView>(R.id.card_menu_description_button)
                    val menuStockTextView = itemView.findViewById<TextView>(R.id.card_menu_stock_button)
                    val menuImage = itemView.findViewById<ImageView>(R.id.image_menu)

                    menuNameTextView.text = item.menuName
                    menuDescriptionTextView.text = item.menuDescription
                    menuStockTextView.text = item.menuStock
                    Picasso.get().load(item.menuImageUrl).into(menuImage)
                },
                { item ->
                    // Handle item click here
                },
                { item ->
                    // Handle button click here
                    val intent = Intent(this, AddStock::class.java)
                    intent.putExtra("menuKey",item.menuKey)
                    startActivity(intent)
                }
            )

            recyclerView.adapter = newAdapterWithButton
        }


    }

    fun toAddMenu(view: View){
        val intent = Intent(this, AddMenu::class.java)
        startActivity(intent)
    }

    fun toOrder(view: View){
        val intent = Intent(this, OrderList::class.java)
        startActivity(intent)
    }
    fun toUserSetting(view: View){
        val intent = Intent(this, UserSettingMerchant::class.java)
        startActivity(intent)
    }

}
