package com.example.modulabschlussandroid.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentInsertBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertFragment : Fragment() {

    private val viewModel: ViewModelObjects by activityViewModels()

    private lateinit var binding: FragmentInsertBinding

    private lateinit var thisObject: Objects

    private lateinit var advertisement: Advertisement

   // private lateinit var personalData: PersonalData

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

        viewModel.showCurrentUserId()
        //Erstellen der Uid aus der Authentication
        val uId = viewModel.uId
        //Erstellen eines Objektes der Object Klasse
        thisObject = Objects()
        //Erstellen eines Objektes der MyObject Klasse für die Firebase Datenbank mit den abrufbaren Objekten
        advertisement = Advertisement()
        //Erstellen eines Objektes der PersonalData Klasse
        //personalData = PersonalData()
        val currentUser = viewModel.currentUser

//Übergabe der Uid als Parameter um die Kategeorien unter Uid des Users zu speichern================
        binding.cvCategories.setOnClickListener {
            showCategorieDialog()
        }
//Übergabe der Uid als Parameter um die Postleitzahl unter Uid des Users zu speichern===============
        binding.cvZipCode.setOnClickListener {
            showZipCodeDialog()
        }

//Übergabe der Uid als Parameter um die Stadt unter Uid des Users zu speichern======================
        binding.cvCity.setOnClickListener {
            showCityDialog()
        }

//Übergabe der Uid als Parameter um die Titel unter Uid des Users zu speichern======================
        binding.cvTitle.setOnClickListener {
            showTitleDialog()
        }

//Übergabe der Uid als Parameter um die Beschreibung unter Uid des Users zu speichern===============
        binding.cvDescription.setOnClickListener {
            showDescriptionDialog()
        }

//Übergabe der Uid als Parameter um die Preis unter Uid des Users zu speichern======================
        binding.cvPrice.setOnClickListener {
            showPriceDialog()
        }

//Inserieren eines neuen Advertisements bzw. einer neuen Anzeige ===================================
        binding.btnFloatingAction.setOnClickListener {
            //Auslesen der Eingabefelder ( die Switches der Kategoriefelder, werden in der nächsten Funktion gesetzt
            advertisement.userId = uId.value
           // Log.d("Insert", "UserId $uId")
            advertisement.zipCode = binding.editZipCode.text.toString()
            advertisement.city = binding.editCity.text.toString()
            advertisement.title = binding.editTitle.text.toString()
            advertisement.description = binding.editDescription.text.toString()
            advertisement.price = binding.editPrice.text.toString()

            //Aufruf der Funktion für den Counter aller Inserate +1 also 2+1 = bisherige Inserate3
            viewModel.addCounterForAdvertises(currentUser.value!!)

            //Aufruf der Funktion aus dem Firebase Repository
            viewModel.saveItemToDatabase(advertisement)

            //Den User Updaten nach der Änderung
            viewModel.updateCurrentUserFromFirestore()

            //Nach dem Inserieren erfolgt die Weiterleitung zur Profilseite
            val navController = binding.btnFloatingAction.findNavController()
            navController.navigate(InsertFragmentDirections.actionInsertFragmentToProfileFragment())
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

//Beginn der Dialogfelder zum Inserieren einer neuen Anzeig=========================================
    //Funktion zur Anzeige des Dialogs für die Kategorienauswahl mit den entsprechenden gewünschten Ausführungen
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun showCategorieDialog() {
        //Objekt dialog erstellen
        val dialog = Dialog(requireContext())
        //Erstellen der View
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Initialisieren aller Switch´s und Button auf dem Dialog der Kategorien
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
            //Für die Übergabe an die Firebase Realtime Datenbank werden hier die Kategorien gesetzt
            if (garageSwitch.isChecked) {
                advertisement.garage = true
            }
            if (camperParkingInsideSwitch.isChecked) {
                advertisement.camperParkingInside = true
            }
            if (camperParkingOutsideSwitch.isChecked) {
                advertisement.camperParkingOutside = true
            }
            if (parkingSpotSwitch.isChecked) {
                advertisement.parkingSpot = true
            }
            if (undergroundParkingSwitch.isChecked) {
                advertisement.underGroundParking = true
            }
            if (carportSwitch.isChecked) {
                advertisement.carport = true
            }
            if (storageSwitch.isChecked) {
                advertisement.storage = true
            }
            if (storageHallSwitch.isChecked) {
                advertisement.storageHall = true
            }
            if (storageRoomSwitch.isChecked) {
                advertisement.storageRoom = true
            }
            if (storageBoxSwitch.isChecked) {
                advertisement.storageBox = true
            }
            if (containerSwitch.isChecked) {
                advertisement.container = true
            }
            if (basementSwitch.isChecked) {
                advertisement.basement = true
            }
            if (barnSwitch.isChecked) {
                advertisement.barn = true
            }
            if (openSpaceSwitch.isChecked) {
                advertisement.openSpace = true
            }
            if (yardSwitch.isChecked) {
                advertisement.yard = true
            }
            //Wenn einer der ganzen Kategorien ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
            if (garageSwitch.isChecked || camperParkingInsideSwitch.isChecked || camperParkingOutsideSwitch.isChecked ||
                parkingSpotSwitch.isChecked || undergroundParkingSwitch.isChecked || carportSwitch.isChecked ||
                storageSwitch.isChecked || storageHallSwitch.isChecked || storageRoomSwitch.isChecked ||
                storageBoxSwitch.isChecked || containerSwitch.isChecked || basementSwitch.isChecked ||
                barnSwitch.isChecked || openSpaceSwitch.isChecked || yardSwitch.isChecked
            ) {
                //die Kategoriewahl wird auf true gesetzt und der Daumen hoch wird angezeigt
                thisObject.doneCategoryChoice = true
                binding.categoriesEdit.setImageResource(R.drawable.done)
                binding.tvCategories.text = "Kategorien erledigt"
                //Anzeige des Floating Action Buttons sobald alle Textfelder ausgefüllt sind
                showFloationActionButton()
            } else {
                //die Kategoriewahl wird auf false gesetzt und der Edit Stift wird angezeigt
                thisObject.doneCategoryChoice = false
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
    private fun showZipCodeDialog() {
        //Objekt zipCodeDialog erstellen
        val zipCodeDialog = Dialog(requireContext())
        //Erstellen der View
        zipCodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        zipCodeDialog.setCancelable(false)
        zipCodeDialog.setContentView(R.layout.zipcode_dialog)
        zipCodeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Initialisieren aller Switch´s und Button auf dem Dialog der Kategorien
        val saveBtn: Button = zipCodeDialog.findViewById(R.id.btn_save)

        val textMessage: TextView = zipCodeDialog.findViewById(R.id.edit_text_zipcode)

        //Um eine Tastatur einzublenden
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                thisObject.doneZipCodeChoice = true
                binding.editZipCode.text = textMessage.text
                binding.zipCodeEdit.setImageResource(R.drawable.done)
                //Für die Firebase datenbank
                advertisement.zipCode = binding.editZipCode.text.toString()
                //Anzeige des Floating Action Buttons sobald alle Textfelder ausgefüllt sind
                showFloationActionButton()
            } else {
                thisObject.doneZipCodeChoice = false
                binding.editZipCode.text = "Wähle eine Postleitzahl"
                binding.zipCodeEdit.setImageResource(R.drawable.edit)
            }
            //Dialog ausblenden
            zipCodeDialog.dismiss()
        }
        //Dialog anzeigen
        zipCodeDialog.show()
    }

    //Dialog Feld Eingabe für die Stadt=================================================================
    private fun showCityDialog() {
        //Objekt cityDialog erstellen
        val cityDialog = Dialog(requireContext())

        //Erstellen der View
        cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        cityDialog.setCancelable(false)
        cityDialog.setContentView(R.layout.city_dialog)
        cityDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Initialisieren aller Switch´s und Button auf dem Dialog der Stadt
        val saveBtn: Button = cityDialog.findViewById(R.id.btn_save)
        val textMessage: TextView = cityDialog.findViewById(R.id.edit_text_city)

        //Um eine Tastatur einzublenden
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //Ermöglicht das direkte Schreiben im Eingabefeld ohne erst reinzuklicken
        textMessage.requestFocus()
        //kleine Zeitverzögerung damit die Tastatur aufgebaut werden kann
        lifecycleScope.launch {
            delay(200)
            inputMethodManager.showSoftInput(textMessage, InputMethodManager.SHOW_IMPLICIT)
        }

        //Beim Klicken des Speichern Buttons auf dem Städtenamen Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //Wenn eine Stadt ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
            if (textMessage.text.isNotEmpty()) {
                thisObject.doneCityChoice = true
                binding.editCity.text = textMessage.text
                binding.cityEdit.setImageResource(R.drawable.done)
                //Für die Firebase datenbank
                advertisement.city = binding.editCity.text.toString()
                //Anzeige des Floating Action Buttons sobald alle Textfelder ausgefüllt sind
                showFloationActionButton()
            } else {
                thisObject.doneCityChoice = false
                binding.editCity.text = "Wähle eine Stadt"
                binding.cityEdit.setImageResource(R.drawable.edit)
            }
            //Dialog ausblenden
            cityDialog.dismiss()
        }
        //Dialog anzeigen
        cityDialog.show()
    }

    //Dialog Feld Eingabe für die Titel=================================================================
    private fun showTitleDialog() {
        //Objekt TitelDialog erstellen
        val titelDialog = Dialog(requireContext())
        //Erstellen der View
        titelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        titelDialog.setCancelable(false)
        titelDialog.setContentView(R.layout.titel_dialog)
        titelDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Initialisieren aller Switch´s und Button auf dem Dialog der Überschrift
        val saveBtn: Button = titelDialog.findViewById(R.id.btn_save)
        val textMessage: TextView = titelDialog.findViewById(R.id.edit_text_titel)

        //Um eine Tastatur einzublenden
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //Ermöglicht das direkte Schreiben im Eingabefeld ohne erst reinzuklicken
        textMessage.requestFocus()
        //kleine Zeitverzögerung damit die Tastatur aufgebaut werden kann
        lifecycleScope.launch {
            delay(200)
            inputMethodManager.showSoftInput(textMessage, InputMethodManager.SHOW_IMPLICIT)
        }

        //Beim Klicken des Speichern Buttons auf dem Überschrift Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //Wenn ein Überschrift ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
            if (textMessage.text.isNotEmpty()) {
                thisObject.doneObjectDescriptionChoice = true
                binding.editTitle.text = textMessage.text
                binding.titleEdit.setImageResource(R.drawable.done)
                //Für die Firebase datenbank
                advertisement.title = binding.editTitle.text.toString()
                //Anzeige des Floating Action Buttons sobald alle Textfelder ausgefüllt sind
                showFloationActionButton()
            } else {
                thisObject.doneObjectDescriptionChoice = false
                binding.editTitle.text = "Wähle eine Überschrift"
                binding.titleEdit.setImageResource(R.drawable.edit)
            }
            //Dialog ausblenden
            titelDialog.dismiss()
        }
        //Dialog anzeigen
        titelDialog.show()
    }

    //Dialog Feld Eingabe für die Beschreibung==========================================================
    private fun showDescriptionDialog() {
        //Objekt descriptionDialog erstellen
        val descriptionDialog = Dialog(requireContext())
        //Erstellen der View
        descriptionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        descriptionDialog.setCancelable(false)
        descriptionDialog.setContentView(R.layout.description_dialog)
        descriptionDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Initialisieren aller Switch´s und Button auf dem Dialog der Beschreibung
        val saveBtn: Button = descriptionDialog.findViewById(R.id.btn_save)
        val textMessage: TextView = descriptionDialog.findViewById(R.id.edit_text_description)

        //Um eine Tastatur einzublenden
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //Ermöglicht das direkte Schreiben im Eingabefeld ohne erst reinzuklicken
        textMessage.requestFocus()
        //kleine Zeitverzögerung damit die Tastatur aufgebaut werden kann
        lifecycleScope.launch {
            delay(200)
            inputMethodManager.showSoftInput(textMessage, InputMethodManager.SHOW_IMPLICIT)
        }

        //Beim Klicken des Speichern Buttons auf dem Beschreibung Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //Wenn eine Beschreibung ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
            if (textMessage.text.isNotEmpty()) {
                thisObject.doneDescriptionChoice = true
                binding.editDescription.text = textMessage.text
                binding.descriptionEdit.setImageResource(R.drawable.done)
                //Für die Firebase datenbank
                advertisement.description = binding.editDescription.text.toString()
                //Anzeige des Floating Action Buttons sobald alle Textfelder ausgefüllt sind
                showFloationActionButton()
            } else {
                thisObject.doneDescriptionChoice = false
                binding.editDescription.text = "Wähle eine Beschreibung"
                binding.descriptionEdit.setImageResource(R.drawable.edit)
            }
            //Dialog ausblenden
            descriptionDialog.dismiss()
        }
        //Dialog anzeigen
        descriptionDialog.show()
    }

    //Dialog Feld Eingabe für die Preis=================================================================
    private fun showPriceDialog() {
        //Objekt PriceDialog erstellen
        val priceDialog = Dialog(requireContext())
        //Erstellen der View
        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        priceDialog.setCancelable(false)
        priceDialog.setContentView(R.layout.price_dialog)
        priceDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Initialisieren aller Switch´s und Button auf dem Dialog der Preis
        val saveBtn: Button = priceDialog.findViewById(R.id.btn_save)
        val textMessage: TextView = priceDialog.findViewById(R.id.edit_text_price)

        //Um eine Tastatur einzublenden
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //Ermöglicht das direkte Schreiben im Eingabefeld ohne erst reinzuklicken
        textMessage.requestFocus()
        //kleine Zeitverzögerung damit die Tastatur aufgebaut werden kann
        lifecycleScope.launch {
            delay(200)
            inputMethodManager.showSoftInput(textMessage, InputMethodManager.SHOW_IMPLICIT)
        }

        //Beim Klicken des Speichern Buttons auf dem Preis Dialog, werden folgende Einstelunngen übernommen...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
            //Wenn eine Preis ausgewählt wurde, dann ersetzen den Bleistift mit dem DaumenHoch mit Feld 1
            if (textMessage.text.isNotEmpty()) {
                thisObject.donePriceChoice = true
                binding.editPrice.text = textMessage.text
                binding.priceEdit.setImageResource(R.drawable.done)
                //Für die Firebase datenbank
                advertisement.price = binding.editPrice.text.toString()
                //Anzeige des Floating Action Buttons sobald alle Textfelder ausgefüllt sind
                showFloationActionButton()
            } else {
                thisObject.donePriceChoice = false
                binding.editPrice.text = "Wähle einen Preis"
                binding.priceEdit.setImageResource(R.drawable.edit)
            }
            //Dialog ausblenden
            priceDialog.dismiss()
        }
        //Dialog anzeigen
        priceDialog.show()
    }

    //Der Floating Action Button soll sichtbar werden, sobald alle Felder ausgefüllt sind
    private fun showFloationActionButton() {
        if (
            thisObject.doneCategoryChoice &&
            thisObject.doneZipCodeChoice &&
            thisObject.doneCityChoice &&
            thisObject.doneObjectDescriptionChoice &&
            thisObject.doneDescriptionChoice &&
            thisObject.donePriceChoice
        ) {
            binding.btnFloatingAction.isVisible = true
        }
    }
//==================================================================================================
}


