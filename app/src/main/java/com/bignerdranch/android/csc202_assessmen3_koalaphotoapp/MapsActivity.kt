package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney  = LatLng(-34.0,151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        updateUI()
    }

    private fun updateUI() {

        val mypoint:LatLng = LatLng(-26.7174, 153.062151)
        val myMarker: MarkerOptions = MarkerOptions().position(mypoint).title("Receipt location")

        mMap.clear()
        mMap.addMarker(myMarker)

        val zoomLevel: Float = 15f
        val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(mypoint, zoomLevel)

        mMap.animateCamera(update)
    }
}