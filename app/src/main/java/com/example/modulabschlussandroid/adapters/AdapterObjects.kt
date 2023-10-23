package com.example.modulabschlussandroid.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.databinding.ListItemSleepBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

//Der Adapter organisiert mit Hilfe der Viewholder das Recycling der Items
class AdapterObjects(

    private var dataset: List<Objects>,
    private var viewModel: ViewModelObjects

) : RecyclerView.Adapter<AdapterObjects.ItemViewHolder>() {

    //Hier wird die View umfasst und der Listeneintrag dargestellt
    inner class ItemViewHolder(val binding: ListItemSleepBinding) :
        RecyclerView.ViewHolder(binding.root)

    //Hier werden die ViewHolder erstellt, also die gerade sichtbaren ListItems ohne Inhalte nur das Gerüst
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ListItemSleepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        binding.tvPrice.text = thisObject.price.toString()
        binding.tvDescription.text = thisObject.description
        binding.ivLiked.setImageResource(R.drawable.star_unliked)

        if (thisObject.liked) {
            binding.ivLiked.setImageResource(R.drawable.star_liked)
        } else {
            binding.ivLiked.setImageResource(R.drawable.star_unliked)
        }
        binding.ivLiked.setOnClickListener {
            thisObject.liked = !thisObject.liked
            try {
                if (!thisObject.liked) {
                    binding.ivLiked.setImageResource(R.drawable.star_unliked)
                    viewModel.updateObjects(Objects(
                        city = thisObject.city,
                        image1Resource = thisObject.image1Resource,
                        image2Resource = thisObject.image2Resource,
                        image3Resource = thisObject.image3Resource,
                        image4Resource = thisObject.image4Resource,
                        image5Resource = thisObject.image5Resource,
                        description = thisObject.description,
                        objectdescription = thisObject.objectdescription,
                        price = thisObject.price,
                        liked = false))
                } else {
                    binding.ivLiked.setImageResource(R.drawable.star_liked)
                    viewModel.updateObjects(Objects(
                        city = thisObject.city,
                        image1Resource = thisObject.image1Resource,
                        image2Resource = thisObject.image2Resource,
                        image3Resource = thisObject.image3Resource,
                        image4Resource = thisObject.image4Resource,
                        image5Resource = thisObject.image5Resource,
                        description = thisObject.description,
                        objectdescription = thisObject.objectdescription,
                        price = thisObject.price,
                        liked = true))
                }
            } catch (e: Exception) {
                Log.e("Adapter", "update fehlgeschlagen")
            }
        }
    }

    //Hier wird die Größe der Liste bzw. die Anzahl der Items für die RecyclerView ermittelt
    override fun getItemCount(): Int {
        return dataset.size
    }

    //Änderungen werden bei Aufruf an das Dataset mitgeteilt
    fun sortObjects(list: List<Objects>) {
        dataset = list
        notifyDataSetChanged()
    }
}
