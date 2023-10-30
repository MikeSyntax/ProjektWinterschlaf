package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.adapters.AdapterFavorite
import com.example.modulabschlussandroid.data.datamodels.apicall.Geo
import com.example.modulabschlussandroid.data.datamodels.apicall.Result
import com.example.modulabschlussandroid.databinding.FragmentDetailBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: ViewModelObjects by activityViewModels()

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

    //Einbinden der ermittelten GeoDaten
     fun geoObserver() {
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
                }
            }
        }
    }
}