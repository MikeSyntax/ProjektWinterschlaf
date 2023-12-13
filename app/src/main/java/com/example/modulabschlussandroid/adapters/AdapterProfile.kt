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

    //Firebase Datenbank
    private var dataset: List<Advertisement>,
    private val viewModel: ViewModelObjects

) : RecyclerView.Adapter<AdapterProfile.AdvertisementViewHolder>() {

    //Hier wird die View umfasst und der Listeneintrag dargestellt
    inner class AdvertisementViewHolder(val binding: ListItemSleepBinding) : RecyclerView.ViewHolder(binding.root)

    //Hier werden die ViewHolder erstellt, also die gerade sichtbaren ListItems ohne Inhalte nur das Gerüst
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisementViewHolder {
        val binding =
            ListItemSleepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvertisementViewHolder(binding)
    }

    //Ermitteln der Größe des Datensatzes
    override fun getItemCount(): Int {
      //  Log.d("adapter", "Dataset size ${dataset.size}")
        return dataset.size
    }

    //Hier werden die ViewHolder also die leeren Gerüste mit Daten befüllt
    override fun onBindViewHolder(holder: AdvertisementViewHolder, position: Int) {

        //Ein bestimmtes Objekt an einer bestimmten Position
        val myAdvertisement = dataset[position]
        //Abkürzung um nicht immer holder.binding zu schreiben
        val binding = holder.binding

        //Befüllen der ViewHolder
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
            viewModel.setCurrentAdvertisement(myAdvertisement)
            //Navigation von Home also Room oder von Profil also Firebase
            viewModel.homeFragment = false
            //Navigation
            val navController = binding.cvItemObject.findNavController()
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToDetailFragment())
        }
    }

    //Udaten der Liste da sich etwas verändert hat
    fun update(list: List<Advertisement>){
        dataset = list
        notifyDataSetChanged()
      //  Log.d("adapter", "Dataset new size ${dataset.size}")
    }
}