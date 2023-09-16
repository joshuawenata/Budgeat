package com.example.myapplication

import android.content.ContentValues.TAG
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
import com.google.firebase.ktx.Firebase

class Function {

//    function to get DB Reference
    fun getDBRef(ref: String): DatabaseReference {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        return database.getReference(ref)
    }

    //    function for writing to database
    fun writeDB(ref:String, path: String, value: Integer){
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
        val userKey: String? = Function().currentUser()?.uid
        val userDataList = ArrayList<User>()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val uid = ds.key
                    if (userKey == uid){
                        val name = ds.child("name").getValue(String::class.java)
                        val email = ds.child("email").getValue(String::class.java)
                        val role = ds.child("role").getValue(String::class.java)

                        if (name != null && email != null && role != null) {
                            userDataList.add(User( email, name, role))
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


    fun fetchMenuData(callback: (ArrayList<Menu>) -> Unit) {
        val myRef = Function().getDBRef("menu")
        val userKey: String? = Function().currentUser()?.uid
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

                            if( menuName != null && menuDescription != null && menuStock != null && menuKey != null) {
                                menuList.add(Menu(menuName, menuDescription, menuStock, menuKey))
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



//    function to get current user information

    fun currentUser(): FirebaseUser? {
        val auth: FirebaseAuth = Firebase.auth
        return auth.currentUser
    }


}