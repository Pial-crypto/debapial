package com.hassanpial.our_1st_project_of_book_exchange

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private lateinit var mMap: GoogleMap
    var locationfound=false
 //   private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       // val mapFragment = supportFragmentManager
         //   .findFragmentById(R.id.map) as SupportMapFragment
      //  mapFragment.getMapAsync(this)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        var desiredcity=intent.getStringExtra("desiredcity").toString()
getCurrentLocationUser(desiredcity)
        // Initialize search EditText
        //searchEditText = findViewById(R.id.editTextSearchLocation)
//
        // Set up search functionality
        //val searchButton = findViewById<Button>(R.id.buttonSearchLocation)
        //searchButton.setOnClickListener {
            //val query = searchEditText.text.toString()
            // Perform search on the map using the query

       // }

        findViewById<Button>(R.id.btnSetLocation).setOnClickListener(){
            val intent = Intent(this, Editing_Profile::class.java)
            intent.putExtra("CITY_NAME", desiredcity)
            if (locationfound) {
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Location not found",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Customize the map as needed
        mMap.uiSettings.isZoomControlsEnabled = true

        // Optionally, add default marker to a location
     //   val defaultLocation = LatLng(37.7749, -122.4194)
       // mMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker in San Francisco"))
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
        // Trigger search for desired city after currentLocation is initialized
        if(::currentLocation.isInitialized) {
            performSearch(intent.getStringExtra("desiredcity").toString())
        }
    }

    private fun performSearch(query: String) {
        // Use the query to search for locations on the map
        // For demonstration purposes, let's center the map on the queried location
        val queriedLocation = getLocationFromQuery(query)
        if (queriedLocation != null) {
            // Move camera to the queried location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(queriedLocation, 16f)) // Adjust zoom level as needed
            // Add a marker at the queried location (optional)
            mMap.addMarker(MarkerOptions().position(queriedLocation).title("Marker at $query"))
        }
        else{
            Toast.makeText(this,"Location not found",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocationFromQuery(query: String): LatLng? {
        val geocoder = Geocoder(this)
        try {
            val addressList = geocoder.getFromLocationName(query, 10) // Increase result limit
            if (addressList != null && addressList.isNotEmpty()) {
                // Choose the best match based on relevance
                locationfound=true
                val bestMatch = chooseBestMatch(query, addressList)
                return LatLng(bestMatch.latitude, bestMatch.longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle IO exception
            Toast.makeText(this, "Error: Unable to retrieve location", Toast.LENGTH_SHORT).show()
        }
        // Location not found
        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()

        return null
    }


    private fun chooseBestMatch(query: String, addressList: List<Address>): Address {
        // Calculate relevance score for each address
        val relevanceScores = addressList.map { address ->
            calculateRelevanceScore(query, address,currentLocation)

        }
        // Choose the address with the highest relevance score
        val bestIndex = relevanceScores.indexOf(relevanceScores.maxOrNull())
        return addressList[bestIndex]
    }

    // Sample function to calculate distance between two locations (in meters)
    fun calculateDistance(currentLocation: Location, addressLocation: Location): Float {
        return currentLocation.distanceTo(addressLocation)
    }

    private fun calculateRelevanceScore(query: String, address: Address, currentLocation: Location): Int {
        var relevanceScore = 0

        // Calculate distance between current location and address location
        val addressLocation = Location(null).apply {
            latitude = address.latitude
            longitude = address.longitude
        }
        val distance = calculateDistance(currentLocation, addressLocation)

        // Adjust relevance score based on distance
        // For example, you might decrease relevance score as distance increases
        relevanceScore -= (distance / 1000).toInt() // Adjust as needed

        // Check if the adminArea of the address contains the query string
        if (address.adminArea?.contains(query, ignoreCase = true) == true) {
            relevanceScore += 3
        }

        // Check if the locality of the address contains the query string
        if (address.locality?.contains(query, ignoreCase = true) == true) {
            relevanceScore += 2
        }

        // Check if the thoroughfare of the address contains the query string
        if (address.thoroughfare?.contains(query, ignoreCase = true) == true) {
            relevanceScore += 1
        }

        return relevanceScore
    }









    private fun getCurrentLocationUser(query: String) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                var cityName = addresses?.get(0)?.locality.toString()
                Toast.makeText(applicationContext, "Current city: $cityName", Toast.LENGTH_SHORT).show()
                // Initialize the map once the current location is obtained
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
// Now you have the user's current city in the cityName variable
                // You can use it as needed, for example, starting a new activity

                // Now you have the user's current city in the cityName variable
                // You can use it as needed
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

    }







}
