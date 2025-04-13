package com.pranil9699.mylog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : BaseActivity() {


    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var textPhone: TextView
    private lateinit var btnLogout: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_base) // Base layout with toolbar + bottom nav
        setContentLayout(R.layout.activity_profile)

//        setupNavigation(R.id.nav_profile)
        setupNavigation(R.id.nav_profile)

        textName = findViewById(R.id.TextName)
        textEmail = findViewById(R.id.TextEmail)
        textPhone = findViewById(R.id.TextPhone)
        btnLogout = findViewById(R.id.btnLogout2)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(userId!!)

        // Load user info
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value.toString()
                val email = snapshot.child("email").value.toString()
                val phone = snapshot.child("phone").value.toString()

                textName.text = name
                textEmail.text = email
                textPhone.text = phone
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        })

        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
}
