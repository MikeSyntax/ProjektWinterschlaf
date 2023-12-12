package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.adapters.AdapterMessages
import com.example.modulabschlussandroid.adapters.AdapterProfile
import com.example.modulabschlussandroid.data.datamodels.chat.Message
import com.example.modulabschlussandroid.databinding.FragmentMessageBinding
import com.example.modulabschlussandroid.databinding.ListItemMessageSenderBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.Timestamp
import java.time.LocalDateTime

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    private val viewModel: ViewModelObjects by activityViewModels()

    private var advertismentId: String = "0"

    private lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //die aus dem NavGraph angelegten Argumente werden übergeben
        arguments?.let {
            advertismentId = it.getString("advertismentId").toString()
            Log.d("Message", "AdvertismentId $advertismentId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        message = Message()
        val uId = viewModel.uId

        binding.btnSend.setOnClickListener {
            message.incomingMessage = false
            message.advertisementId = advertismentId
            message.senderId = uId.value.toString()
            message.message = binding.textInputSender.text.toString()
            message.timestamp = Timestamp.now()
            viewModel.saveMessageToDatabase(message)
            binding.textInputSender.setText("")
        }

        //Setzen des Adapter im Message Fragment
        val recView = binding.rvMessages
        recView.setHasFixedSize(true)


        //Funktion Nachrichten bezüglich einer bestimmten Advertisement Id
        viewModel.checkMessages()

        //Adapter setzten und mit LiveData überwachen
        viewModel.myMessage.observe(viewLifecycleOwner) {

            val adapter = AdapterMessages(it)

            recView.adapter = adapter

            adapter.updateMessagesAdapter(it)

        }

        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}

