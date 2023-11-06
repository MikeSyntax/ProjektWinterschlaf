package com.example.modulabschlussandroid.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.adapters.AdapterObjects
import com.example.modulabschlussandroid.databinding.FragmentHomeBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ViewModelObjects by activityViewModels()

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

        //Live Date mit Objekten
        val objectList = viewModel.objectList

        //RecyclerView
        val recView = binding.rvRentableObjects

        //feste Größe für die Performance
        recView.setHasFixedSize(true)

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

        //zu den Favoriten navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment2())
        }
    }
}
