package com.example.modulabschlussandroid.repositorys

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class RepositoryFirebase(

) {

    private var firebaseAuth = FirebaseAuth.getInstance()

    private var fireStoreDatabase = FirebaseFirestore.getInstance()

    private var firebaseStorage = FirebaseStorage.getInstance()

//Firebase Firestore================================================================================


    //Live Data des aktuellen Users Id
    private var _uId: MutableLiveData<String> = MutableLiveData()
    val uId: MutableLiveData<String>
        get() = _uId

    //LiveData für den aktuellen user und alle Daten über den User
    private var _currentUser: MutableLiveData<PersonalData> = MutableLiveData()
    val currentUser: LiveData<PersonalData>
        get() = _currentUser

    //Um festzustellen, ob ein User bereits eingeloggt ist
    private var _currentAppUser: MutableLiveData<String?> = MutableLiveData(null)
    val currentAppUser: MutableLiveData<String?>
        get() = _currentAppUser

    //Funktion um Festzustellen, ob ein User eingeloggt ist oder nicht==================================
    fun currentAppUserLogged() {
        if (firebaseAuth.currentUser != null) {
            _currentAppUser.value = "loggedIn"
        }
    }

    //Funktion um Profilbilder in das Storage hochzuladen=====================================================
    fun uploadImagetoStorage(uri: Uri) {
        val ref = firebaseStorage.reference.child("imageProfile/${firebaseAuth.currentUser!!.uid}")
        ref.putFile(uri)
        _currentUser.value?.copy(profileImage = uri.toString())?.let { updateUser(it) }
    }

    //Funktion um den User nach Änderungen upzudaten
    fun updateUser(personalData: PersonalData) {
        fireStoreDatabase.collection("user")
            .document(firebaseAuth.currentUser?.uid!!)
            .set(
                //mit der Funktion aus der der Datenklasse PersonalData!!
                personalData.toFirebase()
            ).addOnSuccessListener {
                Log.d("fireRepo", "udateUser Profile done $it")
                _currentUser.postValue(personalData)
            }.addOnFailureListener {
                Log.d("fireRepo", "udateUser Profile failed $it")
            }
    }


    //Funktion um den aktuellen User upzudaten und die Daten aus dem Firestore zu holen und auf der Profilseite anzuzeigen
    fun updateCurrentUserFromFirestore() {
        fireStoreDatabase.collection("user").document(uId.value.toString())
            .get()

            .addOnSuccessListener { thisUser ->
               // Log.d("Firebase Repo Store", "SuccesListener FireStore done - Id ${uId.value.toString()} ")
                _currentUser.value = PersonalData(
                    thisUser.id,
                    thisUser.data?.get("cityName").toString(),
                    thisUser.data?.get("countInsertedItems").toString(),
                    thisUser.data?.get("itemsDone").toString(),
                    thisUser.data?.get("name").toString(),
                    thisUser.data?.get("preName").toString(),
                    thisUser.data?.get("registered").toString(),
                    thisUser.data?.get("streetName").toString(),
                    thisUser.data?.get("streetNumber").toString(),
                    thisUser.data?.get("telNumber").toString(),
                    thisUser.data?.get("userName").toString(),
                    thisUser.data?.get("zipCode").toString(),
                    thisUser.data?.get("profileImage").toString(),
                )
                //Log.d("Firebase Repo Add", "User items Done?? ${currentUser.value}")
                // Log.e("Repo", " User Data set failed in firestore")
            }.addOnFailureListener {
              //  Log.e("Firebase Repo Store", "FailureListener $it")
            }
    }

    //Hinzufügen eines weiteren Inserats bei der Zahl "Meine bisherigen.." bei einem bestimmten User====
    fun addCounterForAdvertises(personalData: PersonalData) {
        // Überprüfen, ob itemsDone nicht null ist
        if (personalData.itemsDone != null) {
            // Hole Dir die aktuelle Anzahl der bisherigen Inserate
            fireStoreDatabase.collection("user")
                .document(uId.value.toString())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    // Überprüfe, ob das Dokument Daten enthält und das itemsDone-Feld existiert
                    if (documentSnapshot.exists() && documentSnapshot.contains("itemsDone")) {
                        val currentCount = documentSnapshot["itemsDone"].toString().toInt()

                        // Addiere die Anzahl für jedes weitere Inserat
                        val countSize = currentCount + 1

                        // Datentyp wird in eine verarbeitbare Version für Firebase umgewandelt mit der to Methode
                        val setCounter = hashMapOf<String, Any>("itemsDone" to countSize.toString())

                        fireStoreDatabase.collection("user")
                            // Hier wird die User Id aus der Auth übergeben, da sonst eine autogenerated Id übergeben wird
                            .document(uId.value.toString())
                            .update(setCounter)
                            .addOnSuccessListener {
                               // Log.d("Repo Firebase", "Anzahl bisheriger Inserate $countSize")
                                personalData.itemsDone = countSize.toString()
                            }
                            .addOnFailureListener { error ->
                              //  Log.e("Repo Firebase", "Error adding counter $error")
                            }
                    } else {
                       // Log.e("Repo Firebase", "Dokument enthält kein itemsDone-Feld.")
                    }
                }
                .addOnFailureListener { error ->
                   // Log.e("Repo Firebase", "Error getting document $error")
                }
        } else {
           // Log.e("Repo Firebase", "itemsDone ist null.")
            // Handle den Fall, dass itemsDone null ist
        }
    }


    //Funktion um einen neuen User im Firestore anzulegen (EditProfile)=================================
    fun newUserDataFirstSignIn(personalData: PersonalData) {
        fireStoreDatabase.collection("user")
            //Hier wird die User Id aus der Auth übergeben, da sonst eine autogenerated Id übergeben wird
            .document(uId.value.toString())
            .set(personalData)
            .addOnSuccessListener { documentRef ->
              //  Log.d("Repo Firebase", "DocumentSnapshot added $documentRef")
            }
            .addOnFailureListener { error ->
               // Log.e("Repo Firebase", "Error adding document $error")
            }
    }

//Firebase Authentication===========================================================================

    //Funktion für den Login ===========================================================================
    //Da die Funktion im Repository ist, muss hier falls der Login erfolgreich ist und nicht sofort
    // weitergeleitet werden kann , eine Task als String "succes" (oder was auch immer) übergeben
    // werden und so kann im Fragment dann ein OnSuccessListener installiert werden
    fun login(email: String, password: String, context: Context): Task<String> {
        //Instanz der TaskSource erstellen als String
        val completionSource = TaskCompletionSource<String>()
        //Prüfung der Eingaben
        if (email.isNotEmpty() && password.isNotEmpty()) {
            //Einloggen mit Email und Passwort
            firebaseAuth.signInWithEmailAndPassword(email, password)
                //falls erfolgreich...
                .addOnCompleteListener {
                    //Wenn der Login erfolgreich war...
                    if (it.isSuccessful) {
                        //... dann setze die completionSource auf erfolgreich
                        completionSource.setResult("succes")
                    } else {
                        Toast.makeText(
                            context,
                            "Error ${it.exception.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(context, "Leere Felder sind nicht erlaubt", Toast.LENGTH_SHORT)
                .show()
        }
        //returne die Task und so kann man in jedem fragment darauf zugreifen
        return completionSource.task
    }

    //Registrierung eines neuen Users mit Erfolgsübergabe um die Weiterleitung zu ermöglichen===========
    fun register(
        email: String,
        password: String,
        passConfirmation: String,
        context: Context
    ): Task<String> {
        val completionSource = TaskCompletionSource<String>()
        //Prüfung der Eingaben
        if (email.isNotEmpty() && password.isNotEmpty() && passConfirmation.isNotEmpty()) {
            if (password == passConfirmation) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            completionSource.setResult("success")
                        } else {
                            Toast.makeText(
                                context,
                                it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    context,
                    "Passwörter stimmen nicht überein",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            Toast.makeText(
                context,
                "Bitte alle Felder ausfüllen",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        return completionSource.task
    }

    //Update der aktuellen Users Id
    fun showCurrentUserId() {
        val user = firebaseAuth.currentUser?.uid.toString()
        _uId.value = user
       // Log.d("Firebase Repo 3 Punkte", "_uId ${_uId.value} user $user uId ${uId.value}")
    }

    //Ausloggen des aktuellen Users
    fun signOutUser() {
        //Ausloggen
        firebaseAuth.signOut()
        _currentAppUser.value = null
    }

//Firebase Firestore Erstellen eines Inserates======================================================

    fun saveItemToDatabase(advertisement: Advertisement) {
        fireStoreDatabase.collection("objectsOnline")
            .document().set(
                advertisement
            )
            .addOnSuccessListener {
              //  Log.d("Savetodatabase", advertisement.toString())
            }.addOnFailureListener {
            //    Log.e("Savetodatabse", it.toString())
            }
    }

//Abfrage in der Firebase Database wieviele Anzeigen von ALLEN im Moment online sind================

    //LiveData für den aktuellen user und alle Daten über den User
    private var _countAdvertises: MutableLiveData<String> = MutableLiveData()
    val countAdvertises: LiveData<String>
        get() = _countAdvertises

    //Funktion zur Abfrage der aktuellen Inserate der ganzen Community
    fun countAdvertises() {

        //und eine Reference setzten in der Kategorie objectsOnline
        fireStoreDatabase.collection("objectsOnline").get().addOnSuccessListener {
            _countAdvertises.postValue("Anzahl aller Inserate ${it.size()}")
        }
    }

//Abfrage in der Firebase Database aller MEINER Anzeigen gerade online sind=========================

    //Live Data für die ausgelesenenen Advertisments
    private var _allMyAdvertises: MutableLiveData<List<Advertisement>> = MutableLiveData()
    val allMyAdvertises: LiveData<List<Advertisement>>
        get() = _allMyAdvertises

    //Prüfen aller Inserate welche von dem eingeloggten User gerade online sind und anzeigen
    fun checkDatabaseForMyAds() {
        //Leere Liste für die Advertises
        val advertise: MutableList<Advertisement> = mutableListOf()
        //und eine Reference setzten in der Kategorie objectsOnline
        fireStoreDatabase.collection("objectsOnline")
            .get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    //Füge dieses ausgelesene der advertise Lise hinzu
                    advertise.add(Advertisement(doc))
                    //filtere die Liste nach der UserId und füge die in Übereinstimmung der neuen Liste hinzu
                    val filteredAds = advertise.filter { myAds ->
                        //vergleiche die Ids
                        myAds.userId == uId.value
                    }
                    //die gefilterte Liste mit dem Live Data setzen
                    _allMyAdvertises.value = filteredAds
                    //  Log.d("Firebase Repo Data Schleife", "Alle id´s ${allAdvertises.value}")
                }
            }.addOnFailureListener {
                //Log.d("Firebase Repo Data", "Error $it")
            }
    }

    //Falls der User neu ist, zeige den Dialog für die Datenerfassung===================================
    fun checkUserDataComplete(uId: String): Task<String> {
        val completionSource = TaskCompletionSource<String>()
        //Abfrage Firestore
        fireStoreDatabase.collection("user")
            //ob dieses Document mit der Id
            .document(uId)
            //suchen
            .get()
            //Listener
            .addOnCompleteListener { task ->
                //Abfrage erfolgreich
                if (task.isSuccessful) {
                    //Resultat der Abfrage
                    val document = task.result
                   // Log.d("success Home", "uId $uId task result ${task.result}")
                    //wenn diese Id bzw. dieses Document NICHT existiert
                    if (!document.exists()) {
                        // dann zeige den Dialog für die User Daten vervollständigung
                        //showNewUserDialog()
                        completionSource.setResult("not existing")
                    }
                }
            }
        return completionSource.task
    }
}
//==================================================================================================
