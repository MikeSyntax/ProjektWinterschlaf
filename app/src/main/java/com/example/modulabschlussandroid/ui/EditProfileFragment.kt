package com.example.modulabschlussandroid.ui


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentEditProfileBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel: ViewModelObjects by activityViewModels()

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        binding.ivProfileImage.setImageURI(it)
        if (it != null) {
            viewModel.uploadImagetoStorage(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//Übergabe und Ermittlung des aktuellen Users aus dem Firestore=====================================

        //Update aller User Daten aus dem Firestore
        viewModel.updateCurrentUserFromFirestore()

        //Zeige die aktuellen Daten des eingeloggten Users mit LiveData
        val currentUser = viewModel.currentUser

//Überwache den aktuellen User mit allen Daten aus der Datenbank Firestore==========================
        currentUser.observe(viewLifecycleOwner) { user ->

            binding.tvLastLoggedUsername.text = user.userName
            binding.tvLastLoggedName.text = user.name
            binding.tvLastLoggedPrename.text = user.preName
            binding.tvLastLoggedPhoneNumber.text = user.telNumber
            binding.tvLastLoggedStreetname.text = user.streetName
            binding.tvLastLoggedStreetnumber.text = user.streetNumber
            binding.tvLastLoggedZipCode.text = user.zipCode
            binding.tvLastLoggedCity.text = user.cityName

            //Profilfoto aus dem Storage laden
            Glide.with(requireContext()).load(user.profileImage)
            //falls keins vorhanden ist nimm den Platzhalter
                .placeholder(R.drawable.projekt_winterschlaf_logo).into(binding.ivProfileImage)
        }

//Mit der Firebase Database Abfrage verbunden=======================================================

        //zum speichern der Userdaten btnSave klicken
        binding.btnSave.setOnClickListener {

            //erstellt ein Kopie des aktuellen Users
            val newUser = currentUser.value?.copy()

            if (newUser?.itemsDone == "null" || newUser?.itemsDone == null){
                newUser?.itemsDone = "0"
            }

            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvLoggedUsername.text.isNotEmpty()) {
                newUser?.userName = binding.tvLoggedUsername.text.toString()
            }

            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvPrename.text.isNotEmpty()) {
                newUser?.preName = binding.tvPrename.text.toString()
            }

            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvName.text.isNotEmpty()) {
                newUser?.name = binding.tvName.text.toString()
            }

            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvZipCode.text.isNotEmpty()) {
                newUser?.zipCode = binding.tvZipCode.text.toString()
            }
            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvCity.text.isNotEmpty()) {
                newUser?.cityName = binding.tvCity.text.toString()
            }

            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvStreetname.text.isNotEmpty()) {
                newUser?.streetName = binding.tvStreetname.text.toString()
            }

            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvStreetnumber.text.isNotEmpty()) {
                newUser?.streetNumber = binding.tvStreetnumber.text.toString()
            }

            //Wenn das Eingabefeld leer bleibt, dann übernimm die alte Angabe aus dem Last Feld in das Editfeld
            if (binding.tvPhoneNumber.text.isNotEmpty()) {
                newUser?.telNumber = binding.tvPhoneNumber.text.toString()
            }

            //Aufruf der Funktion zum speichern im Firestore aus dem FirebaseRepository
            viewModel.saveUserData(newUser!!)

            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
        }

        binding.ivProfileImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.tvChangeProfileImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}