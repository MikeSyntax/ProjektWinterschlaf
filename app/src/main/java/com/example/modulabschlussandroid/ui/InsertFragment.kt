package com.example.modulabschlussandroid.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.databinding.FragmentInsertBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class InsertFragment : Fragment() {

    private lateinit var binding: FragmentInsertBinding

    private val viewModel: ViewModelObjects by activityViewModels()

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var myObject : Objects

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Erstellen eines Objektes der Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()
        //Erstellen der Uid aus der Authentication
        val uId: String = firebaseAuth.uid.toString()
        //Erstellen eines Objektes der Object Klasse
        myObject = Objects()

        //Übergabe der Uid als Parameter um die Kategeorien unter Uid des Users zu speichern
        binding.cvCategories.setOnClickListener {
            showCategorieDialog(uId)
        }

        binding.cvZipCode.setOnClickListener {
            val message: String = "Postleitzahl eingeben"
            showZipCodeDialog(uId)
        }


        //Zu den Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            val navController = binding.cvFavorite.findNavController()
            navController.navigate(InsertFragmentDirections.actionInsertFragmentToFavoriteFragment())
        }

        //zu den Profil navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToProfileFragment())
        }

        //zum inserieren navigieren
        binding.cvHome.setOnClickListener {
            findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToHomeFragment())
        }

        //zum inserieren navigieren
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun zipCode(){
        val zipCode = binding.editZipCode
        if (zipCode == null) {
            binding.zipCodeEdit.setImageResource(R.drawable.done)
        }
    }

//Funktion zur Anzeige des Dialogs für die Kategorienauswahl mit den entsprechenden gewünschten Ausführungen
    private fun showCategorieDialog(Uid: String) {
        //Objekt dialog erstellen
        val dialog = Dialog(requireContext())
        //Erstellen der View
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//Initialisieren aller Switch´s und Button auf dem Dialog der Kategorien============================
        val saveBtn: Button = dialog.findViewById(R.id.btn_save)
        val garageSwitch: Switch = dialog.findViewById(R.id.switch_garage)
        val camperParkingInsideSwitch: Switch = dialog.findViewById(R.id.switch_camperParkingInside)
        val camperParkingOutsideSwitch: Switch = dialog.findViewById(R.id.switch_camperParkingOutside)
        val parkingSpotSwitch: Switch = dialog.findViewById(R.id.switch_parkingSpot)
        val undergroundParkingSwitch: Switch = dialog.findViewById(R.id.switch_undergroundParking)
        val carportSwitch: Switch = dialog.findViewById(R.id.switch_carport)
        val storageSwitch: Switch = dialog.findViewById(R.id.switch_storage)
        val storageHallSwitch: Switch = dialog.findViewById(R.id.switch_storageHall)
        val storageRoomSwitch: Switch = dialog.findViewById(R.id.switch_storageRoom)
        val storageBoxSwitch: Switch = dialog.findViewById(R.id.switch_storageBox)
        val containerSwitch: Switch = dialog.findViewById(R.id.switch_container)
        val basementSwitch: Switch = dialog.findViewById(R.id.switch_basement)
        val barnSwitch: Switch = dialog.findViewById(R.id.switch_barn)
        val openSpaceSwitch: Switch = dialog.findViewById(R.id.switch_openSpace)
        val yardSwitch: Switch = dialog.findViewById(R.id.switch_yard)


//Beim Klicken des Speichern Buttons auf dem Kategorien Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //    binding.editDescription.text = tvMessage.text as Editable?
            //Wenn einer der ganzen Kategorien ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1

            if (garageSwitch.isChecked || camperParkingInsideSwitch.isChecked || camperParkingOutsideSwitch.isChecked ||
                parkingSpotSwitch.isChecked || undergroundParkingSwitch.isChecked || carportSwitch.isChecked ||
                storageSwitch.isChecked || storageHallSwitch.isChecked || storageRoomSwitch.isChecked ||
                storageBoxSwitch.isChecked || containerSwitch.isChecked || basementSwitch.isChecked ||
                barnSwitch.isChecked || openSpaceSwitch.isChecked || yardSwitch.isChecked
            ) {
                //die Kategoriewahl wird auf true gesetzt und der Daumen hoch wird angezeigt
                myObject.doneCategoryChoice = true
                binding.categoriesEdit.setImageResource(R.drawable.done)
                binding.tvCategories.text = "Kategorien erledigt"
            } else {
                //die Kategoriewahl wird auf false gesetzt und der Edit Stift wird angezeigt
                myObject.doneCategoryChoice = false
                binding.categoriesEdit.setImageResource(R.drawable.edit)
                binding.tvCategories.text = "Wähle eine Kategorie"
            }
            //Dialog ausblenden
            dialog.dismiss()
        }
        //Dialog anzeigen
        dialog.show()
    }

//Dialog Feld Eingabe für die Postleitzahl==========================================================
    private fun showZipCodeDialog(Uid: String) {
        //Objekt zipCodeDialog erstellen
        val zipCodeDialog = Dialog(requireContext())
        //Erstellen der View
        zipCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        zipCodeDialog.setCancelable(false)
        zipCodeDialog.setContentView(R.layout.zipcode_dialog)
        zipCodeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//Initialisieren aller Switch´s und Button auf dem Dialog der Kategorien============================
        val saveBtn: Button = zipCodeDialog.findViewById(R.id.btn_save)
        val text: TextView = zipCodeDialog.findViewById(R.id.edit_text_zipcode)
//Beim Klicken des Speichern Buttons auf dem Postleitzahlen Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //Wenn eine Postleitzahl ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
           if (text.text.isNotEmpty()){
            binding.editZipCode.text = text.text
            binding.zipCodeEdit.setImageResource(R.drawable.done)
           } else {
               binding.zipCodeEdit.setImageResource(R.drawable.edit)
           }
            //Dialog ausblenden
            zipCodeDialog.dismiss()
        }
        //Dialog anzeigen
        zipCodeDialog.show()
    }
//==================================================================================================
}