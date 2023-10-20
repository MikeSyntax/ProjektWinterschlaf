package com.example.modulabschlussandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.modulabschlussandroid.R
import com.example.modulabschlussandroid.databinding.FragmentRegisterBinding
import com.example.modulabschlussandroid.data.SecretData

class RegisterFragment : Fragment() {

    // Erstellen einer Liste users aus der Klasse SecretData
    private var userProfile = SecretData()

    //Binding initialisieren
    private lateinit var binding: FragmentRegisterBinding

    //zur Prüfung der Passworteingabe mit Sonderzeichen und Zahlen
    private var validPassword = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //bei Klicken auf den Button bzw. auf die Cardview prüfe ...
        binding.cvForward.setOnClickListener {
            //überprüfen der Passworteingabe mit Sonderzeichen und Groß und klein, dann wird die Variabel validPassword auf true gesetzt und kann so in der if Abfrage genu
// TODO  // checkPasswordForLengthAndPassThrough()
            Log.e("cvForwardClickListener","passThrough")
            //Wenn alle Methoden nicht true sind,
            if (!checkInputName() && !checkEmail() && (!checkInputPassword() /*&& validPassword*/)) {
                exceptionMessage("Registrierung erfolgreich")
                //dann sollen die nachfolgenden Punkte gesetzt werden.
                userProfile.userName = binding.textInputUserAvatar.text.toString()
                userProfile.email = binding.textInputUserEmail.text.toString()
                userProfile.password = binding.textInputUserPassword.text.toString()
                findNavController().navigate(R.id.homeFragment)
                //andernfalls
            } else {
                exceptionMessage("Bitte überprüfe deine Eingabe")
            }
        }
    }
//Ende der onViewCreated

    //Methode für Fehlerausgabe als Toast
    private fun exceptionMessage(message: String) {
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(requireContext(), message, duration).show()
    }

    //Diverse Methoden zum checken der Eingaben
    private fun checkInputName(): Boolean {
        //Anlegen einer Variablen und Übergabe der eingabedaten im entsprechenden Editfeld
        val userInput = binding.textInputUserAvatar.text.toString()
        //Wenn die Liste an SecretData bereits den Usernamen enthält oder leer ist
        if (userProfile.userName.contains(userInput) || userInput.isEmpty()) {
            //gibt diese Fehlermeldung zurück
            exceptionMessage("Der Benutzername ist schon vergeben oder fehlt")
            //Gib true zurück
            return true
        }
        return false
    }

    private fun checkEmail(): Boolean {
        //Anlegen einer Variablen und Übergabe der eingabedaten im entsprechenden Editfeld
        val userEmail = binding.textInputUserEmail.text.toString()
        //Wenn die Liste an SecretData bereits die Email enthält oder leer ist
        if (userProfile.email.contains(userEmail) || userEmail.isEmpty()) {
            //gibt diese Fehlermeldung zurück
            exceptionMessage("Die Email ist schon vorhanden oder fehlt")
            //Gib true zurück
            return true
        }
        return false
    }

    private fun checkInputPassword(): Boolean {
        //Anlegen einer Variablen und Übergabe der eingabedaten im entsprechenden Editfeld
        val password = binding.textInputUserPassword.text.toString()
        //Anlegen einer Variablen und Übergabe der eingabedaten im entsprechenden Editfeld
        val controlPassword = binding.textInputUserSecondPassword.text.toString()
        //Prüfung ob beide Passwörter übereinstimmen
        if (password != controlPassword) {
            //falls nicht gib diese Nachricht zurück
            exceptionMessage("Die Passwörter stimmen nicht überein")
            //Gib true zurück
            return true
        }
        return false
    }

  /*  private fun checkPasswordForLengthAndPassThrough(): Boolean {

        val password = binding.textInputUserPassword
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=,.-_ß/]).{8,}$".toRegex()
        password.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(newText: Editable?){
                val password = newText.toString()
                val isValidPassword = passwordPattern.matches(password)

                if (isValidPassword) {
                    //bei richtiger Eingabe wird das validPasswort auf true gesetzt und kann so in der obigen Anfrage genutuzt werden
                   validPassword = true

                } else {
                    exceptionMessage("Passwort muss aus Groß- und Kleinschreibung, Zahlen und Sonderzeichen bestehen")

                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        return false
    }*/
}