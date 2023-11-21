package com.example.modulabschlussandroid.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.adapters.AdapterObjects
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentHomeBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ViewModelObjects by activityViewModels()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//Dieser Abschnitt unterbindet ein zurücksringen auf den Login Screen===============================

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.close_app)
                builder.setMessage(R.string.sure_to_leave)
                builder.setPositiveButton(R.string.yes) { dialog, which ->
                    requireActivity().finish()
                }
                builder.setNegativeButton(R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

//Aufbau der Ansicht, Objekte und Design erstellen==================================================

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //Returned immer das oberste Element
        return binding.root
    }

//Beginn der Ansicht und befüllen===================================================================

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Intanz des Firestores erstellen
        firestore = FirebaseFirestore.getInstance()

        //Live Date mit Objekten
        val objectList = viewModel.objectList

        //RecyclerView
        val recView = binding.rvRentableObjects

        //feste Größe für die Performance
        recView.setHasFixedSize(true)

        //Um zu erkennen ob der User neu ist, wird  hier abgefragt ob in der Profilseite
        // bei dem Namen noch nichts steht, und dann öffnet sich das Dialogfenster
        checkUserDataComplete()


//LiveData Objekte überwachen=======================================================================

        //Überwachen aller aktuellen Objekte und setzen des Adapter mit Observer
        objectList.observe(viewLifecycleOwner) { objects ->
            //Parameter objectList(it) und ViewModel
            recView.adapter = AdapterObjects(objects, viewModel)
        }

//Gefilterte Objekte überwachen=====================================================================

        //NEU Schnellsuche alle gefilterten Objekte landen in dieser List
        viewModel.zipObjects.observe(viewLifecycleOwner) { objectSearch ->
            //Parameter objectList(it) nullable und ViewModel
            recView.adapter = AdapterObjects(objectSearch!!, viewModel)
            Log.d("success Home", "$objectSearch zipObjects für Suche")
        }

//Suchtext überwachen===============================================================================

        //NEU Schnellsuche der Inputtext wird weitergegeben bis zur Objekt Dao Funktion
        viewModel.inputText.observe(viewLifecycleOwner) { textInput ->
            viewModel.getZipCodeObject(textInput)
            Log.d("success Home", "$textInput input Text")
        }

//Für die Suche einen TextchangedListener===========================================================

        //NEU Schnellsuche für die Postleitzahl
        binding.textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateInputText(p0.toString())
            }
        })

        //Zu den Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            val navController = binding.cvFavorite.findNavController()
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
        }

        //zum eigenen Profil navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
        }

        //zum inserieren navigieren
        binding.cvInsert.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToInsertFragment())
        }
    }


    //Dialog Feld Eingabe für die Preis=================================================================
    private fun showNewUserDialog() {
        //Objekt newUserDialog erstellen
        val newUserDialog = Dialog(requireContext())
        //Erstellen der View
        newUserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        newUserDialog.setCancelable(false)
        newUserDialog.setContentView(R.layout.newuser_dialog)
        newUserDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //Initialisieren des Dialogs und Button für die Weiterleitung zum Profil Edit
        val saveBtn: Button = newUserDialog.findViewById(R.id.btn_save)
        val exitBtn: Button = newUserDialog.findViewById((R.id.btn_exit))
        //Beim Klicken des Speichern Buttons wird der User falls auf das EditProfilFragment weitergeleitet...
        saveBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Viel Spaß", Toast.LENGTH_SHORT).show()
            val navController = findNavController()
            navController.navigate(R.id.editProfileFragment)
            //Dialog ausblenden
            newUserDialog.dismiss()
        }
        //Exit Button zum Verlassen des Dialog Fensters
        exitBtn.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.profileFragment)
            //Dialog ausblenden
            newUserDialog.dismiss()
        }
        //Dialog anzeigen
        newUserDialog.show()
    }

    //Falls der User neu ist, zeige den Dialog für die Datenerfassung
    private fun checkUserDataComplete() {
        //zuerst die User Id aktualisieren
        viewModel.showCurrentUserId()
        //Intanz der uId erstellen
        val uId = viewModel.uId
        //Abfrage Firestore
        firestore.collection("user")
            //ob dieses Document mit der Id
            .document(uId.value.toString())
            //suchen
            .get()
            //Listener
            .addOnCompleteListener { task ->
                //Abfrage erfolgreich
                if (task.isSuccessful) {
                    //Resultat der Abfrage
                    val document = task.result
                    Log.d("success Home", "uId ${uId.value} task result ${task.result}")
                    //wenn diese Id bzw. dieses Document NICHT existiert
                    if (!document.exists()) {
                        // dann zeige den Dialog für die User Daten vervollständigung
                        showNewUserDialog()
                    }
                }
            }
    }
}
