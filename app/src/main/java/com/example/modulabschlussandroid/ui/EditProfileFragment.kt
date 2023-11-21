package com.example.modulabschlussandroid.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentEditProfileBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel: ViewModelObjects by activityViewModels()

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

        //Objekt der personal Data erstellen
        val personalData = PersonalData()

        //Objekt der User Id erstellen
        val uId = viewModel.uId

        //zum speichern der Userdaten btnSave klicken
        binding.btnSave.setOnClickListener {
            viewModel.showCurrentUserId()
            //alle Edittexte werden ausgelesen und als personal Data gespeichert und im Profil Fragment angezeigt
            personalData.userId = uId.value.toString()
           // Log.d("editProfile", "uId ${uId.value} personalData ${personalData.userId}")
            personalData.name = binding.tvName.text.toString()
            personalData.preName = binding.tvPrename.text.toString()
            personalData.userName = binding.tvLoggedUsername.text.toString()
            personalData.zipCode = binding.tvZipCode.text.toString()
            personalData.cityName = binding.tvCity.text.toString()
            personalData.streetName = binding.tvStreetname.text.toString()
            personalData.streetNumber = binding.tvStreetnumber.text.toString()
            personalData.telNumber = binding.tvPhoneNumber.text.toString()
            //Aufruf der Funktion zum speichern aus dem FirebaseRepository
            viewModel.newUserDataFirstSignIn(personalData)
            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
        }

        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}