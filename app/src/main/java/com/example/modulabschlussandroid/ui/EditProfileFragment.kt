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

    private lateinit var personalData: PersonalData

    private var imageUri: Uri? = null

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        binding.ivProfileImage.setImageURI(it)
        if (it != null) {
            imageUri = it.toString().toUri()
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

        //Objekt der personal Data erstellen
        personalData = PersonalData()

        //NEU Zeige die aktuelle Id des eingeloggten Users
        viewModel.showCurrentUserId()

//Übergabe und Ermittlung des aktuellen Users aus dem Firestore=====================================

        //Update aller User Daten aus dem Firestore
        viewModel.updateCurrentUserFromFirestore()

        //Objekt der User Id erstellen
        val uId = viewModel.uId

        //Zeige die aktuellen Daten des eingeloggten Users mit LiveData
        val currentUser = viewModel.currentUser

//Überwache den aktuellen User mit allen Daten aus der Datenbank Firestore==========================
        currentUser.observe(viewLifecycleOwner) { user ->
            viewModel.updateCurrentUserFromFirestore()

            binding.tvLastLoggedUsername.text = user.userName
            binding.tvLastLoggedName.text = user.name
            binding.tvLastLoggedPrename.text = user.preName
            binding.tvLastLoggedPhoneNumber.text = user.telNumber
            binding.tvLastLoggedStreetname.text = user.streetName
            binding.tvLastLoggedStreetnumber.text = user.streetNumber
            binding.tvLastLoggedZipCode.text = user.zipCode
            binding.tvLastLoggedCity.text = user.cityName
            //Profilfoto aus dem Storage laden
            if (user.profileImage != null){
                Glide.with(requireContext()).load(user.profileImage).into(binding.ivProfileImage)
                //falls keins vorhanden ist nimm den Platzhalter
            }else{
                binding.ivProfileImage.setImageResource(R.drawable.projekt_winterschlaf_logo)
            }
        }

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
            personalData.profileImage = binding.ivProfileImage.setImageURI(imageUri).toString()
            Log.d("editImageUri", "imageUri ${imageUri.toString()}")
            //Aufruf der Funktion zum speichern aus dem FirebaseRepository
            viewModel.newUserDataFirstSignIn(personalData)
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