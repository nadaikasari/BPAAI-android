package com.nada.bpaai.ui.maps

import android.content.ContentValues
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.nada.bpaai.R
import com.nada.bpaai.data.local.UserPreference
import com.nada.bpaai.data.remote.response.ListStoryItem
import com.nada.bpaai.databinding.ActivityMapsBinding
import com.nada.bpaai.ui.MainViewModelFactory
import com.nada.bpaai.ui.ViewModelFactory
import com.nada.bpaai.ui.home.MainViewModel
import com.nada.bpaai.ui.home.UserViewModel
import com.nada.bpaai.ui.home.dataStore

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private val boundBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap
    private lateinit var userViewModel: UserViewModel
    private val mapsViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[UserViewModel::class.java]

        getData()

        mapsViewModel.message.observe(this) {
            if (it != "Stories fetched successfully") Toast.makeText(this, it, Toast.LENGTH_SHORT)
                .show()
        }

        mapsViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        setMapStyle()
    }

    private fun getData() {
        userViewModel.getDataUser().observe(this) {
            val token = it.token
            mapsViewModel.getStories(token)
        }
        mapsViewModel.listStory.observe(this) {
            if (it != null) {
                setMarker(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setMarker(data: List<ListStoryItem>) {
        lateinit var locationZoom: LatLng
        data.forEach {
            if (it.lat != null && it.lon != null) {
                val latLng = LatLng(it.lat, it.lon)
                val address = LocationConverter.getStringAddress(latLng, this)
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.name)
                        .snippet(address)
                )
                boundBuilder.include(latLng)
                marker?.tag = it

                locationZoom = latLng
            }
        }

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                locationZoom, 3f
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this,
                        R.raw.maps_standard
                    )
                )
            if (!success) {
                Log.e(ContentValues.TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(ContentValues.TAG, "Can't find style. Error: ", exception)
        }
    }
}