package com.example.projektwinterschlaf.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projektwinterschlaf.R
import com.example.projektwinterschlaf.data.datamodels.Objects
import com.example.projektwinterschlaf.databinding.ListItemFavoriteBinding
import com.example.projektwinterschlaf.viewmodels.ViewModelObjects

class AdapterFavorite(

    private val dataset: List<Objects>,
    private val viewModel: ViewModelObjects,

    ) : RecyclerView.Adapter<AdapterFavorite.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ListItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ListItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Ein Objekt an einer bestimmten Stelle
        val favoriteObjects = dataset[position]
        //Nur zum Abkürzen
        val binding = holder.binding

        //Befüllen der ViewHolder
        binding.ivFavoriteObject1.setImageResource(favoriteObjects.image1Resource)
        binding.ivFavoriteObject2.setImageResource(favoriteObjects.image2Resource)
        binding.ivFavoriteObject3.setImageResource(favoriteObjects.image3Resource)
        binding.tvFavoriteCity.text = favoriteObjects.city
        binding.tvFavoriteDescription.text = favoriteObjects.description
        binding.tvFavoriteObject.text = favoriteObjects.objectdescription
        binding.tvFavoritePrice.text = favoriteObjects.price.toString()
        binding.tvDistance.text = "PLZ ${favoriteObjects.zipCode}"
        //Auslesen aus der Datenbank, ob das Objekt bereits geliked wurde oder nicht
        if (favoriteObjects.liked) {
            binding.ivFavoriteLiked.setImageResource(R.drawable.star_like)
        } else {
            binding.ivFavoriteLiked.setImageResource(R.drawable.star_unlike)
        }
        //Je nachdem ob geliked oder nicht, ändert sich der Zustand durch den Klick auf das Herz
        binding.ivFavoriteLiked.setOnClickListener {
            if (favoriteObjects != null) {
                favoriteObjects.liked = !favoriteObjects.liked
                if (!favoriteObjects.liked) {
                    binding.ivFavoriteLiked.setImageResource(R.drawable.star_unlike)
                    favoriteObjects.liked = false
                    viewModel.updateObjects(favoriteObjects)
                } else {
                    binding.ivFavoriteLiked.setImageResource(R.drawable.star_like)
                    favoriteObjects.liked = true
                    viewModel.updateObjects(favoriteObjects)
                }
            }
        }
    }
}