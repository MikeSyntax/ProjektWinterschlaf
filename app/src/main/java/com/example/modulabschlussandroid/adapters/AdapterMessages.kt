package com.example.modulabschlussandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.example.modulabschlussandroid.data.datamodels.chat.Message
import com.example.modulabschlussandroid.databinding.ListItemMessageReceiverBinding
import com.example.modulabschlussandroid.databinding.ListItemMessageSenderBinding

class AdapterMessages(

    private var dataset: List<Message>,

) : RecyclerView.Adapter<ViewHolder>() {

    class MessageSenderViewHolder(val binding: ListItemMessageSenderBinding) :ViewHolder(binding.root)

    class MessageReceiverViewHolder(val binding: ListItemMessageReceiverBinding) :ViewHolder(binding.root)

    //Entscheidung ob incoming(Receiver oder gesendet
    override fun getItemViewType(position: Int): Int {
        return if (dataset[position].incomingMessage) 1 else 0
    }

    //Größe des Datensatzes errechnen
    override fun getItemCount(): Int {
       return dataset.size
    }

    //Das entsprechende Item verwenden je nach dem ob Sender oder Receiver
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewBinding?
        return if (viewType == 1) {
            binding = ListItemMessageSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MessageSenderViewHolder(binding)
        } else {
            binding = ListItemMessageReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MessageReceiverViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thisMessage = dataset[position]
        if (holder is MessageReceiverViewHolder){
            holder.binding.tvMessageReceiver.text = thisMessage.message
            holder.binding.timeStampReceiver.text = thisMessage.timestamp.toString()
        } else if (holder is MessageSenderViewHolder){
            holder.binding.tvMessageTextSender.text = thisMessage.message
            holder.binding.timeStampSender.text = thisMessage.timestamp.toString()
        }
    }

    fun updateMessagesAdapter(list: List<Message>){
        dataset = list
        notifyDataSetChanged()
    }
}