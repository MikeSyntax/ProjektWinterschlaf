package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.adapters.AdapterFavorite
import com.example.modulabschlussandroid.databinding.FragmentFavoriteBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: ViewModelObjects by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allLikedObjects = viewModel.likedObjectsLive

        val recView = binding.rvFavoriteObjects

        recView.setHasFixedSize(true)

        //Überwachen der aktuellen Liste an gelikten Objekten und setzen des Adapters
        allLikedObjects.observe(viewLifecycleOwner){
            recView.adapter = AdapterFavorite(it,viewModel)

        }


        //Zum Home Screen navigieren
        binding.cvHome.setOnClickListener {
            val navController = binding.cvHome.findNavController()
            navController.navigate(FavoriteFragmentDirections.actionFavoriteFragmentToHomeFragment())

        }

        //zu den Favoriten navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToProfileFragment())
        }

        //Mit dem zurückButton zurück navigieren
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}