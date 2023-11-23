package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.adapters.AdapterProfile
import com.example.modulabschlussandroid.databinding.FragmentProfileBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ViewModelObjects by activityViewModels()

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

//Übergabe und Ermittlung des aktuellen User =======================================================

        //Zeige die aktuelle Id des eingeloggten Users
        viewModel.showCurrentUserId()

        //Update aller User Daten aus dem Firestore
        viewModel.updateCurrentUserFromFirestore()

        //Zeige die aktuellen Daten des eingeloggten Users mit LiveData
        val currentUser = viewModel.currentUser

//Überwache den aktuellen User mit allen Daten aus der Datenbank Firestore==========================
        currentUser.observe(viewLifecycleOwner) { user ->
            viewModel.updateCurrentUserFromFirestore()

            binding.tvLoggedUsername.text = user.userName
            binding.tvUserRealName.text = user.name
            binding.tvUserRealPreName.text = user.preName
            binding.tvUserTelNumber.text = user.telNumber
            binding.tvUserStreetName.text = user.streetName
            binding.tvUserStreetNumber.text = user.streetNumber
            binding.tvUserZipCode.text = user.zipCode
            binding.tvUserCity.text = user.cityName
            binding.tvUserItemsDone.text = "Meine bisherigen Inserate ${user.itemsDone}"
        }

//Setzen des Adapter im Profil Fragment mit Anzeige meiner Inserte und Anzahl online================

        val recView = binding.rvMyAdvertises
        recView.setHasFixedSize(true)

        //Datenbank auslesen
        viewModel.readDatabase()

        //Adapter setzten und mit LiveData überwachen
        viewModel.allAdvertises.observe(viewLifecycleOwner) {
            val adapter = AdapterProfile(it)
            adapter.update(it)
            recView.adapter = adapter
            //Anzeige der Anzahl meiner online Inserate
            if (viewModel.allAdvertises.value?.size != null) {
                binding.tvCountMyObjects.text =
                    "online (${viewModel.allAdvertises.value?.size.toString()})"
            }
        }

//Live Data überwachen und die aktuelle Anzahl der Inserate anzeigen================================
        viewModel.countAdvertises()

        //Anzahl aller Anzeigen online mit LiveData
        val countAdvertises = viewModel.countAdvertises

        countAdvertises.observe(viewLifecycleOwner) {
            binding.tvUserCountInserted.text = it
        }
//Verschiedene Klicklistener========================================================================

        //zum HomeScreen navigieren
        binding.cvHome.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
        }

        //zum Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFavoriteFragment())
        }

        //zum inserieren navigieren
        binding.cvInsert.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToInsertFragment())
        }

        //zurück zur vorhergehenden Seite navigieren
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cvEdit.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }

        //Ausloggen mit Wartevorgang und Toast Bestätigung bevor die Weiterleitung beginnt
        binding.cvLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logout erfolgreich", Toast.LENGTH_SHORT).show()
            viewModel.signOutUser()
            //Übergabe der Zeitverzögerung
            viewLifecycleOwner.lifecycleScope.launch {
                delay(2000)
                //Weiterleitung zum einloggen
                val navController = findNavController()
                navController.navigate(ProfileFragmentDirections.actionProfileFragmentToReadyLoginFragment())
            }
        }
    }
}