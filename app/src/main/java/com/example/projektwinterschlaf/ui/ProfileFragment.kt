package com.example.projektwinterschlaf.ui
//==================================================================================================
//****************************        Profil Fragment         **************************************
//==================================================================================================

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.projektwinterschlaf.R
import com.example.projektwinterschlaf.adapters.AdapterProfile
import com.example.projektwinterschlaf.databinding.FragmentProfileBinding
import com.example.projektwinterschlaf.viewmodels.ViewModelObjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ViewModelObjects by activityViewModels()

//==================================================================================================
//onCreatedView=====================================================================================
//==================================================================================================

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

//==================================================================================================
//onViewCreated=====================================================================================
//==================================================================================================

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//Übergabe und Ermittlung des aktuellen User =======================================================

        //Update aller User Daten aus dem Firestore
        viewModel.updateCurrentUserFromFirestore()

        //Zeige die aktuellen Daten des eingeloggten Users mit LiveData
        val currentUser = viewModel.currentUser

//==================================================================================================
//Überwache den aktuellen User mit allen Daten aus der Datenbank Firestore==========================
//==================================================================================================
        currentUser.observe(viewLifecycleOwner) { user ->

            binding.tvLoggedUsername.text = user.userName
            binding.tvUserRealName.text = user.name
            binding.tvUserRealPreName.text = user.preName
            binding.tvUserTelNumber.text = user.telNumber
            binding.tvUserStreetName.text = user.streetName
            binding.tvUserStreetNumber.text = user.streetNumber
            binding.tvUserZipCode.text = user.zipCode
            binding.tvUserCity.text = user.cityName
            binding.tvUserItemsDone.text = "Meine bisherigen Inserate ${user.itemsDone}"
            Log.d("profil", "itemsDone ${user.itemsDone} ")
           //Profilfoto aus dem Storage laden
            Glide.with(requireContext()).load(user.profileImage)
                .placeholder(R.drawable.projekt_winterschlaf_logo).into(binding.ivProfileImage)
            //falls keins vorhanden ist nimm den Platzhalter
        }
//==================================================================================================
//Setzen des Adapter im Profil Fragment mit Anzeige meiner Inserte und Anzahl online================
//==================================================================================================

        val recView = binding.rvMyAdvertises
        recView.setHasFixedSize(true)

//Datenbank auslesen um die Zahl der Inserate des eingeloggten Users anzuzeigen=====================

        //Funktion für die Zahl aller Inserate des eingeloggten Users
        viewModel.checkDatabaseForMyAds()

        //Adapter setzten und mit LiveData überwachen
        viewModel.allMyAdvertises.observe(viewLifecycleOwner) {
            val adapter = AdapterProfile(it, viewModel)
            adapter.update(it)
            recView.adapter = adapter
            //Anzeige der Anzahl meiner online Inserate
            if (viewModel.allMyAdvertises.value?.size != null) {
                binding.tvCountMyObjects.text =
                    "online (${viewModel.allMyAdvertises.value?.size.toString()})"
            }
        }

//Live Data überwachen und die aktuelle Anzahl der Inserate anzeigen================================
        viewModel.countAdvertises()

        //Anzahl aller Anzeigen online mit LiveData
        val countAdvertises = viewModel.countAdvertises

        countAdvertises.observe(viewLifecycleOwner) {
            binding.tvUserCountInserted.text = it
        }

//==================================================================================================
//Navigation========================================================================================
//==================================================================================================

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

//==================================================================================================
//Logout des Users==================================================================================
//==================================================================================================

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
//==================================================================================================
//Ende=================================Ende===================================Ende==================
//==================================================================================================
