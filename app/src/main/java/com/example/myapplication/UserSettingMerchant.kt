package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.UUID

class UserSettingMerchant : ComponentActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usersetting_merchant)

        init()
    }

    private fun init() {
        val name: TextView = findViewById(R.id.user_name_merchant)
        val email: TextView = findViewById(R.id.user_email_merchant)
        imageButton = findViewById(R.id.image_profile_merchant)

        Function().fetchSearchUserData(Function().getCurrentUserKey().toString()) { userDataList ->
            name.text = userDataList.getOrNull(0)?.name ?: "Unknown"
        }
        email.text = Function().currentUser()?.email

        // Fetch imageUrl from the database
        val currentUserKey = Function().getCurrentUserKey()
        Function().fetchDB("user/$currentUserKey/imageDownloadUrl") { imageUrl ->
            if (!imageUrl.isNullOrEmpty()) {
                // If imageUrl exists, load and display the image using Picasso
                Picasso.get().load(imageUrl).into(imageButton)
            }
        }

    }

    fun toHome(view: View){
        val intent = Intent(this, HomeMerchant::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun toHistory(view: View){
        val intent = Intent(this, OrderList::class.java)
        startActivity(intent)
        finish()
    }

    fun LogoutMerchant(view: View) {
        Firebase.auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
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
            imageButton.setImageBitmap(bitmap)
            uploadImageToFirebaseStorage(selectedImageUri)
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        // Create a reference to the image in Firebase Storage
        Log.d("test","1")
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
                    Picasso.get().load(imageUrl).into(imageButton)
                    Function().writeDB("user", Function().getCurrentUserKey()+"/imageDownloadUrl", imageUrl)
                }
            }
            .addOnFailureListener { exception ->
                // Image upload failed
                // Handle the error here
                Log.d("error", exception.toString())
            }
    }

    fun changeImageMerchant(view: View) {
        openGallery()
    }
}
