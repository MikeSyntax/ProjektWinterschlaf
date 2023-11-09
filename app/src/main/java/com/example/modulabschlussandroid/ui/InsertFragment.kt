package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.databinding.FragmentInsertBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class InsertFragment : Fragment() {

    private lateinit var binding: FragmentInsertBinding
    private val viewModel: ViewModelObjects by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.descriptionArrow.setOnClickListener {
            binding.cvPopUpCategories.isVisible = true
        }

        binding.btnSaveInCvDescription.setOnClickListener {
            binding.cvPopUpCategories.isVisible = false
        }


        //Zu den Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            val navController = binding.cvFavorite.findNavController()
            navController.navigate(InsertFragmentDirections.actionInsertFragmentToFavoriteFragment())
        }

        //zu den Profil navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToProfileFragment())
        }

        //zum inserieren navigieren
        binding.cvHome.setOnClickListener {
            findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToHomeFragment())
        }

        //zum inserieren navigieren
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}