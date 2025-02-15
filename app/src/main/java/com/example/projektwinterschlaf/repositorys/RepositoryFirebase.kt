package com.example.projektwinterschlaf.repositorys

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projektwinterschlaf.data.datamodels.Advertisement
import com.example.projektwinterschlaf.data.datamodels.PersonalData
import com.example.projektwinterschlaf.data.datamodels.chat.Message
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RepositoryFirebase(

) {

    private var firebaseAuth = FirebaseAuth.getInstance()

    private var fireStoreDatabase = FirebaseFirestore.getInstance()

    private var firebaseStorage = FirebaseStorage.getInstance()

//==================================================================================================
//Firebase LiveData=================================================================================
//==================================================================================================

    //Live Data des aktuellen Users Id
    private var _uId: MutableLiveData<String> = MutableLiveData()
    val uId: MutableLiveData<String>
        get() = _uId

    //LiveData für den aktuellen user und alle Daten über den User
    private var _currentUser: MutableLiveData<PersonalData> = MutableLiveData()
    val currentUser: LiveData<PersonalData>
        get() = _currentUser

    //LiveData um festzustellen, ob ein User bereits eingeloggt ist
    private var _currentAppUser: MutableLiveData<String?> = MutableLiveData(null)
    val currentAppUser: MutableLiveData<String?>
        get() = _currentAppUser

    //Live Data für die ausgelesenenen Advertisments
    private var _allMyAdvertises: MutableLiveData<List<Advertisement>> = MutableLiveData()
    val allMyAdvertises: LiveData<List<Advertisement>>
        get() = _allMyAdvertises

    //LiveData für den aktuellen user und alle Daten über den User
    private var _countAdvertises: MutableLiveData<String> = MutableLiveData()
    val countAdvertises: LiveData<String>
        get() = _countAdvertises

    //LiveData um festzustellen, welches Inserat gerade geöffnet ist, wird hier die ObjectId als LiveData gespeichert
    private var _currentAdvertismentId: MutableLiveData<String?> = MutableLiveData(null)
    val currentAdvertisementId: MutableLiveData<String?>
        get() = _currentAdvertismentId

    //LiveData für die Nachrichten einer bestimmten Advertisement Id
    private var _myMessages: MutableLiveData<List<Message>> = MutableLiveData()
    val myMessage: LiveData<List<Message>>
        get() = _myMessages

    //LiveData für alle Inserte der gesamten User
    private var _allUserAdvertisements: MutableLiveData<List<Advertisement>> = MutableLiveData()
    val allUserAdvertisements: LiveData<List<Advertisement>>
        get() = _allUserAdvertisements

//==================================================================================================
//Firebase Authentication===========================================================================
//==================================================================================================

//Funktion um Festzustellen, ob ein User eingeloggt ist oder nicht==================================
    fun currentAppUserLogged() {
        if (firebaseAuth.currentUser != null) {
            _currentAppUser.value = "loggedIn"
        }
    }

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
                            /*  val personalData = PersonalData()
                              personalData.userId = it.result.user!!.uid
                              saveUserData(personalData)*/
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

//Update der aktuellen Users Id=====================================================================
    fun showCurrentUserId() {
        val user = firebaseAuth.currentUser?.uid.toString()
        _uId.value = user
        // Log.d("Firebase Repo 3 Punkte", "_uId ${_uId.value} user $user uId ${uId.value}")
    }

//Ausloggen des aktuellen Users=====================================================================

    fun signOutUser() {
        //Ausloggen
        firebaseAuth.signOut()
        _currentAppUser.value = null
    }

//==================================================================================================
//Firebase Storage==================================================================================
//==================================================================================================

//Funktion um Profilbilder in das Storage hochzuladen===============================================
    fun uploadImagetoStorage(uri: Uri) {
        val ref = firebaseStorage.reference.child("imageProfile/${firebaseAuth.currentUser!!.uid}")
        ref.putFile(uri).addOnSuccessListener { Task ->
            ref.downloadUrl.addOnSuccessListener { Uri ->
                // Log.d("repositr...", "Uri $Uri")
                if (Uri.toString() != currentUser.value?.profileImage) {
                    _currentUser.value?.copy(profileImage = Uri.toString())
                        .let { saveUserData(it!!) }
                }
            }.addOnFailureListener {
                //     Log.d("repositr...", "Error bild nicht hochgeladen")
            }
        }
    }

//==================================================================================================
//Firebase Firestore================================================================================
//==================================================================================================

    //Funktion um den aktuellen User upzudaten und die Daten aus dem Firestore zu holen und auf der Profilseite anzuzeigen
    fun updateCurrentUserFromFirestore() {
        fireStoreDatabase.collection("user").document(uId.value.toString())
            .get()
            .addOnSuccessListener { thisUser ->
                _currentUser.value = PersonalData(
                    thisUser.id,
                    thisUser.data?.get("cityName").toString(),
                    thisUser.data?.get("itemsDone").toString(),
                    thisUser.data?.get("name").toString(),
                    thisUser.data?.get("preName").toString(),
                    thisUser.data?.get("streetName").toString(),
                    thisUser.data?.get("streetNumber").toString(),
                    thisUser.data?.get("telNumber").toString(),
                    thisUser.data?.get("userName").toString(),
                    thisUser.data?.get("zipCode").toString(),
                    thisUser.data?.get("profileImage").toString(),
                )
            }.addOnFailureListener {
                //  Log.e("Firebase Repo Store", "FailureListener $it")
            }
    }

//Hinzufügen eines weiteren Inserats bei der Zahl "Meine bisherigen.." bei einem bestimmten User====
    fun addCounterForAdvertises(personalData: PersonalData) {
        personalData.itemsDone = personalData.itemsDone?.toInt()?.plus(1).toString()
        fireStoreDatabase.collection("user")
            .document(personalData.userId)
            .set(personalData)
            .addOnSuccessListener {
                _currentUser.postValue(personalData)
            }
    }

//Funktion um einen neuen User im Firestore anzulegen (EditProfile)=================================
    fun saveUserData(personalData: PersonalData) {
        fireStoreDatabase.collection("user")
            //Hier wird die User Id aus der Auth übergeben, da sonst eine autogenerated Id übergeben wird
            .document(uId.value.toString())
            .set(personalData)
            .addOnSuccessListener { documentRef ->
                _currentUser.postValue(personalData)
                //  Log.d("Repo Firebase", "DocumentSnapshot added $documentRef")
            }
            .addOnFailureListener { error ->
                // Log.e("Repo Firebase", "Error adding document $error")
            }
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

//Firebase Firestore Nachricht aus dem MessageFragment speichern NOCH NICHT WEITERGEMACHT===========

    fun saveMessageToDatabase(message: Message) {
        fireStoreDatabase.collection("objectsOnline")
            .document(currentAdvertisementId.value.toString())
            .collection("chat").add(
                message
            )
            .addOnSuccessListener {
                Log.d("Repo", "Message erfolgreich gespeichert $message")
            }
            .addOnFailureListener {
                Log.d("Repo", "Message NICHT gespeichert $it")
            }
    }

//Funktion im die Advertisment Id in einer Variablen zu speichern und im Chat zu nutzen=============
    fun getAllAdId() {
        var objectId: String
        fireStoreDatabase.collection("objectsOnline")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    objectId = document.id
                    updateAdId(objectId)
                    // Log.d("Repo", "$objectId")
                }
            }.addOnFailureListener {
                // Log.e("Repo", it.toString())
            }
    }

//Funktion um die ObjectId in dem erstellten Advertisment zu updaten================================
    private fun updateAdId(objectId: String) {
        val setAdId = hashMapOf<String, Any>("objectId" to objectId)
        fireStoreDatabase.collection("objectsOnline")
            .document(objectId)
            .update(setAdId)
            .addOnSuccessListener {
                //    Log.d("Repo", "$setAdId")
            }
    }

    //Konvertieren der Uhrzeit von Milli und Nanosekunden, zu normaler Uhrzeit
    fun convertTimestampToDateTime(timestamp: com.google.firebase.Timestamp): String {
        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val netDate = Date(milliseconds)
        return sdf.format(netDate)
    }

//Abfrage in der Firebase Database aller meiner Nachrichten=========================================
    fun checkMessages() {
        val messages: MutableList<Message> = mutableListOf()
        try {
            fireStoreDatabase.collection("objectsOnline")
                .document(currentAdvertisementId.value.toString())
                .collection("chat")
                .get()
                .addOnSuccessListener {snapshot ->
                    messages.clear()
                    snapshot.documents.forEach { thisMessage ->
                        messages.add(Message(thisMessage))
                    }
                        _myMessages.postValue(messages.sortedByDescending { it.timestamp })
                }
        } catch (e: Exception) {
          //  Log.e("Repo", "Error checkMessages $e")
        }
    }

//Funktion um die aktuelle Advertisment Id in einer Variablen zu speichern und im Chat zu nutzen====
    fun getAdvertisementId(advertisement: Advertisement) {
        fireStoreDatabase.collection("objectsOnline")
            .document()
            .get()
            .addOnSuccessListener {
                _currentAdvertismentId.value = advertisement.objectId
                //Log.d("Repo", " currentAdId ${currentAdvertisementId.value}")
            }
            .addOnFailureListener {
                // Log.e("Repo", it.toString())
            }
    }

//Abfrage in der Firebase Database wieviele Anzeigen von ALLEN im Moment online sind================

    //Funktion zur Abfrage der aktuellen Inserate der ganzen Community
    fun countAdvertises() {
        //und eine Reference setzten in der Kategorie objectsOnline
        fireStoreDatabase.collection("objectsOnline").get().addOnSuccessListener {
            _countAdvertises.postValue("Inserate aller User ${it.size()}")
        }
    }

//Prüfen aller Inserate welche von dem eingeloggten User gerade online sind und anzeigen============
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
                    // Log.d("Firebase Repo Data Schleife", "Alle id´s ${allMyAdvertises.value}")
                }
            }.addOnFailureListener {
                //Log.d("Firebase Repo Data", "Error $it")
            }
    }

    //Anzeigen aller Inserate auf dem HomeFragmentzeigen, sobald Room Datenbank erledigt ist
    fun showAllUserAdvertisements() {
        //Leere Liste für die Advertises
        val allAdvertise: MutableList<Advertisement> = mutableListOf()
        //und eine Reference setzten in der Kategorie objectsOnline
        fireStoreDatabase.collection("objectsOnline")
            .get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    //Füge dieses ausgelesene der advertise Lise hinzu
                    allAdvertise.add(Advertisement(doc))
                }
                //die gefilterte Liste mit dem Live Data setzen
                _allUserAdvertisements.postValue(allAdvertise)
                //Log.d("Fire Repo", "Alle id´s ${allMyAdvertises.value}")
            }
            .addOnFailureListener {
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
                        //     if (document.getBoolean("userDataComplete") == false) {
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
//Ende===================================Ende===============================Ende====================
//==================================================================================================
