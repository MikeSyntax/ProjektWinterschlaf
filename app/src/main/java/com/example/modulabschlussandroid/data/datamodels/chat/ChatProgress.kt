package com.example.modulabschlussandroid.data.datamodels.chat

import retrofit2.http.Url

data class ChatProgress(
    val documentId: String,
    val senderId: String,
    val receiverId: String,
    val imageAdvertisment: Url,
)