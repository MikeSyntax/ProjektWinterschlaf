package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.databinding.FragmentReadyLoginBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

//
class ReadyLoginFragment() : Fragment() {

    private lateinit var binding: FragmentReadyLoginBinding

    private val viewModel: ViewModelObjects by activityViewModels()

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

        //Einloggen
        binding.cvForward.setOnClickListener {
            val email = binding.textInputUserEmail.text.toString()
            val password = binding.textInputUserPassword.text.toString()
            //Funkttion für den Login kommt aus dem RepoFirebase
            viewModel.login(email, password, requireContext()).addOnSuccessListener {
                val navController = findNavController()
                navController.navigate(ReadyLoginFragmentDirections.actionReadyLoginFragmentToHomeFragment())
            }
        }

        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //Registrieren
        binding.tvPleaseRegister.setOnClickListener {
            val navController = findNavController()
            navController.navigate(ReadyLoginFragmentDirections.actionReadyLoginFragmentToRegisterFragment())
        }
    }
}