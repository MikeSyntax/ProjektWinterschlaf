package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentProfileBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ViewModelObjects by activityViewModels()

    // Erstellen einer Instanz userProfile aus der Klasse SecretData als LiveData
    private var userProfile =  PersonalData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        if (userProfile.loggedIn == true){
        binding.tvLoggedUsername.text = userProfile.userName
        } else {
            binding.tvLoggedUsername.text = "Ausgeloggt"
        }




        //zum HomeScreen navigieren
        binding.cvHome.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
        }

        //zum Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFavoriteFragment())
        }

        //zurück zur vorhergehenden Seite navigieren
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}