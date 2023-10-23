package com.example.modulabschlussandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.modulabschlussandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Binding Initilisieren
    private lateinit var binding: ActivityMainBinding
/*
    //Kamera Capture initialisieren
    private lateinit var imageCapture: ImageCapture

    //Den Executor/Ausführung initialisieren
    private lateinit var cameraExecutor: ExecutorService
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



    /*
        cameraExecutor = Executors.newSingleThreadExecutor()
        try {
            startCamera()
        } catch (e: Exception) {
            Log.e("Ausführung", "Ausführung")
        }

        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            externalMediaDirs.first(),
            SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS",
                Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    // Handle error
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // Photo saved successfully
                }
            }
        )
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("startCamera", "startCamera funktion")
                // Handle any errors
            }
        }, ContextCompat.getMainExecutor(this))*/
    }


/*
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }*/
}
