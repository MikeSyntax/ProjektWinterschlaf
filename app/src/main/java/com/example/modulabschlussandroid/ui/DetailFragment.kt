package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.databinding.FragmentDetailBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

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

        viewModel.currentObject.observe(viewLifecycleOwner) { thisObject ->
            binding.tvDetailCity.text = thisObject.city
            binding.tvDetailDescription.text = thisObject.description
            binding.tvDetailObject.text = thisObject.objectdescription
            binding.tvDetailPrice.text = "${thisObject.price.toString()}€"
            binding.ivDetailObject1.setImageResource(thisObject.image1Resource)
            binding.ivDetailObject2.setImageResource(thisObject.image2Resource)
            binding.ivDetailObject3.setImageResource(thisObject.image3Resource)

            // TODO muss aus der Datenbank geholt werden
            binding.ivDetailLiked.setImageResource(R.drawable.star_unliked)

            // TODO Liked or Unlinked

            if (thisObject.liked) {
                binding.ivDetailLiked.setImageResource(R.drawable.star_liked)
            } else {
                binding.ivDetailLiked.setImageResource(R.drawable.star_unliked)
            }


            binding.ivDetailLiked.setOnClickListener {
                thisObject.liked = !thisObject.liked

                if (!thisObject.liked) {
                    binding.ivDetailLiked.setImageResource(R.drawable.star_unliked)
                    viewModel.updateObjects(false)
                } else {
                    binding.ivDetailLiked.setImageResource(R.drawable.star_liked)
                    viewModel.updateObjects(true)
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

        binding.ivDetailLiked.setOnClickListener {
            viewModel.deleteById()
        }
    }


}