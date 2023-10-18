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

        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cvForward.setOnClickListener {
            val user = binding.textInputUserAvatar.text.toString()
            val authentification = binding.textInputUserPassword.text.toString()
            val userEmail = binding.textInputUserEmail.text.toString()
            if ((userProfile.userName == user



                        && userProfile.password == authentification) || (userProfile.email == userEmail && userProfile.password == authentification)) {
                findNavController().navigate(R.id.homeFragment)
            } else {
                when {
                    userProfile.userName != user -> exceptionMessage("Leider falschen Benutzernamen eingegeben")
                    userProfile.password != authentification -> exceptionMessage("Leider falsches Passwort eingegeben")
                    userProfile.email != userEmail -> exceptionMessage("Leider falsche Email eingegeben")
                    else -> exceptionMessage("Da ist etwas schiefgegangen")
                }
            }
        }
    }

    private fun exceptionMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        Toast.makeText(requireContext(), message, duration).show()
    }

}