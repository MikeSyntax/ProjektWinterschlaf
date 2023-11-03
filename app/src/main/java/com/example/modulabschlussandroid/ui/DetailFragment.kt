package com.example.modulabschlussandroid.ui

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: ViewModelObjects by activityViewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //für die Weitergabe der Koordinaten an den Distance Api Call
    private var lat1: String = ""
    private var lon1: String = ""
     var lat2: String = ""
     var lon2: String = ""

    private var _textDistance: MutableLiveData<String> = MutableLiveData()
    val textDistance: LiveData<String>
        get() = _textDistance

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

        //Provider für die Ermittlung des eigenen Standortes
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

        //Observer des aktuellen angeklickten Objekts
        viewModel.currentObject.observe(viewLifecycleOwner) { thisObject ->
            //Setzen der einzelnen Textfelder mit dem Inhalb der für dieses Object hinterlegten Daten

            _textDistance.value = "Entfernung zum Ziel"
            binding.btnGetDistance.text = textDistance.value

            binding.tvDetailCity.text = thisObject.city
            binding.tvDetailDescription.text = thisObject.description
            binding.tvDetailObject.text = thisObject.objectdescription
            binding.tvDetailPrice.text = "${thisObject.price.toString()}€"
            binding.ivDetailObject1.setImageResource(thisObject.image1Resource)
            binding.ivDetailObject2.setImageResource(thisObject.image2Resource)
            binding.ivDetailObject3.setImageResource(thisObject.image3Resource)

            //Verbinden der Detailansicht mit den GeoDaten für das Ziel
            geoObserver() //Ziel
            //Hier wird durch den FusedLocation Manager der eigene Standort ermittelt für den Start
            location()  //Start

            //Durch Klicken des Button wird die Entfernung zwischen beiden Koordinaten ermittelt und angezeigt
            //Verbinden der Detailansicht mit den Distance Daten
            binding.btnGetDistance.setOnClickListener {
               // und die aktuellen Koordinaten werden an die zweite Api übergebenk um die Entfernung darzustellen
                viewModel.getDistanceData("$lat1,$lon1", "$lat2,$lon2")
                //Der in der getDistance Abfrage ermittelte Wert für die Entfernung wird ausgelesen und angezeigt
                distanceObserver()
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
    }

    private fun distanceObserver() {
        viewModel.distanceData.observe(viewLifecycleOwner) {
            //Einbinden der DistanceMatrix Klasse
                distanceMatrix: DistanceMatrix? ->
            Log.d("success Detail", "$distanceMatrix distanceObserver für die Api der Entfernung")
            //von der distanceMatrix über rows-Klasse zur element-Klasse bis zum Ziel distance-Klasse und dort das Textfeld mit den Kilometern
            distanceMatrix?.rows?.firstOrNull()?.elements?.firstOrNull()?.distance?.text?.let { distanceText ->
                //Verbinden des Textfeldes auf dem Button mit der Kilometerangabe
                _textDistance.value = "$distanceText bis zum Ziel"
                binding.btnGetDistance.text = textDistance.value
                Log.d("success Detail", "${binding.btnGetDistance.text} Text im Button")
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

    // über den fusedLocation Manager wird der eigene Standort ermittelt,
    // hier in diesem Fall wird der letzte also lastLocation ermittelt,
    // und das funktioniert erst wenn man auf Google Maps seinen eigenen Standort kurz festlegt
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
                105
            )
            return
        }
        task.addOnSuccessListener {
            Log.d("success SuccessListener", "${it.longitude} Start Longitude")
            if (it != null) {
                //Hier werden die Koordinaten des eigenen Standortes in die Variablen gespeichert für Entfernungsabfrage
                lat1 = it.latitude.toString()
                lon1 = it.longitude.toString()

                     /* Toast.makeText(
                          requireContext(),
                          "Ziel Koordinaten \n${it.latitude} ${it.longitude}",
                          Toast.LENGTH_SHORT
                      ).show()*/
            }
        }
    }
}