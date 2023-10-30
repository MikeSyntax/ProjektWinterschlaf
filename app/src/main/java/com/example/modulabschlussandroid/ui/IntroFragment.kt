package com.example.modulabschlussandroid.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {
    private lateinit var binding: FragmentIntroBinding

    private var userProfile = PersonalData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Variable für die Dauer des Progress
        val timeDuration: Long = 4000

        //Einbinden der ProgressBar mit Parameterübergabe für die Zustände
        binding.progressBar.apply {
            //Max 100 Float
            progressMax = 100f
            //Die ProgressBar soll sich ganz schließen also 100% und das in der Dauer der Variablen
            setProgressWithAnimation(100f, timeDuration)
            //Größe der ProgressBar in Float
            progressBarWidth = 5f
            //Farbe
            progressBarColor = Color.GREEN
            //Größe des Hintergrundes der ProgressBar in Float
            backgroundProgressBarWidth = 5f
            //Farbe
            backgroundProgressBarColor = Color.DKGRAY
        }
        //Erstellen einer Instanz der Klasse Handler, ein Handler wird verwendet um den Code in einem bestimmten Zeitplan auszuführen
        //der Looper.getMainLoopper gibt mir den Looper des Hauptthread und stellt sicher, dass Ausführungen  auf der Oberfläche nicht blockiert werden.
        //postDelayed mit dieser Methode wird die Ausführung der Funktion in der klammer nach einer bestimmt Zeit ausgeführt.
        Handler(Looper.getMainLooper()).postDelayed({
            //Funktion zum Seitenwechsel
            navigate()
            //Ausführung nach dieser Zeit
        }, timeDuration)
    }

    //Funktion zu Navigation auf die gewünschte Seite
    private fun navigate() {
        //Falls eingeloggt, weiterleitung zum HomeScreen
        if (userProfile.loggedIn) {
            findNavController().navigate(R.id.homeFragment)
        //Falls nicht eingeloggt, dann direct zum LoginScreen
        } else {
            findNavController().navigate(R.id.logInFragment)
        }
    }
}