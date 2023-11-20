package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentReadyLoginBinding
import com.google.firebase.auth.FirebaseAuth

//
class ReadyLoginFragment() : Fragment() {

    private lateinit var binding: FragmentReadyLoginBinding

    //NEU Objekt der FirebaseAuth erstellen
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadyLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//NEU Firebase Authentification ====================================================================
        //Instanz von der FirebaseAuth erstellen
        firebaseAuth = FirebaseAuth.getInstance()

        //Einloggen
        binding.cvForward.setOnClickListener {
            //Funkttion für den Login
            login()
        }

        //Registrieren
        binding.tvPleaseRegister.setOnClickListener {
            val navController = findNavController()
            navController.navigate(ReadyLoginFragmentDirections.actionReadyLoginFragmentToRegisterFragment())
        }
    }

//Funktion für den Login ===========================================================================
    private fun login() {
        val email = binding.textInputUserEmail.text.toString()
        val password = binding.textInputUserPassword.text.toString()
        val navController = findNavController()
        //Prüfung der Eingaben
        if (email.isNotEmpty() && password.isNotEmpty()) {
            //Einloggen mit Email und Passwort
            firebaseAuth.signInWithEmailAndPassword(email, password)
                //falls erfolgreich...
                .addOnCompleteListener {
                    //Wenn der Login erfolgreich war...
                    if (it.isSuccessful) {
                            //dann navigiere zu HomeFragment
                        navController.navigate(ReadyLoginFragmentDirections.actionReadyLoginFragmentToHomeFragment())
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT)
                .show()
        }
    }
}