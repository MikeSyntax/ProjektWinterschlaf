package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.adapters.AdapterObjects
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

        val recView = binding.rvRentableObjects
        recView.setHasFixedSize(true)

        viewModel.loadFromDatabase()

        //Setzen des Adapter mit Observer
        viewModel.objectListLive.observe(viewLifecycleOwner) {
            //Parameter objectList(it) und ViewModel
            recView.adapter = AdapterObjects(it)
        }

    }
}