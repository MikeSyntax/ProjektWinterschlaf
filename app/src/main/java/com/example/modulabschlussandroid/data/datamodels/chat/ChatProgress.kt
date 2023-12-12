package com.example.modulabschlussandroid.data.datamodels.chat

import retrofit2.http.Url

data class ChatProgress(
    val advertisementId: String,
    val senderId: String,
    val receiverId: String,
    val imageAdvertisment: Url,
)