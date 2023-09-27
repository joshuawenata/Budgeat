package com.example.myapplication

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AdapterCheckout
import com.example.myapplication.`object`.Menu
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.squareup.picasso.Picasso
import java.math.BigDecimal

class FinishOrder : ComponentActivity() {
    private var buyList: java.util.ArrayList<Int>? = java.util.ArrayList()
    val menuList: ArrayList<Menu> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_order)

        init()
    }

    private fun init() {
        val restaurantName: String? = intent.getStringExtra("user|restaurantName")
        val temp: ArrayList<Menu>? = intent.getSerializableExtra("menu") as? ArrayList<Menu>
        val countList: ArrayList<Int>? = intent.getIntegerArrayListExtra("count")

        val restaurantNameView: TextView = findViewById(R.id.finishorder_restaurantName)
        val recyclerView: RecyclerView = findViewById(R.id.finishorder_recyclerview)

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
                    val menuPriceTextView = itemView.findViewById<TextView>(R.id.card_menu_price)
                    val menuImageView = itemView.findViewById<ImageView>(R.id.card_menu_image)

                    menuNameTextView.text = item.menuName
                    val quantity = countList[position]
                    val totalAmount = BigDecimal(quantity) * item.menuPrice.toBigDecimal()
                    menuPriceTextView.text = "$totalAmount,00"
                    menuQuantityTextView.text = quantity.toString()
                    Picasso.get().load(item.menuImageUrl).into(menuImageView)
                }
            ) { item ->
                // Handle item click here
            }

            recyclerView.adapter = newAdapter
        }

    }

    fun decline(view: View) {
        val temp = intent.getIntegerArrayListExtra("count")
        buyList = java.util.ArrayList()
        for (i in temp?.indices!!){
            if(temp!![i]!=0){
                buyList!!.add(temp[i])
            }
        }
        val merchantKey = Function().getCurrentUserKey()
        val orderKey = intent.getStringExtra("orderKey")
        Function().fetchMenuData(merchantKey){menuDBList ->
            var counting = 0
            for (i in menuList!!) {
                val menuKey = i.menuKey
                val stockDifference = Integer.parseInt(menuDBList!![counting].menuStock) + buyList!![counting]
                val stockDifferenceString = stockDifference.toString()

                // Update the menu stock using the writeDB function
                Function().writeDB("menu", "$merchantKey/$menuKey/menuStock", stockDifferenceString)
                if (orderKey != null) {
                    Function().deleteOrder(orderKey)
                }
                counting++
            }
        }

        val intent = Intent(this, OrderList::class.java)
        startActivity(intent)
        finish()
    }
    fun finish(view: View) {
        val orderKey = intent.getStringExtra("orderKey")
        if (orderKey != null) {
            Function().deleteOrder(orderKey)
        }

        val intent = Intent(this, OrderList::class.java)
        startActivity(intent)
        finish()
    }
}
