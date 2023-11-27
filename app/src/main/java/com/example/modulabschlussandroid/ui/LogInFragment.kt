package com.example.modulabschlussandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.databinding.FragmentLogInBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.auth.FirebaseAuth


class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val viewModel: ViewModelObjects by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMoreInfo.setOnClickListener {
            findNavController().navigate(R.id.infoFragment)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.readyLoginFragment)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        //Ist der aktuelle User noch eingeloggt?
        viewModel.currentAppUserLogged()
        //Prüfung ob ein User eingeloggt ist, falls ja dann erfolgt die Weiterleitung
        viewModel.currentAppUser.observe(viewLifecycleOwner) {
            if (it == "success") {
                val navController = findNavController()
                navController.navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
            }
        }
    }
}









/*
//Falls der User noch immer eingeloggte ist, wird direkt zum Homescreen weitergeleitet==============
    override fun onStart() {
        super.onStart()
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null){
            val navController = findNavController()
            navController.navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
        }
    }*/