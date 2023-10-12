package com.example.myapplication

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.Adapter
import com.example.myapplication.`object`.User
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class HomeCustomer : ComponentActivity() {
    private lateinit var userDataList: List<User>
    private lateinit var adapter: Adapter<User>
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var databaseReference: DatabaseReference
    private val searchHistoryKeywords = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_customer)

        // Initialize Firebase Analytics
        firebaseAnalytics = Firebase.analytics

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().reference

        // Fetch user data from your data source or Firebase Realtime Database
        Function().fetchRestaurantData { userDataList ->
            this.userDataList = userDataList

            sortByRecommendation()

            val searchEditText: EditText = findViewById(R.id.search_bar)
            val recyclerView = findViewById<RecyclerView>(R.id.home_recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            adapter = Adapter(
                this,
                userDataList,
                R.layout.card_restaurant,
                { itemView, item, position ->
                    val restaurantNameTextView =
                        itemView.findViewById<TextView>(R.id.card_restaurant_name)
                    val restaurantImage =
                        itemView.findViewById<ImageView>(R.id.image_restaurant)
                    restaurantNameTextView.text = item.name
                    Picasso.get().load(item.imageDownloadUrl).into(restaurantImage)
                    if (position % 2 == 0 && position != 0) {
                        val adView = itemView.findViewById<ImageView>(R.id.ad_view)
                        adView.visibility = View.VISIBLE
                    }
                },
                { item ->
                    val intent = Intent(this, MenuList::class.java)
                    intent.putExtra("userKey", item.userKey)
                    intent.putExtra("userName", item.name)
                    intent.putExtra("userAddress", item.address)
                    intent.putExtra("imageDownloadUrl", item.imageDownloadUrl)
                    startActivity(intent)
                }
            )

            recyclerView.adapter = adapter

            // Load search history keywords
            loadSearchHistoryKeywords()

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val filterText = s.toString().toLowerCase()
                    val filteredData = filterDataBySearchText(filterText)
                    adapter.filterList(filteredData)
                    adapter.notifyDataSetChanged()

                    // Call logSearchEvent with the search text
                    logSearchEvent(filterText)

                    // Call saveSearchHistoryToDatabase with the search text
                    saveSearchHistoryToDatabase(filterText)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun loadSearchHistoryKeywords() {
        // Retrieve the user's search history from Firebase Realtime Database
        // and extract keywords from the queries
        // You should implement this logic based on your database structure
        val userKey = Function().getCurrentUserKey()

        if (userKey != null) {
            // Example: Query the search history data
            val searchHistoryRef = databaseReference.child("search_history").child(userKey)
            searchHistoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (searchSnapshot in snapshot.children) {
                        val query = searchSnapshot.child("query").getValue(String::class.java)
                        query?.let {
                            // Split the query into keywords and add them to the list
                            val keywords = it.split(" ")
                            searchHistoryKeywords.addAll(keywords)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })
        }
    }

    private fun filterDataBySearchText(filterText: String): List<User> {
        // Combine the current search text and search history keywords
        val keywordsToMatch = (filterText.split(" ") + searchHistoryKeywords).distinct()

        // Filter the user data based on keywords
        return userDataList.filter { user ->
            val userKeywords = user.name.toLowerCase().split(" ")
            // Check if any of the keywords to match exist in the user's name
            keywordsToMatch.any { keyword ->
                userKeywords.any { userKeyword -> userKeyword.contains(keyword) }
            }
        }
    }

    private fun logSearchEvent(query: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }

    private fun saveSearchHistoryToDatabase(query: String) {
        // Get the user's unique ID (replace with your user key retrieval logic)
        val userKey = Function().getCurrentUserKey()

        // Generate a unique key for this search history entry
        val searchHistoryKey = databaseReference.child("search_history").push().key

        // Save the search history entry to the database
        searchHistoryKey?.let {
            val searchHistoryData = hashMapOf(
                "userKey" to userKey,
                "query" to query,
                "timestamp" to System.currentTimeMillis()
            )

            val searchHistoryPath = "search_history/$userKey/$it"
            databaseReference.child(searchHistoryPath).setValue(searchHistoryData)
        }
    }

    private fun sortByRecommendation() {
        val userKey = Function().getCurrentUserKey()

        if (userKey != null) {
            // Fetch the current user's search history
            val currentUserSearchHistoryRef = databaseReference.child("search_history").child(userKey)

            currentUserSearchHistoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(currentUserSnapshot: DataSnapshot) {
                    val currentUserSearchQueries = mutableListOf<String>()

                    for (searchSnapshot in currentUserSnapshot.children) {
                        val query = searchSnapshot.child("query").getValue(String::class.java)
                        query?.let {
                            currentUserSearchQueries.add(it)
                        }
                    }

                    // Sort restaurants based on similarity to the current user's search history
                    val sortedRestaurants = userDataList.sortedBy { restaurant ->
                        val restaurantKeywords = restaurant.name.toLowerCase().split(" ")
                        val similarity = calculateSearchHistorySimilarity(currentUserSearchQueries, restaurantKeywords)

                        // Sort by similarity (descending order)
                        -similarity
                    }

                    // Update the adapter with the sorted data
                    adapter.filterList(sortedRestaurants)
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })
        }
    }

    private fun calculateSearchHistorySimilarity(userQueries: List<String>, restaurantKeywords: List<String>): Int {
        // This is a simplified similarity calculation; you can use more advanced algorithms as needed
        var similarity = 0

        for (userQuery in userQueries) {
            for (keyword in restaurantKeywords) {
                if (userQuery.contains(keyword, ignoreCase = true)) {
                    similarity++
                }
            }
        }

        return similarity
    }

    fun sortByLoc(view: View) {
        // The target latitude and longitude you want to filter by
        Function().getCurrentUserKey()?.let { currentUserKey ->
            Function().fetchSearchUserData(currentUserKey) { users ->
                if (users.isNotEmpty()) {
                    val targetLatitude = users[0].latitude // Replace with your target latitude
                    val targetLongitude = users[0].longitude // Replace with your target longitude

                    // The maximum allowed distance in meters
                    val maxDistance = 1000.0 // Adjust this value as needed

                    val sortedData = userDataList.sortedBy { restaurant ->
                        val restaurantLatitude = restaurant.latitude
                        val restaurantLongitude = restaurant.longitude

                        // Calculate the distance between the restaurant and the target location
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            targetLatitude.toDouble(), targetLongitude.toDouble(),
                            restaurantLatitude.toDouble(), restaurantLongitude.toDouble(),
                            results
                        )

                        // Sort by distance (ascending order)
                        results[0]
                    }

                    adapter.filterList(sortedData)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun toHistory(view: View) {
        val intent = Intent(this, HistoryCustomer::class.java)
        startActivity(intent)
    }

    fun toUserSetting(view: View) {
        val intent = Intent(this, UserSetting::class.java)
        startActivity(intent)
    }

    fun toAi(view: View) {
        val intent = Intent(this, AiRecommendation::class.java)
        startActivity(intent)
    }
}
