package com.pranil9699.mylog

data class LogItem(
    var text: String = "",
    var emoji: String = "",
    var timestamp: Long = 0L,
    var favorite: Boolean = false,
    var id: String = "" // This is optional but helpful
)


