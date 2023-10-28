package com.example.modulabschlussandroid.ui

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.modulabschlussandroid.databinding.FragmentLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class LocationFragment : Fragment() {


    private lateinit var binding: FragmentLocationBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentLocationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())


        binding.button.setOnClickListener {
        fetchLocation()
        }
    }

    private fun fetchLocation() {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)

            return
        }
        task.addOnSuccessListener {
            if (it != null){
                binding.tvLatitude.text = it.latitude.toString()
                binding.tvLongitude.text = it.longitude.toString()
                Toast.makeText(requireContext(),"${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}