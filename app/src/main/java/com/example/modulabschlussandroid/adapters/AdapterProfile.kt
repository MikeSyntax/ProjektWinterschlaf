package com.example.modulabschlussandroid.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.modulabschlussandroid.data.datamodels.MyObject
import com.example.modulabschlussandroid.databinding.ListItemSleepBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects
import com.google.firebase.Firebase
import com.google.firebase.database.database

class AdapterProfile (
    private val viewModelObjects: ViewModelObjects,
    private val dataset: List<MyObject>,
):RecyclerView.Adapter<AdapterProfile.MyObjectViewHolder>(){

    inner class MyObjectViewHolder(val binding: ListItemSleepBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyObjectViewHolder {
       val binding = ListItemSleepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyObjectViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: MyObjectViewHolder, position: Int) {




    }
}