package com.example.projektwinterschlaf.ui
//==================================================================================================
//****************************       Message Fragment         **************************************
//==================================================================================================

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.projektwinterschlaf.adapters.AdapterMessages
import com.example.projektwinterschlaf.data.datamodels.chat.Message
import com.example.projektwinterschlaf.databinding.FragmentMessageBinding
import com.example.projektwinterschlaf.viewmodels.ViewModelObjects
import com.google.firebase.Timestamp

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    private lateinit var message: Message

    private val viewModel: ViewModelObjects by activityViewModels()

    private var advertisementId: String = "0"

//==================================================================================================
//onCreate==========================================================================================
//==================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //die aus dem NavGraph angelegten Argumente werden übergeben
        arguments?.let {
            advertisementId = it.getString("advertisementId").toString()
            Log.d("Message", "AdvertisementId $advertisementId")
        }
    }

//==================================================================================================
//onCreatedView=====================================================================================
//==================================================================================================

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

//==================================================================================================
//onViewCreated=====================================================================================
//==================================================================================================

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//==================================================================================================
//Nachricht eingeben und absenden===================================================================
//==================================================================================================

        binding.btnSend.setOnClickListener {
            message = Message()
            val uId = viewModel.uId
            //wenn die Id des aktuellen Users die gleiche wie die uId ist,
            // dann ist es der Verfasser des Inserats und incoming muss auf false stehen
            if (viewModel.currentUser.value?.userId == uId.value) {
                message.incomingMessage = false
                message.advertisementId = advertisementId
                message.senderId = uId.value.toString()
                message.message = binding.textInputSender.text.toString()
                message.timestamp = Timestamp.now()
                //Speicherung erfolgt in Firebase
                viewModel.saveMessageToDatabase(message)
                binding.textInputSender.setText("")
                viewModel.checkMessages()
            } else {
                message.incomingMessage = true
                message.advertisementId = advertisementId
                message.senderId = uId.value.toString()
                message.message = binding.textInputSender.text.toString()
                message.timestamp = Timestamp.now()
                //Speicherung erfolgt in Firebase
                viewModel.saveMessageToDatabase(message)
                binding.textInputSender.setText("")
                viewModel.checkMessages()
            }
        }

//==================================================================================================
//Adapter setzen====================================================================================
//==================================================================================================

        //Setzen des Adapter im Message Fragment
        val recView = binding.rvMessages
        recView.setHasFixedSize(true)

        //Funktion Nachrichten bezüglich einer bestimmten Advertisement Id
        viewModel.checkMessages()

        //Adapter setzten und mit LiveData überwachen
        viewModel.myMessage.observe(viewLifecycleOwner) {

            //Sortierung der Liste über den Zeitstempel
            val sortedList = it.sortedBy { Timestamp.now() }
            //Setzen des Adapters
            val adapter = AdapterMessages(sortedList, viewModel)
            recView.adapter = adapter
            //Udaten der Message Liste
            adapter.updateMessagesAdapter(it)
        }

        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
//==================================================================================================
//Ende===============================Ende=======================================Ende================
//==================================================================================================

