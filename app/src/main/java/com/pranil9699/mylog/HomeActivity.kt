package com.pranil9699.mylog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : BaseActivity() {

    private lateinit var logRecyclerView: RecyclerView
    private lateinit var logAdapter: LogAdapter
    private val logList = mutableListOf<LogItem>()
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base) // Base layout with toolbar + bottom nav
        setContentLayout(R.layout.activity_home) // Inject actual content into content_frame

        setupNavigation(R.id.nav_log) // This sets up both toolbar + bottom nav

        // Show floating action button for adding logs
       // findViewById<FloatingActionButton>(R.id.fab_add_log).visibility = View.VISIBLE

        // Firebase setup
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("logs").child(auth.currentUser!!.uid)

        // Setup RecyclerView
        logRecyclerView = findViewById(R.id.logRecyclerView)
        logRecyclerView.layoutManager = LinearLayoutManager(this)
        logAdapter = LogAdapter(this, logList) { logItem ->
            val logRef = db.child(logItem.id)
            val newStatus = !logItem.favorite
            logRef.child("favorite").setValue(newStatus).addOnSuccessListener {
                val msg = if (newStatus) "Added to Favorite ⭐" else "Removed from Favorite ❌"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        logRecyclerView.adapter = logAdapter

        // Add Log FAB click
        findViewById<View>(R.id.fab_add_log).setOnClickListener {
            startActivity(Intent(this, AddLogActivity::class.java))
        }

        loadLogsFromFirebase()
    }



    private fun loadLogsFromFirebase() {
        db.orderByKey().limitToLast(100).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                logList.clear()
                for (data in snapshot.children.reversed()) {
                    val log = data.getValue(LogItem::class.java)
                    if (log != null) {
                        log.id = data.key ?: ""
                        logList.add(log)
                    }
                }
                logAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Error loading logs", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
