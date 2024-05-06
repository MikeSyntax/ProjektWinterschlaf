package com.example.projektwinterschlaf.data.datamodels.chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class Message(
    var senderId: String = "",
    var incomingMessage: Boolean = true,
    var timestamp: Timestamp = Timestamp.now(),
    var message: String = "",
    var advertisementId: String = "",

    ){
    constructor(dataSnapshot: DocumentSnapshot) : this() {
        senderId = dataSnapshot["senderId"].toString()
        incomingMessage = dataSnapshot["incomingMessage"] as Boolean
        timestamp = dataSnapshot["timestamp"] as Timestamp
        message = dataSnapshot["message"].toString()
        advertisementId = dataSnapshot["advertisementId"].toString()

    }
}