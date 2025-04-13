package com.pranil9699.mylog

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditLogActivity : AppCompatActivity() {

    private lateinit var editTextLog: EditText
    private lateinit var emojiSpinner: Spinner
    private lateinit var btnUpdateLog: Button

    private val emojiList = listOf("😊", "😔", "🔥", "😎", "💡", "🥹", "🤯", "❤️", "🚀")

    private lateinit var logId: String
    private lateinit var currentText: String
    private lateinit var currentEmoji: String
    private var currentTimestamp: Long = 0L // Store the original timestamp
    private var currentFavorite: Boolean = false // Store the original favorite status
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_log)

        editTextLog = findViewById(R.id.editTextLog)
        emojiSpinner = findViewById(R.id.emojiSpinner)
        btnUpdateLog = findViewById(R.id.btnUpdateLog)

        // Get data passed from intent
        logId = intent.getStringExtra("logId") ?: ""
        currentText = intent.getStringExtra("logText") ?: ""
        currentEmoji = intent.getStringExtra("emoji") ?: ""
        currentTimestamp = intent.getLongExtra("timestamp", 0L) // Get the original timestamp
        currentFavorite = intent.getBooleanExtra("favorite", false) // Get the original favorite status

        editTextLog.setText(currentText)

        // Setup Emoji Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, emojiList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        emojiSpinner.adapter = adapter

        val selectedIndex = emojiList.indexOf(currentEmoji)
        if (selectedIndex != -1) {
            emojiSpinner.setSelection(selectedIndex)
        }

        btnUpdateLog.setOnClickListener {
            val updatedText = editTextLog.text.toString().trim()
            val updatedEmoji = emojiSpinner.selectedItem.toString()

            if (updatedText.isEmpty()) {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val dbRef = FirebaseDatabase.getInstance().getReference("logs").child(userId).child(logId)

            val updatedLog = LogItem(updatedText, updatedEmoji, currentTimestamp,currentFavorite,logId)

            dbRef.setValue(updatedLog).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Log updated!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update log", Toast.LENGTH_SHORT).show()
                }
            }


        }
        val btnDeleteLog = findViewById<Button>(R.id.btnDeleteLog)

        btnDeleteLog.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val dbRef = FirebaseDatabase.getInstance().getReference("logs").child(userId).child(logId)

            dbRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Log deleted!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to delete log", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
