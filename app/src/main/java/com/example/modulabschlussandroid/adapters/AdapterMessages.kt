package com.example.modulabschlussandroid.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.modulabschlussandroid.data.datamodels.chat.Message
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class AdapterMessages(

    private var dataset: List<Message>,
    private var viewModel: ViewModelObjects

) : RecyclerView.Adapter<AdapterMessages.MessageViewHolder>() {
}