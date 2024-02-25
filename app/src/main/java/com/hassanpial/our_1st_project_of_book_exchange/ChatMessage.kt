package com.hassanpial.our_1st_project_of_book_exchange

data class ChatMessage(
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0
)
