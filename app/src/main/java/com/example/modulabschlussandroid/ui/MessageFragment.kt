package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.databinding.FragmentMessageBinding
import com.example.modulabschlussandroid.databinding.ListItemMessageSenderBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    private val viewModel: ViewModelObjects by activityViewModels()

    private var advertismentId: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //die aus dem NavGraph angelegten Argumente werden übergeben
        arguments?.let {
            advertismentId = it.getString("advertismentId").toString()
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





        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }









}