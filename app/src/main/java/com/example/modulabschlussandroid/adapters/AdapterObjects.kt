package com.example.modulabschlussandroid.adapters

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

): RecyclerView.Adapter<AdapterObjects.ItemViewHolder>() {

    //Hier wird die View umfasst und der Listeneintrag dargestellt
    inner class ItemViewHolder(val binding: ListItemSleepBinding): RecyclerView.ViewHolder(binding.root)

    //Hier werden die ViewHolder erstellt, also die gerade sichtbaren ListItems ohne Inhalte nur das Gerüst
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       val binding = ListItemSleepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        if (thisObject.liked){
            binding.btnUnliked.setImageResource(R.drawable.star_liked)
        } else{
            binding.btnUnliked.setImageResource(R.drawable.star_unliked)
        }
        binding.btnUnliked.setOnClickListener {
            thisObject.liked = !thisObject.liked
            if (!thisObject.liked) {
                binding.btnUnliked.setImageResource(R.drawable.star_unliked)
            } else {
                binding.btnUnliked.setImageResource(R.drawable.star_liked)
            }
        }
    }

    //Hier wird die Größe der Liste bzw. die Anzahl der Items für die RecyclerView ermittelt
    override fun getItemCount(): Int {
     return dataset.size
    }

    //Änderungen werden bei Aufruf an das Dataset mitgeteilt
    fun sortObjects (list: List<Objects>){
        dataset = list
        notifyDataSetChanged()
    }
}
