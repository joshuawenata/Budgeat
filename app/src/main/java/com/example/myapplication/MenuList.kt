package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AdapterWithButtonAndPicker
import com.squareup.picasso.Picasso

class MenuList : ComponentActivity() {
    private val buyList: ArrayList<Int> = ArrayList()
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        val intent = intent

        val restaurantImage: ImageView = findViewById(R.id.restaurant_dashboard_image)
        val restaurantName: TextView? = findViewById(R.id.restaurant_name)
        val restaurantAddress: TextView? = findViewById(R.id.restaurant_address)
        if (restaurantName != null) {
            restaurantName.text = intent.getStringExtra("userName")
        }
        if (restaurantAddress != null) {
            restaurantAddress.text = intent.getStringExtra("userAddress")
        }
        Picasso.get().load(intent.getStringExtra("imageDownloadUrl")).into(restaurantImage)
        val userKey = intent.getStringExtra("userKey")

        Function().fetchMenuData(userKey) { menuList ->
            // Now you can use the menuList or pass it to your RecyclerView setup code
            val recyclerView = findViewById<RecyclerView>(R.id.menulist_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapterWithButtonAndPicker = AdapterWithButtonAndPicker(
                this,
                menuList,
                R.layout.card_menu_with_button_and_picker,
                { itemView, item ->
                    val menuNameTextView = itemView.findViewById<TextView>(R.id.card_menu_name_picker)
                    val menuDescriptionTextView = itemView.findViewById<TextView>(R.id.card_menu_description_picker)
                    val menuCategoryTextView = itemView.findViewById<TextView>(R.id.card_menu_category_picker)
                    val menuStockTextView = itemView.findViewById<TextView>(R.id.card_menu_stock_picker)
                    val menuImageView = itemView.findViewById<ImageView>(R.id.card_menu_image_picker)

                    menuNameTextView.text = item.menuName
                    menuDescriptionTextView.text = item.menuDescription
                    menuCategoryTextView.text = item.menuCategory
                    menuStockTextView.text = item.menuStock
                    Picasso.get().load(item.menuImageUrl).into(menuImageView)

                    if(menuCategoryTextView.text.equals("Halal")){
                        menuCategoryTextView.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
                    }else{
                        menuCategoryTextView.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                    }

                    buyList.add(0)
                },
                { item ->
                    // Handle item click here
                },
                { itemView, item, position ->
                    // Handle button click here
                    val addButton = itemView.findViewById<ImageButton>(R.id.card_add_order)
                    val plus = itemView.findViewById<ImageButton>(R.id.plus)
                    val minus  = itemView.findViewById<ImageButton>(R.id.minus)
                    val buyCount  = itemView.findViewById<TextView>(R.id.buy_count)

                    addButton.visibility = View.GONE
                    plus.visibility = View.VISIBLE
                    minus.visibility = View.VISIBLE
                    buyCount.visibility = View.VISIBLE

                    buyList[position] = 1
                    buyCount.text = 1.toString()
                },
                { itemView, item, position ->
                    // Handle minus button click here
                    buyList[position] = 0
                    val textCount = itemView.findViewById<TextView>(R.id.buy_count)
                    if(Integer.parseInt(textCount.text.toString())>0){
                        val newValue: Int = Integer.parseInt(textCount.text.toString())-1
                        textCount.text = newValue.toString()
                        buyList[position] = newValue
                    }
                },
                { itemView, item, position ->
                    // Handle plus button click here
                    buyList[position] = 0
                    val textCount = itemView.findViewById<TextView>(R.id.buy_count)
                    val menuStockTextView = itemView.findViewById<TextView>(R.id.card_menu_stock_picker)
                    if(Integer.parseInt(textCount.text.toString())<Integer.parseInt(menuStockTextView.text.toString())){
                        val newValue: Int = Integer.parseInt(textCount.text.toString())+1
                        textCount.text = newValue.toString()
                        buyList[position] = newValue
                    }
                }
            )

            recyclerView.adapter = newAdapterWithButtonAndPicker

        }
    }

    fun toCheckout(view: View){
        val userKey = intent.getStringExtra("userKey")
        val intent = Intent(this, Checkout::class.java)
        intent.putExtra("buyList",buyList)
        intent.putExtra("userKey", userKey)
        startActivity(intent)
    }

}

