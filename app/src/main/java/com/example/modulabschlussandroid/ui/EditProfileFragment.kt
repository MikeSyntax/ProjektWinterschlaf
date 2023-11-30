package com.example.modulabschlussandroid.ui


import android.os.Bundle
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

    private lateinit var personalData: PersonalData

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
        personalData = PersonalData()

        //NEU Zeige die aktuelle Id des eingeloggten Users
        viewModel.showCurrentUserId()

//Übergabe und Ermittlung des aktuellen Users aus dem Firestore=====================================

        //Update aller User Daten aus dem Firestore
        viewModel.updateCurrentUserFromFirestore()

        //Zeige die aktuellen Daten des eingeloggten Users
        val currentUser = viewModel.currentUser

        //Objekt der User Id erstellen
        val uId = viewModel.uId

//Mit der Firebase Database Abfrage verbunden=======================================================

        //zum speichern der Userdaten btnSave klicken
        binding.btnSave.setOnClickListener {
            //Aktualisieren der UserId und anzeigen
            viewModel.showCurrentUserId()
            //alle Edittexte werden ausgelesen und als personal Data gespeichert und im Profil Fragment angezeigt
            personalData.userId = uId.value.toString()
            // Log.d("editProfile", "uId ${uId.value} personalData ${personalData.userId}")

            personalData.userName = binding.tvLoggedUsername.text.toString()
            personalData.preName = binding.tvPrename.text.toString()
            personalData.name = binding.tvName.text.toString()
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