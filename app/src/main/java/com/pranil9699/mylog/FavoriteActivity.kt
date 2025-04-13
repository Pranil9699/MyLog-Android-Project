package com.pranil9699.mylog


import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoriteActivity : BaseActivity() {

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: LogAdapter
    private val favoriteList = mutableListOf<LogItem>()
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setContentLayout(R.layout.activity_favorite)

        setupNavigation(R.id.nav_favorite)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("logs").child(auth.currentUser!!.uid)

        favoriteRecyclerView = findViewById(R.id.logRecyclerView)
        favoriteRecyclerView.layoutManager = LinearLayoutManager(this)

        favoriteAdapter = LogAdapter(this, favoriteList) { /* Do nothing on double click here */ }
        favoriteRecyclerView.adapter = favoriteAdapter

        loadFavoriteLogs()
    }

    private fun loadFavoriteLogs() {
        db.orderByChild("favorite").equalTo(true).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteList.clear()
                for (data in snapshot.children.reversed()) {
                    val log = data.getValue(LogItem::class.java)
                    if (log != null) {
                        log.id = data.key ?: ""
                        favoriteList.add(log)
                    }
                }

                favoriteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FavoriteActivity, "Failed to load favorites", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
