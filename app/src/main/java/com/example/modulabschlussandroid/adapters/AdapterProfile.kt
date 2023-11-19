package com.example.modulabschlussandroid.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.databinding.ListItemSleepBinding


class AdapterProfile(

    private var dataset: List<Advertisement>

) : RecyclerView.Adapter<AdapterProfile.AdvertisementViewHolder>() {

    inner class AdvertisementViewHolder(val binding: ListItemSleepBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisementViewHolder {
        val binding =
            ListItemSleepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvertisementViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("adapter", "Dataset size ${dataset.size}")
        return dataset.size
    }

    override fun onBindViewHolder(holder: AdvertisementViewHolder, position: Int) {

        val myAdvertisement = dataset[position]
        val binding = holder.binding

        binding.tvObject.text = myAdvertisement.title
        binding.tvCity.text = myAdvertisement.city
        binding.tvPrice.text = myAdvertisement.price
        binding.tvDescription.text = myAdvertisement.description
        binding.tvDistance.text = myAdvertisement.zipCode
    }

    fun update(list: List<Advertisement>){
        dataset = list
        notifyDataSetChanged()
        Log.d("adapter", "Dataset new size ${dataset.size}")
    }
}