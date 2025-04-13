package com.pranil9699.mylog

import android.os.Bundle
import android.widget.TextView

class RecommendationActivity : BaseActivity() {

    private lateinit var dosTextView: TextView
    private lateinit var dontsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ MUST be first to load base layout
        setContentView(R.layout.activity_base)

        // ✅ Inject your specific layout
        setContentLayout(R.layout.activity_recommendation)

        // ✅ Then setup navigation
        setupNavigation(R.id.nav_recommendation)

        // ✅ Now safe to access views
        dosTextView = findViewById(R.id.dosList)
        dontsTextView = findViewById(R.id.dontsList)

        val dos = listOf(
            "Write logs daily to track your mental state.",
            "Use emojis to express how you feel.",
            "Keep your logs honest and personal.",
            "Review your logs weekly for self-reflection."
        )

        val donts = listOf(
            "Don't share personal information publicly.",
            "Avoid skipping too many log entries.",
            "Don’t be too hard on yourself in the logs.",
            "Don't use offensive or harmful language."
        )

        dosTextView.text = dos.joinToString(separator = "\n• ", prefix = "• ")
        dontsTextView.text = donts.joinToString(separator = "\n• ", prefix = "• ")
    }
}
