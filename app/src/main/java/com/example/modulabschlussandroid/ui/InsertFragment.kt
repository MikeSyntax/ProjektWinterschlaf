package com.example.modulabschlussandroid.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.databinding.FragmentInsertBinding
import com.example.modulabschlussandroid.viewmodels.ViewModelObjects

class InsertFragment : Fragment() {

    private lateinit var binding: FragmentInsertBinding
    private val viewModel: ViewModelObjects by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.categoriesEdit.setOnClickListener {
            showDialog()
        }

        //Zu den Favoriten navigieren
        binding.cvFavorite.setOnClickListener {
            val navController = binding.cvFavorite.findNavController()
            navController.navigate(InsertFragmentDirections.actionInsertFragmentToFavoriteFragment())
        }

        //zu den Profil navigieren
        binding.cvProfile.setOnClickListener {
            findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToProfileFragment())
        }

        //zum inserieren navigieren
        binding.cvHome.setOnClickListener {
            findNavController().navigate(InsertFragmentDirections.actionInsertFragmentToHomeFragment())
        }

        //zum inserieren navigieren
        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {

        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

      //  val tvMessage: TextView = dialog.findViewById(R.id.edit_tv_in_cv_description)
      //  tvMessage.text = binding.editDescription.text

        val yesBtn: Button = dialog.findViewById(R.id.btn_save)

        yesBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Eingabe übernommen", Toast.LENGTH_SHORT).show()
        //    binding.editDescription.text = tvMessage.text as Editable?
            dialog.dismiss()
        }
        dialog.show()
    }
}