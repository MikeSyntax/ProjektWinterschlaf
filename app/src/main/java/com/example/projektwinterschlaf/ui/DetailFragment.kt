package com.example.projektwinterschlaf.ui
//==================================================================================================
//****************************        Detail Fragment         **************************************
//==================================================================================================

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.projektwinterschlaf.R
import com.example.projektwinterschlaf.data.datamodels.Advertisement
import com.example.projektwinterschlaf.data.datamodels.apicall.distance.DistanceMatrix
import com.example.projektwinterschlaf.data.datamodels.apicall.geo.Geo
import com.example.projektwinterschlaf.data.datamodels.apicall.geo.Result
import com.example.projektwinterschlaf.databinding.FragmentDetailBinding
import com.example.projektwinterschlaf.viewmodels.ViewModelObjects
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: ViewModelObjects by activityViewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var advertisement: Advertisement

    //für die Weitergabe der Koordinaten an den Distance Api Call
    private var lat1: String = ""
    private var lon1: String = ""
    private var lat2: String = ""
    private var lon2: String = ""

    //LiveData für die API Call Abfrage der Entfernung

    private var _textDistance: MutableLiveData<String> = MutableLiveData()
    val textDistance: LiveData<String>
        get() = _textDistance

//==================================================================================================
//onCreatedView=====================================================================================
//==================================================================================================

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

//==================================================================================================
//onViewCreated=====================================================================================
//==================================================================================================

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Hier wird die aktuelle AdvertismentId mit LiveData verfolgt um beim Chat verwendet zu werden
        val currentAdvertisementId = viewModel.currentAdvertisementId

        //Provider für die Ermittlung des eigenen Standortes
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

//==================================================================================================
//Observer der Room Datenbank=======================================================================
//==================================================================================================

        //Observer des aktuellen angeklickten Objekts
        viewModel.currentObject.observe(viewLifecycleOwner) { thisObject ->
            if (viewModel.homeFragment) {


                //Setzen der errechneten Entfernung zwischen Start und Ziel
                _textDistance.value = "Entfernung zum Ziel"
                binding.btnGetDistance.text = textDistance.value
                //Setzen der einzelnen Textfelder mit dem Inhalb der für dieses Object hinterlegten Daten
                binding.tvDetailCity.text = thisObject.city
                binding.tvDetailDescription.text = thisObject.description
                binding.tvDetailObject.text = thisObject.objectdescription
                binding.tvDetailPrice.text = "${thisObject.price.toString()}€"
                binding.ivDetailObject1.setImageResource(thisObject.image1Resource)
                binding.ivDetailObject2.setImageResource(thisObject.image2Resource)
                binding.ivDetailObject3.setImageResource(thisObject.image3Resource)
                binding.tvDistance.text = "PLZ ${thisObject.zipCode}"

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
                    binding.ivDetailLiked.setImageResource(R.drawable.star_like)
                } else {
                    binding.ivDetailLiked.setImageResource(R.drawable.star_unlike)
                }

                binding.ivDetailLiked.setOnClickListener {
                    if (thisObject != null) {
                        thisObject.liked = !thisObject.liked
                        if (!thisObject.liked) {
                            binding.ivDetailLiked.setImageResource(R.drawable.star_unlike)
                            thisObject.liked = false
                            viewModel.updateObjects(thisObject)
                        } else {
                            binding.ivDetailLiked.setImageResource(R.drawable.star_like)
                            thisObject.liked = true
                            viewModel.updateObjects(thisObject)
                        }
                    }
                }
            }
        }

//==================================================================================================
//Observer der Firebase Datenbank===================================================================
//==================================================================================================

        //Observer des aktuellen angeklickten Objekts
        viewModel.currentAdvertisement.observe(viewLifecycleOwner) { thisAdvertisment ->
            if (!viewModel.homeFragment) {


                //Setzen der errechneten Entfernung zwischen Start und Ziel
                _textDistance.value = "Entfernung zum Ziel"
                binding.btnGetDistance.text = textDistance.value
                //Setzen der einzelnen Textfelder mit dem Inhalb der für dieses Object hinterlegten Daten
                binding.tvDetailCity.text = thisAdvertisment.city
                binding.tvDetailDescription.text = thisAdvertisment.description
                binding.tvDetailObject.text = thisAdvertisment.title
                binding.tvDetailPrice.text = "${thisAdvertisment.price.toString()}€"
                binding.tvDistance.text = "PLZ ${thisAdvertisment.zipCode}"
                binding.tvAdsUserName.textSize = 20f
                binding.tvAdsUserName.text = "USER: ${thisAdvertisment.ownerOfThisAd}"
                //Profilfoto aus dem Storage laden
                Glide.with(requireContext()).load(thisAdvertisment.profileImageForAd)
                    .placeholder(R.drawable.projekt_winterschlaf_logo).into(binding.ivUser)
             //   Log.d("Detail", "Bild Ersteller ${thisAdvertisment.profileImageForAd}")
                //falls keins vorhanden ist nimm den Platzhalter
                advertisement = thisAdvertisment
                //Verbinden der Detailansicht mit den GeoDaten für das Ziel
                geoObserver() //Ziel
                //Hier wird durch den FusedLocation Manager der eigene Standort ermittelt für den Start
                location()  //Start
                //Erkennen der aktuellen AdvertismentId mit LiveData
                viewModel.getAdvertisementId(advertisement)
                //Durch Klicken des Button wird die Entfernung zwischen beiden Koordinaten ermittelt und angezeigt
                //Verbinden der Detailansicht mit den Distance Daten
                binding.btnGetDistance.setOnClickListener {
                    // und die aktuellen Koordinaten werden an die zweite Api übergebenk um die Entfernung darzustellen
                    viewModel.getDistanceData("$lat1,$lon1", "$lat2,$lon2")
                    //Der in der getDistance Abfrage ermittelte Wert für die Entfernung wird ausgelesen und angezeigt
                    distanceObserver()
                }
            }
        }

//==================================================================================================
//Diverse OnClickListener===========================================================================
//==================================================================================================

        //Zurück zum Homescreen
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //Zurück zum Homescreen
        binding.cvHome.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToHomeFragment())
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

        //zum inserieren navigieren
        binding.cvInsert.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToInsertFragment())
        }

//NEU
        //zum chatten bzw. Nachrichten schreiben navigieren
        binding.btnMessage.setOnClickListener {
            Log.d("Detail", "AdvertismentId ${currentAdvertisementId.value.toString()}")
            findNavController().navigate(
                DetailFragmentDirections
                    //mit Übergabe der aktuellen Advertisment Id als Argument
                    .actionDetailFragmentToMessageFragment(currentAdvertisementId.value.toString()))
        }
    }

//==================================================================================================
//Observer für den Entfernungs API Call=============================================================
//==================================================================================================

    //Einbinden der ermittelten Entfernung zwischen Start und Ziel
    private fun distanceObserver() {
        viewModel.distanceData.observe(viewLifecycleOwner) {
            //Einbinden der DistanceMatrix Klasse
                distanceMatrix: DistanceMatrix? ->
            //Log.d("success Detail", "$distanceMatrix distanceObserver für die Api der Entfernung")
            //von der distanceMatrix über rows-Klasse zur element-Klasse bis zum Ziel distance-Klasse und dort das Textfeld mit den Kilometern
            distanceMatrix?.rows?.firstOrNull()?.elements?.firstOrNull()?.distance?.text?.let { distanceText ->
                //Verbinden des Textfeldes auf dem Button mit der Kilometerangabe
                _textDistance.value = "$distanceText bis zum Ziel"
                binding.btnGetDistance.text = textDistance.value
              //  Log.d("success Detail", "${binding.btnGetDistance.text} Text im Button")
            }
        }
    }

//==================================================================================================
//Observer für den Koordinaten API Call=============================================================
//==================================================================================================

    //Einbinden der ermittelten GeoDaten des Ziels
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
                //    Log.d("success geoObserver", "${thisGeo.longitude} Ziel Longitude")
                    //Hier werden die Koordinaten des Zieles in die Variablen gespeichert für Entfernungsabfrage
                    lat2 = thisGeo.latitude.toString()
                    lon2 = thisGeo.longitude.toString()
                }
            }
        }
    }

//==================================================================================================
//Feststellen der eigenen Location abhängig vom letzten Standort====================================
//==================================================================================================

    // über den fusedLocation Manager wird der eigene Standort ermittelt also der Startpunkt,
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
        //    Log.d("success SuccessListener", "${it.longitude} Start Longitude")
            if (it != null) {
                //Hier werden die Koordinaten des eigenen Standortes in die Variablen gespeichert für Entfernungsabfrage
                lat1 = it.latitude.toString()
                lon1 = it.longitude.toString()
            }
        }
    }
}

//==================================================================================================
//Ende================================Ende===================================Ende===================
//==================================================================================================
