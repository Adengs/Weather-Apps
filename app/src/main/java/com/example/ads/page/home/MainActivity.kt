package com.example.ads.page.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.activity.viewModels
import com.example.ads.connection.RequestState
import com.example.ads.databinding.ActivityMainBinding
import com.example.ads.model.Response
import com.example.ads.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val weatherViewModel : WeatherViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val REQUEST_CODE_LOCATION_PERMISSION = 1
    private var latitude = ""
    private var longitude = ""
    private var appid = "246870ca0491e4f355fa3c139dd60029"
    private var lang = "ID"
    private var units = "metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        initSetup()
    }

    private fun initSetup() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Log.e("TAG", "Null Recieved")
                    } else {
                        Log.e("TAG", "Success")
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                        getData()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE_LOCATION_PERMISSION
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_LONG).show()
                initSetup()
            } else {
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getData() {
        val params = mutableMapOf<String, String>()
        params["lat"] = latitude
        params["lon"] = longitude
        params["appid"] = appid
        params["lang"] = lang
        params["units"] = units

        weatherViewModel.getWeather(params).observe(this){
            if (it != null){
                when(it){
                    is RequestState.Loading -> showLoading()
                    is RequestState.Success -> {
                        if (it.data != null){
                            setData(it.data)
                            hideLoading()
                        }
                    }
                    is RequestState.Error -> {
                        hideLoading()
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setData(data: Response?) {
        binding.kota.text = data?.name.toString()
        binding.suhu.text = data?.main?.temp.toString() + "°C"
        binding.desc.text = data?.weather?.get(0)?.description.toString()
        binding.wind.text = data?.wind?.speed.toString()
        binding.humidity.text = data?.main?.humidity.toString()
        binding.visibility.text = data?.visibility.toString()
        binding.air.text = data?.main?.pressure.toString()

        var iconCode = ""
        if (data?.weather?.get(0)?.icon == "01d") {
            iconCode = "https://openweathermap.org/img/wn/01d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "01n") {
            iconCode = "https://openweathermap.org/img/wn/01n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "02d") {
            iconCode = "https://openweathermap.org/img/wn/02d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "02n") {
            iconCode = "https://openweathermap.org/img/wn/02n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "03d") {
            iconCode = "https://openweathermap.org/img/wn/03d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "03n") {
            iconCode = "https://openweathermap.org/img/wn/03n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "04d") {
            iconCode = "https://openweathermap.org/img/wn/04d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "04n") {
            iconCode = "https://openweathermap.org/img/wn/04n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "09d") {
            iconCode = "https://openweathermap.org/img/wn/09d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "09n") {
            iconCode = "https://openweathermap.org/img/wn/09n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "10d") {
            iconCode = "https://openweathermap.org/img/wn/10d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "10n") {
            iconCode = "https://openweathermap.org/img/wn/10n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "11d") {
            iconCode = "https://openweathermap.org/img/wn/11d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "11n") {
            iconCode = "https://openweathermap.org/img/wn/11n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "13d") {
            iconCode = "https://openweathermap.org/img/wn/13d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "13n") {
            iconCode = "https://openweathermap.org/img/wn/13n@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "50d") {
            iconCode = "https://openweathermap.org/img/wn/50d@2x.png"
        }
        if (data?.weather?.get(0)?.icon == "50n") {
            iconCode = "https://openweathermap.org/img/wn/50n@2x.png"
        }

        Picasso.get().load(iconCode).into(binding.logo)
    }

    private fun showLoading(){
        binding.pBar.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        binding.pBar.visibility = View.GONE
    }
}