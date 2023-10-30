package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.adapters.AdapterObjects
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.databinding.FragmentHomeBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ViewModelObjects by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val objectList = viewModel.objectList

        val recView = binding.rvRentableObjects

        recView.setHasFixedSize(true)

        //Überwachen aller aktuellen Objekte und setzen des Adapter mit Observer
        objectList.observe(viewLifecycleOwner) {
            //Parameter objectList(it) und ViewModel
            recView.adapter = AdapterObjects(it, viewModel)
        }

        //Zu den Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            val navController = binding.cvFavorite.findNavController()
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
        }

        //zu den Favoriten navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment2())
        }
    }
}
