package com.example.modulabschlussandroid.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.databinding.ListItemSleepBinding
import com.example.modulabschlussandroid.ui.HomeFragmentDirections
import com.example.modulabschlussandroid.ui.ProfileFragmentDirections
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects


class AdapterProfile(

    private var dataset: List<Advertisement>,
    private val viewModel: ViewModelObjects

) : RecyclerView.Adapter<AdapterProfile.AdvertisementViewHolder>() {

    inner class AdvertisementViewHolder(val binding: ListItemSleepBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisementViewHolder {
        val binding =
            ListItemSleepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvertisementViewHolder(binding)
    }

    override fun getItemCount(): Int {
      //  Log.d("adapter", "Dataset size ${dataset.size}")
        return dataset.size
    }

    override fun onBindViewHolder(holder: AdvertisementViewHolder, position: Int) {

        val myAdvertisement = dataset[position]
        val binding = holder.binding

        binding.tvObject.text = myAdvertisement.title
        binding.tvCity.text = myAdvertisement.city
        binding.tvPrice.text = "${myAdvertisement.price} €"
        binding.tvDescription.text = myAdvertisement.description
        binding.tvDistance.text = "PLZ ${myAdvertisement.zipCode}"



        //Weiterleitung auf eine Detailseite
        binding.cvItemObject.setOnClickListener {

            //API Call wird gestartet mit Übergabe der Stadt
            val city: String = myAdvertisement.city!!
            viewModel.getGeoResult(city)

            //Navigation auf das  aktuelle Objekt welches angeklickt wurde
            viewModel.setCurrentAdvertisment(myAdvertisement)
            val navController = binding.cvItemObject.findNavController()
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToDetailFragment())
        }
    }


    fun update(list: List<Advertisement>){
        dataset = list
        notifyDataSetChanged()
      //  Log.d("adapter", "Dataset new size ${dataset.size}")
    }
}