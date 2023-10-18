package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.databinding.FragmentReadyLoginBinding
import com.example.modulabschlussandroid.secret.SecretData


//
class ReadyLoginFragment() : Fragment() {

    private lateinit var binding: FragmentReadyLoginBinding

    // Erstellen einer Instanz userProfile aus der Klasse SecretData
    private val userProfile = SecretData()

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

        //Zurück Button
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //Vorwärts Button
        binding.cvForward.setOnClickListener {
            //Initialisieren neuer Variablen mit dem Text, welcher über die Tastatur in die Felder eingegeben wird
            val user = binding.textInputUserAvatar.text.toString()
            val authentification = binding.textInputUserPassword.text.toString()
            val userEmail = binding.textInputUserEmail.text.toString()

            //When Bedingung erfüllt dann...
            when {
                (userProfile.userName == user && userProfile.password == authentification) -> {
                    findNavController().navigate(R.id.homeFragment)
                }

               //When Bedingung erfüllt dann...
                userProfile.email == userEmail && userProfile.password == authentification -> {
                    findNavController().navigate(R.id.homeFragment)
                }

                //When Bedingung nicht erfüllt dann
                userProfile.userName != user -> {
                    exceptionMessage("Leider falschen Benutzernamen eingegeben")
                    return@setOnClickListener
                }

                //When Bedingung nicht erfüllt dann
                userProfile.password != authentification -> {
                    exceptionMessage("Leider falsches Passwort eingegeben")
                    return@setOnClickListener
                }

                //When Bedingung nicht erfüllt dann
                userProfile.email != userEmail -> {
                    exceptionMessage("Leider falsche Email eingegeben")
                    return@setOnClickListener
                }

                //When alles schiefgeht dann
                else -> exceptionMessage("Upps, das sollte nicht passieren")
            }
        }
    }

    //Funktion zur Ausgabe eines Toast
    private fun exceptionMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        Toast.makeText(requireContext(), message, duration).show()
    }
}