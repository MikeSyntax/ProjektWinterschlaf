package com.example.modulabschlussandroid.data.datamodels.chat

import com.google.firebase.Timestamp

data class Message(
    val senderId: String,
    val timestamp: Timestamp,
    val message: String,

)