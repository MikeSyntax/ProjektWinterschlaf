package com.example.modulabschlussandroid.adapters

import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.databinding.ListItemSleepBinding
import com.example.modulabschlussandroid.ui.HomeFragmentDirections
import com.example.modulabschlussandroid.ui.lat1
import com.example.modulabschlussandroid.ui.lon1
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

//Der Adapter organisiert mit Hilfe der Viewholder das Recycling der Items
class AdapterObjects(

    private var dataset: List<Objects>,
    private var viewModel: ViewModelObjects

) : RecyclerView.Adapter<AdapterObjects.ItemViewHolder>() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //Hier wird die View umfasst und der Listeneintrag dargestellt
    inner class ItemViewHolder(val binding: ListItemSleepBinding) :
        RecyclerView.ViewHolder(binding.root)

    //Hier werden die ViewHolder erstellt, also die gerade sichtbaren ListItems ohne Inhalte nur das Gerüst
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ListItemSleepBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        return ItemViewHolder(binding)
    }

    //Hier werden die ViewHolder also die leeren Gerüste mit Daten befüllt
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

        //Ein bestimmtes Objekt an einer bestimmten Position
        val thisObject = dataset[position]

        //Abkürzung um nicht immer holder.binding zu schreiben
        val binding = holder.binding

        //Befüllen der ViewHolder
        binding.ivObject.setImageResource(thisObject.image1Resource)
        binding.tvObject.text = thisObject.objectdescription
        binding.tvCity.text = thisObject.city
        binding.tvPrice.text = "${thisObject.price.toString()}€"
        binding.tvDescription.text = thisObject.description


        //Weiterleitung auf eine Detailseite
        binding.cvItemObject.setOnClickListener {

            //API Call wird gestartet mit Übergabe der Stadt
            val city: String = thisObject.city
            viewModel.getGeoResult(city)

            //Navigation auf das  aktuelle Objekt welches angeklickt wurde
            viewModel.setCurrentObject(thisObject)
            val navController = binding.cvItemObject.findNavController()
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
        }
    }

    //Hier wird die Größe der Liste bzw. die Anzahl der Items für die RecyclerView ermittelt
    override fun getItemCount(): Int {
        return dataset.size
    }


    private fun location() {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            Log.d("success SuccessListener", "${it.longitude} Start Longitude")
            if (it != null) {
                //Hier werden die Koordinaten des eigenen Standortes in die Variablen gespeichert für Entfernungsabfrage
                lat1 = it.latitude.toString()
                lon1 = it.longitude.toString()

                Toast.makeText(
                    requireContext(),
                    "Ziel Koordinaten \n${it.latitude} ${it.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    /*
        //Änderungen werden bei Aufruf an das Dataset mitgeteilt
        fun sortObjects(list: List<Objects>) {
            dataset = list
            notifyDataSetChanged()
        }*/
}
