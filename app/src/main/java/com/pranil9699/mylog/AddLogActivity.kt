package com.pranil9699.mylog

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddLogActivity : AppCompatActivity() {

//    private var editTextLog: EditText? = null  // ❌ Not needed here
    private lateinit var editTextLog: EditText
    private lateinit var emojiSpinner: Spinner
    private lateinit var btnSaveLog: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private val emojiList = listOf("😊", "😔", "🔥", "😎", "💡", "🥹", "🤯", "❤️", "🚀")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_log)

        editTextLog = findViewById(R.id.editTextLog)
        emojiSpinner = findViewById(R.id.emojiSpinner)
        btnSaveLog = findViewById(R.id.btnSaveLog)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("logs").child(auth.currentUser!!.uid)

        // Emoji Dropdown
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, emojiList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        emojiSpinner.adapter = adapter

        btnSaveLog.setOnClickListener {
            val logText = editTextLog.text.toString()
            val selectedEmoji = emojiSpinner.selectedItem.toString()

            if (logText.isEmpty()) {
                Toast.makeText(this, "Please write something...", Toast.LENGTH_SHORT).show()
            } else {
                val logId = dbRef.push().key!!
                val log = LogItem(logText, selectedEmoji, System.currentTimeMillis(), false, logId)
                dbRef.child(logId).setValue(log).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Log saved!", Toast.LENGTH_SHORT).show()
                        finish() // Go back to home
                    } else {
                        Toast.makeText(this, "Failed to save log", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
