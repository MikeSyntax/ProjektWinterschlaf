package com.example.modulabschlussandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.example.modulabschlussandroid.data.datamodels.chat.Message
import com.example.modulabschlussandroid.databinding.ListItemMessageReceiverBinding
import com.example.modulabschlussandroid.databinding.ListItemMessageSenderBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class AdapterMessages(

    private var dataset: List<Message>,
    private var viewModel: ViewModelObjects

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

    //Bau der ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewBinding?
    //Das entsprechende Item verwenden je nach dem ob Sender oder Receiver
        return if (viewType == 1) {
            binding = ListItemMessageSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MessageSenderViewHolder(binding)
        } else {
            binding = ListItemMessageReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MessageReceiverViewHolder(binding)
        }
    }

    //Setzen der ViewHolder bzw. befüllen
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thisMessage = dataset[position]
        if (holder is MessageReceiverViewHolder){
            holder.binding.tvMessageReceiver.text = thisMessage.message
            holder.binding.timeStampReceiver.text = viewModel.convertTimestampToDateTime(thisMessage.timestamp)
        } else if (holder is MessageSenderViewHolder){
            holder.binding.tvMessageTextSender.text = thisMessage.message
            holder.binding.timeStampSender.text = viewModel.convertTimestampToDateTime(thisMessage.timestamp)
        }
    }

    //Update nachdem sich die Liste verändert hat
    fun updateMessagesAdapter(list: List<Message>){
        dataset = list
        notifyDataSetChanged()
    }
}