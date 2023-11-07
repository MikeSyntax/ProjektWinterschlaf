package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentProfileBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ViewModelObjects by activityViewModels()

    private lateinit var firebaseAuth: FirebaseAuth

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

//Übergabe und Ermittlung des aktuellen Users=======================================================

//NEU Update den aktuellen User
        viewModel.updateCurrentUser()

//NEU Zeige die aktuelle Email des eingeloggten Users
        val currentUserEmail = viewModel.currentUserEmail.value

//NEU Überwache den aktuellen User
        viewModel.currentUserEmail.observe(viewLifecycleOwner){
        //Zeige falls eingeloggt den Usernamen
        binding.tvLoggedUsername.text = currentUserEmail
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

        //zurück zur vorhergehenden Seite navigieren
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //Ausloggen mit Wartevorgang und Toast Bestätigung bevor die Weiterleitung beginnt
        binding.cvLogout.setOnClickListener {
            //Initialisierung der firebaseAuth
            firebaseAuth = FirebaseAuth.getInstance()
            //Ausloggen
            firebaseAuth.signOut()
            //Anzeige das der Logout Vorgang erfolgreich war
            Toast.makeText(requireContext(),"Logout Successful", Toast.LENGTH_SHORT).show()
            //Übergabe der Zeitverzögerung
            viewLifecycleOwner.lifecycleScope.launch{
                delay(2000)
            //Weiterleitung zum einloggen
            val navController = findNavController()
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToReadyLoginFragment())
            }
        }
    }
}