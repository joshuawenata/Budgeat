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
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class UserSettingMerchant : ComponentActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageButton: CircleImageView
    private var isImageLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usersetting_merchant)

        init()
    }

    private fun init() {
        val name: TextView = findViewById(R.id.user_name_merchant)
        val email: TextView = findViewById(R.id.user_email_merchant)
        val phone: TextView = findViewById(R.id.user_phone_merchant)
        val address: TextView = findViewById(R.id.user_address_merchant)
        imageButton = findViewById(R.id.image_profile_merchant)

        Function().fetchSearchUserData(Function().getCurrentUserKey().toString()) { userDataList ->
            name.text = userDataList.getOrNull(0)?.name ?: "Unknown"
            phone.text = userDataList.getOrNull(0)?.phone ?: "Unknown"
            address.text = userDataList.getOrNull(0)?.address ?: "Unknown"
        }
        email.text = Function().currentUser()?.email

        val currentUserKey = Function().getCurrentUserKey()
        Function().fetchDB("user/$currentUserKey/imageDownloadUrl") { imageUrl ->
            if (!imageUrl.isNullOrEmpty()) {
                isImageLoading = true
                Picasso.get().load(imageUrl).into(imageButton, object : Callback {
                    override fun onSuccess() {
                        isImageLoading = false
                    }

                    override fun onError(e: Exception?) {
                        isImageLoading = false
                    }
                })
            }
        }
    }

    fun toHome(view: View) {
        if (isImageLoading) {
            // Image is still loading, don't proceed
            // You can display a toast or show a message to inform the user
            // In this example, we are just logging the message
            Log.d("UserSettingMerchant", "Image is still loading. Please wait.")
        } else {
            val intent = Intent(this, HomeMerchant::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    fun toHistory(view: View) {
        if (isImageLoading) {
            // Image is still loading, don't proceed
            // You can display a toast or show a message to inform the user
            Log.d("UserSettingMerchant", "Image is still loading. Please wait.")
        } else {
            val intent = Intent(this, OrderList::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun LogoutMerchant(view: View) {
        if (isImageLoading) {
            // Image is still loading, don't proceed
            Log.d("UserSettingMerchant", "Image is still loading. Please wait.")
        } else {
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun openGallery() {
        if (isImageLoading) {
            // Image is still loading, don't proceed
            Log.d("UserSettingMerchant", "Image is still loading. Please wait.")
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
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
        val storage = Firebase.storage("gs://budgeat-25e02.appspot.com")
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    Picasso.get().load(imageUrl).into(imageButton)
                    Function().writeDB("user", Function().getCurrentUserKey() + "/imageDownloadUrl", imageUrl)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("UserSettingMerchant", "Image upload failed. Error: ${exception.message}")
            }
    }

    fun changeImageMerchant(view: View) {
        openGallery()
    }

    fun fetchLoc(view: View) {
        Function().fetchLocation(applicationContext, this) { addressLine ->
            val address: TextView = findViewById(R.id.user_address_merchant)
            address.text = addressLine
            val userKey = Function().getCurrentUserKey()
            Function().writeDB("user", "$userKey/address", addressLine)
        }
    }

    fun Support(view: View) {
        if (isImageLoading) {
            Log.d("UserSettingMerchant", "Image is still loading. Please wait.")
        } else {
            val intent = Intent(this, Support::class.java)
            startActivity(intent)
            finish()
        }
    }
}
