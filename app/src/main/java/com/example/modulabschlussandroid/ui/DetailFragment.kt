package com.example.modulabschlussandroid.ui

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.apicall.distance.DistanceMatrix
import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Geo
import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Result
import com.example.modulabschlussandroid.databinding.FragmentDetailBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

var lat1: String = ""
var lon1: String = ""
var lat2: String = ""
var lon2: String = ""

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: ViewModelObjects by activityViewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireContext())
        //Observer des aktuellen angeklickten Objekts
        viewModel.currentObject.observe(viewLifecycleOwner) { thisObject ->

            //Setzen der einzelnen Textfelder mit dem Inhalb der für dieses Object hinterlegten Daten
            binding.tvDetailCity.text = thisObject.city
            binding.tvDetailDescription.text = thisObject.description
            binding.tvDetailObject.text = thisObject.objectdescription
            binding.tvDetailPrice.text = "${thisObject.price.toString()}€"
            binding.ivDetailObject1.setImageResource(thisObject.image1Resource)
            binding.ivDetailObject2.setImageResource(thisObject.image2Resource)
            binding.ivDetailObject3.setImageResource(thisObject.image3Resource)

            //Verbinden der Detailansicht mit den GeoDaten
            geoObserver()
            //Verbinden der Detailansicht mit den Distance Daten
            distanceObserver()


            binding.btnGetDistance.setOnClickListener {
                location()
                viewModel.getDistanceData("$lat1,$lon1", "$lat2,$lon2")
                Log.d("success Knöpfchen für Distance Anzeige", "$lon1, Longitude1 Start und $lon2 Longitude 2 Ziel")
            }


            //Hier werden die Objekte geliked und auf in der Datenbank gespeichert
            if (thisObject.liked) {
                binding.ivDetailLiked.setImageResource(R.drawable.star_liked)
            } else {
                binding.ivDetailLiked.setImageResource(R.drawable.star_unliked)
            }


            binding.ivDetailLiked.setOnClickListener {
                if (thisObject != null) {
                    thisObject.liked = !thisObject.liked
                    if (!thisObject.liked) {
                        binding.ivDetailLiked.setImageResource(R.drawable.star_unliked)
                        thisObject.liked = false
                        viewModel.updateObjects(thisObject)
                    } else {
                        binding.ivDetailLiked.setImageResource(R.drawable.star_liked)
                        thisObject.liked = true
                        viewModel.updateObjects(thisObject)
                    }
                }
            }
        }

        //Zurück zum Homescreen
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //Zurück zum Homescreen
        binding.cvHome.setOnClickListener {
            findNavController().navigateUp()
        }

        //Löschen eines Eintrags in der Datenbank und zurück navigieren
        binding.ivDetailDelete.setOnClickListener {
            viewModel.deleteById()
            findNavController().navigateUp()
        }

        //zu den Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToFavoriteFragment())
        }

        //zu den Favoriten navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToProfileFragment())
        }

        //Entfernung zum Ziel messen
        binding.tvDistance.setOnClickListener {
            findNavController().navigate(R.id.locationFragment)
        }
    }
/*
    private fun distanceObserver() {
        viewModel.distanceData.observe(viewLifecycleOwner) {
            //auf das LiveData zugreifen
            val data: DistanceMatrix? = viewModel.distanceData.value
            if (data != null) {
                val results: List<Row>? = data.rows
                if (results != null){
                    val deeperResults: List<Element>? = results.
                }
                for (thisDistance in results!!) {
                    binding.tvDistance.text = thisDistance.text


                    /* val results: String = data.status.toString()
                     for (thisDistance in results){
                         binding.tvDistance.text = "${thisDistance.} km"*/
                }
            }
        }
    }
*/
    private fun distanceObserver() {
        viewModel.distanceData.observe(viewLifecycleOwner) {
                //Einbinden der DistanceMatrix Klasse
                distanceMatrix: DistanceMatrix? ->
                Log.d("success Detail", "$distanceMatrix distanceObserver für die Api der Entfernung")
            //von der distanceMatrix über rows-Klasse zur element-Klasse bis zum Ziel distance-Klasse und dort das Textfeld mit den Kilometern
            distanceMatrix?.rows?.firstOrNull()?.elements?.firstOrNull()?.distance?.text?.let{
                    distanceText ->
                //Verbinden des Textfeldes auf dem Button mit der Kilometerangabe
                binding.btnGetDistance.text = "$distanceText km bis zu Ziel"
            }
        }
    }


    //Einbinden der ermittelten GeoDaten
    private fun geoObserver() {
        viewModel.geoResult.observe(viewLifecycleOwner) {
            // Auf das LiveData-Objekt zugreifen
            val geo: Geo? = viewModel.geoResult.value
            if (geo != null) {
                // Auf die Liste "results" zugreifen
                val results: List<Result> = geo.results
                // Zugriff auf die Liste "results" und können damit arbeiten
                for (thisGeo in results) {
                    // Hier kann man auf einzelne "Result"-Objekte zugreifen, z.B. result.latitude, result.longitude, result.name, usw.
                    binding.tvLatitude.text = "${thisGeo.latitude.toString()} lat"
                    binding.tvLongitude.text = "${thisGeo.longitude.toString()} lon"
                    Log.d("success geoObserver", "${thisGeo.longitude} Ziel Longitude")
                    //Hier werden die Koordinaten des Zieles in die Variablen gespeichert für Entfernungsabfrage
                    lat2 = thisGeo.latitude.toString()
                    lon2 = thisGeo.longitude.toString()
                }
            }
        }
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

}