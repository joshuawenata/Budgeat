package com.example.myapplication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.UUID

class AddMenu : ComponentActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private lateinit var imageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu)

        init()
    }

    private fun init() {
        imageView = findViewById(R.id.image_menu_add)
    }

    fun addMenuDB(view: View) {
        val menuNameEditText: EditText = findViewById(R.id.add_menu_name)
        val menuDescriptionEditText: EditText = findViewById(R.id.add_menu_description)
        val menuStockEditText: EditText = findViewById(R.id.add_menu_stock)
        val menuPriceEditText: EditText = findViewById(R.id.add_menu_price)

        val menuName = menuNameEditText.text.toString()
        val menuDescription = menuDescriptionEditText.text.toString()
        val menuStock = menuStockEditText.text.toString()
        val menuPrice = menuPriceEditText.text.toString()

        var flag = true

        if (menuName.isEmpty()) {
            menuNameEditText.error = "Please fill the menu name field"
            flag = false
        }else if (menuDescription.isEmpty()) {
            menuDescriptionEditText.error = "Please fill the menu description field"
            flag = false
        }else if (menuStock.isEmpty()) {
            menuStockEditText.error = "Please fill the menu stock field"
            flag = false
        }else if (menuPrice.isEmpty()) {
            menuStockEditText.error = "Please fill the menu price field"
            flag = false
        }else if (!::imageUrl.isInitialized){
            Toast.makeText(this, "image still loading...", Toast.LENGTH_SHORT).show()
            flag = false
        }else{
            flag = true
        }

        if(flag){
            val myRef = Function().getDBRef("user")
            val menuKey: String = myRef.push().key.toString()
            val userKey: String? = Function().getCurrentUserKey()

            Function().writeDB("menu", "$userKey/$menuKey/menuName",menuName)
            Function().writeDB("menu", "$userKey/$menuKey/menuDescription",menuDescription)
            Function().writeDB("menu", "$userKey/$menuKey/menuStock",menuStock)
            Function().writeDB("menu", "$userKey/$menuKey/menuPrice",menuPrice)
            Function().writeDB("menu", "$userKey/$menuKey/menuKey",menuKey)
            Function().writeDB("menu", "$userKey/$menuKey/menuImageUrl",imageUrl)

            val intent = Intent(this, HomeMerchant::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!

            // Load and display the selected image
            val inputStream = contentResolver.openInputStream(selectedImageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
            uploadImageToFirebaseStorage(selectedImageUri)
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        // Create a reference to the image in Firebase Storage
        val storage = Firebase.storage("gs://budgeat-25e02.appspot.com")
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        // Upload the image
        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image upload successful
                // You can get the download URL of the uploaded image like this:
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    // Do something with the image URL, like saving it to a database or displaying it
                    Picasso.get().load(imageUrl).into(imageView)
                    this.imageUrl = uri.toString()
                }
            }
            .addOnFailureListener { exception ->
                // Image upload failed
                // Handle the error here
                Log.d("error", exception.toString())
            }
    }

    fun addImage(view: View) {
        openGallery()
    }
}
