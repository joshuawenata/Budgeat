package com.example.myapplication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.myapplication.`object`.Menu
import com.example.myapplication.`object`.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.`object`.Order
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase

class Function {

//    function to get DB Reference

    fun getDBRef(ref: String): DatabaseReference {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        return database.getReference(ref)
    }

    //    function for writing to database
    fun writeDB(ref:String, path: String, value: Int){
        val myRef = getDBRef(ref)
        myRef.child(path).setValue(value)
    }
    fun writeDB(ref:String, path: String, value: String){
        val myRef = getDBRef(ref)
        myRef.child(path).setValue(value)
    }

//    function to read from database

    fun fetchUserData(callback: (ArrayList<User>) -> Unit) {
        val myRef = Function().getDBRef("user")
        val userKey: String? = Function().getCurrentUserKey()
        val userDataList: ArrayList<User> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (userKey==ds.child("userKey").getValue(String::class.java)){
                        val name = ds.child("name").getValue(String::class.java)
                        val email = ds.child("email").getValue(String::class.java)
                        val phone = ds.child("phone").getValue(String::class.java)
                        val role = ds.child("role").getValue(String::class.java)
                        val userKey = ds.child("userKey").getValue(String::class.java)
                        val imageDownloadUrl = ds.child("imageDownloadUrl").getValue(String::class.java)

                        if (name != null && email != null && phone != null && role != null && userKey != null && imageDownloadUrl != null) {
                            userDataList.add(User( email, phone, name, role, userKey, imageDownloadUrl))
                        }
                    }
                }
                callback(userDataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchDB(path: String, callback: (String?) -> Unit) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val dbQuery = databaseReference.child(path)

        dbQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the data exists at the specified path
                if (dataSnapshot.exists()) {
                    val value = dataSnapshot.getValue(String::class.java)
                    callback(value)
                } else {
                    // Data does not exist at the specified path
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error if the database query is cancelled
                // You can add your error handling logic here
                callback(null)
            }
        })
    }

    fun fetchSearchUserData(userKey: String, callback: (ArrayList<User>) -> Unit) {
        val myRef = Function().getDBRef("user")
        val userDataList: ArrayList<User> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (userKey==ds.child("userKey").getValue(String::class.java)){
                        val name = ds.child("name").getValue(String::class.java)
                        val email = ds.child("email").getValue(String::class.java)
                        val phone = ds.child("phone").getValue(String::class.java)
                        val role = ds.child("role").getValue(String::class.java)
                        val userKey = ds.child("userKey").getValue(String::class.java)
                        val imageDownloadUrl = ds.child("imageDownloadUrl").getValue(String::class.java)

                        if (name != null && email != null && phone != null && role != null && userKey != null && imageDownloadUrl != null) {
                            userDataList.add(User( email, phone, name, role, userKey, imageDownloadUrl))
                        }
                    }
                }
                callback(userDataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchRestaurantData(callback: (ArrayList<User>) -> Unit) {
        val myRef = Function().getDBRef("user")
        val userDataList: ArrayList<User> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (ds.child("role").getValue(String::class.java) == "merchant"){
                        val name = ds.child("name").getValue(String::class.java)
                        val email = ds.child("email").getValue(String::class.java)
                        val phone = ds.child("phone").getValue(String::class.java)
                        val role = ds.child("role").getValue(String::class.java)
                        val userKey = ds.child("userKey").getValue(String::class.java)
                        val imageDownloadUrl = ds.child("imageDownloadUrl").getValue(String::class.java)

                        if (name != null && email != null && phone != null && role != null && userKey != null && imageDownloadUrl != null) {
                            userDataList.add(User( email, phone, name, role, userKey, imageDownloadUrl))
                        }
                    }
                }
                callback(userDataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchMenuData(userKey: String?, callback: (ArrayList<Menu>) -> Unit) {
        val myRef = Function().getDBRef("menu")
        val menuList: ArrayList<Menu> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val uid = ds.key
                    if (userKey == uid){
                        for (dss in ds.children) {
                            val menuName = dss.child("menuName").getValue(String::class.java)
                            val menuDescription = dss.child("menuDescription").getValue(String::class.java)
                            val menuStock = dss.child("menuStock").getValue(String::class.java)
                            val menuKey = dss.child("menuKey").getValue(String::class.java)
                            val menuImageUrl = dss.child("menuImageUrl").getValue(String::class.java)

                            if( menuName != null && menuDescription != null && menuStock != null && menuKey != null && menuImageUrl != null) {
                                menuList.add(Menu(menuName, menuDescription, menuStock, menuKey, menuImageUrl))
                            }
                        }
                    }
                }
                callback(menuList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchRestaurantHistoryData(callback: (ArrayList<User>, ArrayList<ArrayList<Menu>>) -> Unit) {
        val myRef = Function().getDBRef("order")
        val restaurantDataList: ArrayList<User> = ArrayList()
        val menuDataList: ArrayList<ArrayList<Menu>> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val restaurantKey: String? = ds.key
                    for (dss in ds.children) {
                        val userKey = dss.child("userKey").getValue(String::class.java)
                        if (userKey == Function().getCurrentUserKey() && restaurantKey != null) {
                            fetchSearchUserData(restaurantKey) { userDataList ->
                                for (userData in userDataList) {
                                    if (restaurantKey == userData.userKey) {
                                        restaurantDataList.add(User(userData.email, userData.phone, userData.name, userData.role, userData.userKey, userData.imageDownloadUrl))
                                    }
                                }
                                // Move this callback inside the fetchSearchUserData callback to ensure data consistency.
                                fetchMenuData(restaurantKey) { menuList ->
                                    val temp: ArrayList<Menu> = ArrayList()
                                    for (menu in menuList) {
                                        val menuKey = menu.menuKey
                                        val menuName = menu.menuName
                                        val menuDescription = menu.menuDescription
                                        val menuStock = menu.menuStock
                                        val menuImageUrl = menu.menuImageUrl
                                        if(dss.child(menuKey).exists()){
                                            temp.add(Menu(menuName, menuDescription, menuStock, menuKey, menuImageUrl))
                                        }
                                    }
                                    menuDataList.add(temp)
                                    if (menuDataList.size == restaurantDataList.size) {
                                        callback(restaurantDataList, menuDataList)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchCountOrder(position: Int, callback: (ArrayList<Int>) -> Unit){
        val myRef = Function().getDBRef("order")
        val countList: ArrayList<Int> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var childCount = 0
                for (ds in dataSnapshot.children) {
                    val restaurantKey: String? = ds.key
                    for (dss in ds.children) {
                        val userKey = dss.child("userKey").getValue(String::class.java)
                        if (userKey == Function().getCurrentUserKey() && restaurantKey != null) {
                            if (childCount == position) {
                                var i = 0
                                for(dsss in dss.children){
                                    i++
                                    if(dss.childrenCount.toInt()!=i){
                                        Log.d("count", dsss.getValue(Int::class.java).toString())
                                        dsss.getValue(Int::class.java)?.let { countList.add(it) }
                                    }
                                }
                                callback(countList)
                            }
                        }
                        childCount++
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchCountMerchantOrder(position: Int, callback: (ArrayList<Int>) -> Unit){
        val myRef = Function().getDBRef("order")
        val countList: ArrayList<Int> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val restaurantKey: String? = ds.key
                    var childCount = 0
                    for (dss in ds.children) {
                        if (restaurantKey != null && restaurantKey == Function().getCurrentUserKey()) {
                            if (childCount == position) {
                                var i = 0
                                for(dsss in dss.children){
                                    i++
                                    if(dss.childrenCount.toInt()!=i){
                                        dsss.getValue(Int::class.java)?.let { countList.add(it) }
                                    }
                                }
                                callback(countList)
                            }
                        }else{
                            childCount = 0
                        }
                        childCount++
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchOrderHistoryData(callback: (ArrayList<User>, ArrayList<ArrayList<Menu>>) -> Unit) {
        val myRef = Function().getDBRef("order")
        val userOrderDataList: ArrayList<User> = ArrayList()
        val menuDataList: ArrayList<ArrayList<Menu>> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val restaurantKey: String? = ds.key
                    for (dss in ds.children) {
                        val userKey = dss.child("userKey").getValue(String::class.java)
                        if (userKey != null && restaurantKey != null) {
                            fetchSearchUserData(userKey) { userDataList ->
                                for (userData in userDataList) {
                                    userOrderDataList.add(User(userData.email, userData.phone, userData.name, userData.role, userData.userKey, userData.imageDownloadUrl))
                                }
                                // Move this callback inside the fetchSearchUserData callback to ensure data consistency.
                                fetchMenuData(restaurantKey) { menuList ->
                                    val temp: ArrayList<Menu> = ArrayList()
                                    for (menu in menuList) {
                                        val menuKey = menu.menuKey
                                        val menuName = menu.menuName
                                        val menuDescription = menu.menuDescription
                                        val menuStock = menu.menuStock
                                        val menuImageUrl = menu.menuImageUrl
                                        if(dss.child(menuKey).exists()){
                                            temp.add(Menu(menuName, menuDescription, menuStock, menuKey, menuImageUrl))
                                        }
                                    }
                                    menuDataList.add(temp)
                                    if (menuDataList.size == userOrderDataList.size) {
                                        callback(userOrderDataList, menuDataList)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchOrderListData(callback: (ArrayList<User>, ArrayList<ArrayList<Menu>>, ArrayList<String>) -> Unit) {
        val myRef = Function().getDBRef("order")
        val userOrderDataList: ArrayList<User> = ArrayList()
        val menuDataList: ArrayList<ArrayList<Menu>> = ArrayList()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val restaurantKey: String? = ds.key
                    val orderList: ArrayList<String> = ArrayList()
                    for (dss in ds.children) {
                        dss.key?.let { orderList.add(it) }
                    }
                    for (dss in ds.children) {
                        val userKey = dss.child("userKey").getValue(String::class.java)
                        if (userKey != null && restaurantKey != null && restaurantKey.equals(Function().getCurrentUserKey())) {
                            fetchSearchUserData(userKey) { userDataList ->
                                for (userData in userDataList) {
                                    userOrderDataList.add(User(userData.email, userData.phone, userData.name, userData.role, userData.userKey, userData.imageDownloadUrl))
                                }
                                // Move this callback inside the fetchSearchUserData callback to ensure data consistency.
                                fetchMenuData(restaurantKey) { menuList ->
                                    val temp: ArrayList<Menu> = ArrayList()
                                    for (menu in menuList) {
                                        val menuKey = menu.menuKey
                                        val menuName = menu.menuName
                                        val menuDescription = menu.menuDescription
                                        val menuStock = menu.menuStock
                                        val menuImageUrl = menu.menuImageUrl
                                        if(dss.child(menuKey).exists()){
                                            temp.add(Menu(menuName, menuDescription, menuStock, menuKey, menuImageUrl))
                                        }
                                    }
                                    menuDataList.add(temp)
                                    if (menuDataList.size == userOrderDataList.size) {
                                        callback(userOrderDataList, menuDataList, orderList)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }

        myRef.addListenerForSingleValueEvent(valueEventListener)
    }

//    function to get current user information

    fun currentUser(): FirebaseUser? {
        val auth: FirebaseAuth = Firebase.auth
        return auth.currentUser
    }

    fun getCurrentUserKey(): String? {
        val user: FirebaseUser? = Function().currentUser()
        return user?.uid
    }

//    function to get current address by gps

    fun fetchLocation(context: Context, activity: Activity, callback: (latitude: Double, longitude: Double) -> Double) {
        var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(context,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                callback(it.latitude,it.longitude)
            }
        }
    }

//    function to delete
    fun deleteOrder(orderKey: String){
        val databaseReference = getDBRef("order")
        val nodeReference = databaseReference.child(getCurrentUserKey().toString()).child(orderKey)

        nodeReference.removeValue()
    }

}