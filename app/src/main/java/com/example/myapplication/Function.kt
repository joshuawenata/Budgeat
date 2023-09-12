package com.example.myapplication

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
}