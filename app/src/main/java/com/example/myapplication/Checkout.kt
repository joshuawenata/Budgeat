package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.AdapterCheckout
import com.example.myapplication.`object`.Menu
import java.util.ArrayList

class Checkout : ComponentActivity() {
    private var buyList: ArrayList<Int>? = ArrayList()
    private var menuList: ArrayList<Menu>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val intent = intent
        val userKey = intent.getStringExtra("userKey")
        buyList = intent.getIntegerArrayListExtra("buyList")

        Function().fetchMenuData(userKey) { menuList ->
            val newMenuList: ArrayList<Menu> = ArrayList()
            for(i in buyList?.indices!!){
                if(buyList!![i]!=0){
                    this.menuList?.add(menuList[i])
                    newMenuList?.add(menuList[i])
                }
            }
            val temp = intent.getIntegerArrayListExtra("buyList")
            buyList = ArrayList()
            for (i in temp?.indices!!){
                if(temp!![i]!=0){
                    buyList!!.add(temp[i])
                }
            }
            // Now you can use the menuList or pass it to your RecyclerView setup code
            val recyclerView = findViewById<RecyclerView>(R.id.checkout_recyclerview)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val newAdapter = AdapterCheckout(
                this,
                newMenuList,
                R.layout.card_menu,
                { itemView, item, position ->
                    val menuNameTextView =
                        itemView.findViewById<TextView>(R.id.card_menu_name)
                    val menuQuantityTextView =
                        itemView.findViewById<TextView>(R.id.card_menu_quantity)

                    menuNameTextView.text = item.menuName
                    Log.d("test", buyList!!.size.toString())
                    Log.d("test", position.toString())
                    menuQuantityTextView.text = (buyList?.get(position) ?: Int).toString()
                },
                { item ->
                    // Handle item click here
                }
            )

            recyclerView.adapter = newAdapter
        }
    }

    fun toHome(view: View){
        val temp = intent.getIntegerArrayListExtra("buyList")
        buyList = ArrayList()
        for (i in temp?.indices!!){
            if(temp!![i]!=0){
                buyList!!.add(temp[i])
            }
        }

        val merchantKey: String? = intent.getStringExtra("userKey")
        for (i in menuList?.indices ?: emptyList<Int>().indices) {
            val orderRef = Function().getDBRef("order")
            val orderKey = orderRef.push().key
            val menuKey = menuList!![i].menuKey
            val buyItem = buyList?.getOrNull(i) ?: 0 // Default value if buyList is null or doesn't have an element at index i
            Function().writeDB("order", "$merchantKey/$orderKey/$menuKey", buyItem)
            Function().writeDB("order", "$merchantKey/$orderKey/userKey", Function().getCurrentUserKey().toString())
        }
        val intent = Intent(this, HomeCustomer::class.java)
        startActivity(intent)
        finishAffinity()
    }

}
