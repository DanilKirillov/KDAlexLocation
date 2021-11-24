package science.involta.project

// This is project demonstration location LATITUDE LONGITUDE implementation 'com.google.android.gms:play-services-location:18.0.0'

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity: AppCompatActivity() {
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var latitudeText: TextView? = null
    private var longitudeText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        latitudeText = findViewById(R.id.latitude)
        longitudeText = findViewById(R.id.longitude)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()

        if (!checkLocationPermission()) requestPermission()
        else getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result

                latitudeText?.text = location.latitude.toString()
                longitudeText?.text = location.longitude.toString()

                Log.d("LOCATION",  location.latitude.toString())
                Log.d("LOCATION",  location.longitude.toString())
            } else {
                Toast.makeText(this, "Нет данных о геолокации", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        val state = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return state == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_CODE)
    }

    private fun requestPermission() {
        val dontAskMeAgain = ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if (dontAskMeAgain) {
            //тут мы можем попросить пользователя, не будет
            //ли он против если мы ему покажем запрос на разрешение
            Toast.makeText(this, "Не запрашивать больше", Toast.LENGTH_SHORT).show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            if (grantResults.size <= 0) {
                Toast.makeText(this, "Пользователь отклонил запрос", Toast.LENGTH_SHORT).show()
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, "Пользователь запретил использование геолокации", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        private val REQUEST_CODE = 13
    }
}