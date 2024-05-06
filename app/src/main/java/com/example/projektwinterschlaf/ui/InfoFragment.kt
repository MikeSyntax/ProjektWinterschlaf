package com.example.projektwinterschlaf.ui

//==================================================================================================
//****************************         Info Fragment          **************************************
//==================================================================================================
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projektwinterschlaf.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}