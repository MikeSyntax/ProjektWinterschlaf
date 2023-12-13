package com.example.modulabschlussandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.databinding.ListItemSleepBinding
import com.example.modulabschlussandroid.ui.HomeFragmentDirections
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

//Der Adapter organisiert mit Hilfe der Viewholder das Recycling der Items
class AdapterObjects(

    //für die Room Datenbank
    private var dataset: List<Objects>,

    //für die Firebase Datenbank
    //private var dataset: List<Advertisement>,

    private var viewModel: ViewModelObjects

) : RecyclerView.Adapter<AdapterObjects.ItemViewHolder>() {

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

        //Ein bestimmtes Objekt an einer bestimmten Position
        val thisObject = dataset[position]

        //Abkürzung um nicht immer holder.binding zu schreiben
        val binding = holder.binding

        //Befüllen der ViewHolder
        binding.ivObject.setImageResource(thisObject.image1Resource)
        binding.tvObject.text = thisObject.objectdescription
        binding.tvCity.text = thisObject.city
        binding.tvPrice.text = "${thisObject.price.toString()} €"
        binding.tvDescription.text = thisObject.description
        binding.tvDistance.text = "PLZ ${thisObject.zipCode}"

        //Weiterleitung auf eine Detailseite
        binding.cvItemObject.setOnClickListener {

            //API Call wird gestartet mit Übergabe der Stadt
            val city: String? = thisObject.city
            if (city != null) {
                viewModel.getGeoResult(city)
            }

            //Navigation auf das aktuelle Objekt welches angeklickt wurde
            viewModel.setCurrentObject(thisObject)
            //ob die Navigation von Home oder Profil erfolgt, da Home Room und Profil Firebase
            viewModel.homeFragment = true
            //Navigieren
            val navController = binding.cvItemObject.findNavController()
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
        }
    }

    //Hier wird die Größe der Liste bzw. die Anzahl der Items für die RecyclerView ermittelt
    override fun getItemCount(): Int {
        return dataset.size
    }
}
