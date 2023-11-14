package com.example.modulabschlussandroid.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.databinding.FragmentInsertBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class InsertFragment : Fragment() {

    private lateinit var binding: FragmentInsertBinding

    private val viewModel: ViewModelObjects by activityViewModels()

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var myObject: Objects

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

//Übergabe der Uid als Parameter um die Kategeorien unter Uid des Users zu speichern================
        binding.cvCategories.setOnClickListener {
            showCategorieDialog(uId)
        }
//Übergabe der Uid als Parameter um die Postleitzahl unter Uid des Users zu speichern===============
        binding.cvZipCode.setOnClickListener {
            showZipCodeDialog(uId)
        }
//Übergabe der Uid als Parameter um die Stadt unter Uid des Users zu speichern======================
        binding.cvCity.setOnClickListener {
            showCityDialog(uId)
        }

//Bottom Nav BAR ===================================================================================
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

    //Funktion zur Anzeige des Dialogs für die Kategorienauswahl mit den entsprechenden gewünschten Ausführungen
    private fun showCategorieDialog(Uid: String) {
        //Objekt dialog erstellen
        val dialog = Dialog(requireContext())
        //Auswahl kürzerer Name
        var categoryChoice = myObject.doneCategoryChoice
        //Erstellen der View
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//Initialisieren aller Switch´s und Button auf dem Dialog der Kategorien============================
        val saveBtn: Button = dialog.findViewById(R.id.btn_save)
        val garageSwitch: Switch = dialog.findViewById(R.id.switch_garage)
        val camperParkingInsideSwitch: Switch = dialog.findViewById(R.id.switch_camperParkingInside)
        val camperParkingOutsideSwitch: Switch =
            dialog.findViewById(R.id.switch_camperParkingOutside)
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
                categoryChoice = true
                binding.categoriesEdit.setImageResource(R.drawable.done)
                binding.tvCategories.text = "Kategorien erledigt"
            } else {
                //die Kategoriewahl wird auf false gesetzt und der Edit Stift wird angezeigt
                categoryChoice = false
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
        //Auswahl kürzerer Name
        var zipCodeChoice = myObject.doneZipCodeChoice
        //Erstellen der View
        zipCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        zipCodeDialog.setCancelable(false)
        zipCodeDialog.setContentView(R.layout.zipcode_dialog)
        zipCodeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//Initialisieren aller Switch´s und Button auf dem Dialog der Kategorien============================
        val saveBtn: Button = zipCodeDialog.findViewById(R.id.btn_save)
        val textMessage: TextView = zipCodeDialog.findViewById(R.id.edit_text_zipcode)

        //Um eine Tastatur einzublenden
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //Ermöglicht das direkte Schreiben im Eingabefeld ohne erst reinzuklicken
        textMessage.requestFocus()
        //kleine Zeitverzögerung damit die Tastatur aufgebaut werden kann
        lifecycleScope.launch {
            delay(200)
        inputMethodManager.showSoftInput(textMessage, InputMethodManager.SHOW_IMPLICIT)
        }

//Beim Klicken des Speichern Buttons auf dem Postleitzahlen Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //Wenn eine Postleitzahl ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
            if (textMessage.text.isNotEmpty()) {
                binding.editZipCode.text = "Postleitzahl ${textMessage.text}"
                binding.zipCodeEdit.setImageResource(R.drawable.done)
                zipCodeChoice = true
            } else {
                binding.editZipCode.text = "Wähle eine Postleitzahl"
                binding.zipCodeEdit.setImageResource(R.drawable.edit)
                zipCodeChoice = false

            }
            //Dialog ausblenden
            zipCodeDialog.dismiss()
        }
        //Dialog anzeigen
        zipCodeDialog.show()
    }

    //Dialog Feld Eingabe für die Stadt=============================================================
    private fun showCityDialog(Uid: String) {
        //Objekt cityDialog erstellen
        val cityDialog = Dialog(requireContext())
        //Auswahl kürzerer Name
        var cityChoice = myObject.doneCityChoice
        //Erstellen der View
        cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        cityDialog.setCancelable(false)
        cityDialog.setContentView(R.layout.city_dialog)
        cityDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//Initialisieren aller Switch´s und Button auf dem Dialog der Stadt=================================
        val saveBtn: Button = cityDialog.findViewById(R.id.btn_save)
        val textMessage: TextView = cityDialog.findViewById(R.id.edit_text_city)
//Beim Klicken des Speichern Buttons auf dem Städtenamen Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //Wenn eine Stadt ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
            if (textMessage.text.isNotEmpty()) {
                binding.editCity.text = "Stadt ${textMessage.text}"
                binding.cityEdit.setImageResource(R.drawable.done)
                cityChoice = true
            } else {
                binding.editCity.text = "Wähle eine Stadt"
                binding.cityEdit.setImageResource(R.drawable.edit)
                cityChoice = false

            }
            //Dialog ausblenden
            cityDialog.dismiss()
        }
        //Dialog anzeigen
        cityDialog.show()
    }
//==================================================================================================
}