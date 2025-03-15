package com.expapps.cmparentapp.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.expapps.cmparentapp.FirebaseSource
import com.expapps.cmparentapp.R
import com.expapps.cmparentapp.databinding.ActivityLocationBinding
import com.expapps.cmparentapp.models.Locations
import com.expapps.cmparentapp.sharedprefs.KSharedPreference
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

class LocationActivity : AppCompatActivity(),
    GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    private lateinit var firebaseSource: FirebaseSource
    private lateinit var binding: ActivityLocationBinding
    private lateinit var locations: LatLng
    private var markerPerth: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseSource = FirebaseSource()
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        // AIzaSyCiNwJc0E8-o9hi4ZlvKzFno9b63WTRUHc

    }

    fun getLocation(map: GoogleMap) {
        firebaseSource.getLocation(KSharedPreference.getCurrentUserId(this)).observe(this) {
            it?.let {
                locations = LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0)

                markerPerth = map.addMarker(
                    MarkerOptions()
                        .position(locations)
                        .title("Child Location")
                )
                markerPerth?.tag = 0
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(locations, 20f))
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        getLocation(map)

    }



    override fun onMarkerClick(p0: Marker): Boolean {
        return false
    }
}