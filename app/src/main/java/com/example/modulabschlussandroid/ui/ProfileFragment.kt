package com.example.modulabschlussandroid.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.data.datamodels.MyObject
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentProfileBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ViewModelObjects by activityViewModels()

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var database: FirebaseDatabase

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

//NEU Update den aktuellen UserEmail
        viewModel.updateCurrentUserId()

//NEU Zeige die aktuelle Email des eingeloggten Users
        val currentUserId = viewModel.currentUserId.value

//Übergabe und Ermittlung des aktuellen Users aus dem Firestore=====================================

//NEU Update aller User Daten aus dem Firestore
        viewModel.updateUser(currentUserId.toString())

//NEU Zeige die aktuellen Daten des eingeloggten Users
        val currentUser = viewModel.currentUser

//NEU Überwache den aktuellen User mit allen Daten aus der Datenbank Firestore======================
        currentUser.observe(viewLifecycleOwner) { user ->
            binding.tvLoggedUsername.text = user.userName
            binding.tvUserRealName.text = user.name
            binding.tvUserRealPreName.text = user.preName
            binding.tvUserTelNumber.text = user.telNumber
            binding.tvUserStreetName.text = user.streetName
            binding.tvUserStreetNumber.text = user.streetNumber
            binding.tvUserZipCode.text = user.zipCode
            binding.tvUserCity.text = user.cityName
            binding.tvUserCountInserted.text =
                "Anzeigen online ${user.countInsertedItems.toString()}"
            binding.tvUserItemsDone.text = "Bisherige Anzeigen ${user.itemsDone.toString()}"
            binding.tvUserRegistered.text = "Registriert seit ${user.registered}"

//NEU nur zur Probe mit der Firebase Database Abfrage verbunden
            val objectID: String = "-NjHj3V8FzEZdpo8_jj2"
            readDatabase(objectID)
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

        //Ausloggen mit Wartevorgang und Toast Bestätigung bevor die Weiterleitung beginnt
        binding.cvLogout.setOnClickListener {
            //Initialisierung der firebaseAuth
            firebaseAuth = FirebaseAuth.getInstance()
            //Ausloggen
            firebaseAuth.signOut()
            //Anzeige das der Logout Vorgang erfolgreich war
            Toast.makeText(requireContext(), "Logout Successful", Toast.LENGTH_SHORT).show()
            //Übergabe der Zeitverzögerung
            viewLifecycleOwner.lifecycleScope.launch {
                delay(2000)
                //Weiterleitung zum einloggen
                val navController = findNavController()
                navController.navigate(ProfileFragmentDirections.actionProfileFragmentToReadyLoginFragment())
            }
        }
    }

    //NEU
    private fun readDatabase(objectID: String) {
        var count: Int = 0
        //Object von der Database bauen
        database = Firebase.database
        //und eine Reference setzten in der Kategorie myObjects
        val ref = database.getReference("objectsOnline")
        //Lies aus der objectsOnline alle Daten des Users mit der uId
        ref.child(objectID).get().addOnSuccessListener {
            if (it.exists()) {
                count++
                Log.d("Profile", "Reading successfully $it")
                val city = it.child("city").value
                val description = it.child("description").value
                val price = it.child("price").value
                val title = it.child("title").value
                val zipCode = it.child("zipCode").value
                binding.tvCity.text = city.toString()
                binding.tvPrice.text = price.toString()
                binding.tvObject.text = title.toString()
                binding.tvDistance.text = zipCode.toString()
                binding.tvDescription.text = description.toString()
                binding.tvUserCountInserted.text = "Anzeigen online ${count.toString()}"

            } else {
                Log.d("Profile", "This Id doesn´t exist")
            }
        }.addOnFailureListener {
            Log.d("Profile", "Data reading failed $it")
        }
    }
}