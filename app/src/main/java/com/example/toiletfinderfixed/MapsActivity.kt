package com.example.toiletfinderfixed

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rtchagas.pingplacepicker.ui.onclick
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONArray
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var currentLatLng: LatLng

    private lateinit var search: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val sharedPref = getSharedPreferences("LoginInformation", 0) ?: return
        val isAdminFromPref = sharedPref.getInt("isAdmin", 0)



        if(isAdminFromPref == 0) {
            fabForAdmin.visibility = View.GONE
        }

        fabForAdmin.setOnClickListener {
            Intent(this, ToiletRequestActivity::class.java).also {
                startActivity(it)
            }
        }

        fabMyAccount.setOnClickListener{
            val usernameFromSharedPref = sharedPref.getString("username", "")
         //   val genderFromSharedPref = sharedPref.getString("gender", "")
                   // Toast.makeText(this@MapsActivity, "$usernameFromSharedPref", Toast.LENGTH_SHORT).show()
                if(usernameFromSharedPref == "") {
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it)
                    }
                } else {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                    }

                }
//            val queue = Volley.newRequestQueue(this)
//            val url = "http://10.0.2.2/toilet_finder/read.php"
//
//            val stringRequest = StringRequest(Request.Method.GET, url,
//                Response.Listener<String>{response ->
//                    Toast.makeText(this@MapsActivity, "$response", Toast.LENGTH_SHORT).show()
//                    Log.d("REV", "$response")
//                }, Response.ErrorListener { error ->
//                    Toast.makeText(this@MapsActivity, "$error", Toast.LENGTH_LONG).show()
//                    Log.e("ERR", "$error")}
//                )
//            queue.add(stringRequest)
        }

        fabAddToilet.setOnClickListener{
            val usernameFromSharedPref = sharedPref.getString("username", "")
            if(usernameFromSharedPref == ""){
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }else {
                Intent(this, AddToiletActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
//                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }

        createLocationRequest()

    }



    private var mToilets: Marker? = null
    private val markerInfo = HashMap<Marker?, HashMap<String, String>>()
    private var allToilets : JSONArray = JSONArray()
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        cvMap.visibility = View.GONE

        val url = "http://192.168.100.14/toilet_finder/toilet/read.php"

        val jsonObject = JSONArray()
        val request = JsonArrayRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Log.d("LOCATION", "${response}")

                //Toast.makeText(this@MapsActivity, "${response[0]}", Toast.LENGTH_LONG).show()

                allToilets = response

                for(x in 0 until response.length()){
                    val customInfo = HashMap<String, String>()
                    var latitude = "${response.getJSONObject(x)["latitude"]}".toDouble()
                    var longitude = "${response.getJSONObject(x)["longitude"]}".toDouble()
                    Log.d("LATLNG", "$latitude")
                    Log.d("LATLNG", "$longitude")
                    var nama = response.getJSONObject(x)["nama"]
                    var toilets = LatLng(latitude, longitude)
                    var gender = response.getJSONObject(x)["gender"]
                    var idToilets = response.getJSONObject(x)["id_toilet"]
                    Log.d("LATLNG", "$toilets")

                    var options = MarkerOptions()
                        .position(toilets)
                        .title(nama as String?)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name))
                    mToilets = map.addMarker(options)

//                            .snippet("$gender")

                    customInfo.put("gender", "$gender")
                    customInfo.put("id_toilet", "$idToilets")
                    markerInfo.put(mToilets, customInfo)
                }

            }, Response.ErrorListener { error ->
                Log.d("LOCERROR", "$error")
                Toast.makeText(this@MapsActivity, "$error", Toast.LENGTH_SHORT).show()
            })

        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        VolleySingleton.getInstance(this).addToRequestQueue(request)

        // Add a marker in Sydney and move the camera
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)

        setUpMap()

        map.isMyLocationEnabled = true

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            }
        }

    }

    fun displayInfo(){

    }

    override fun onMarkerClick(p0: Marker) : Boolean{
        cvMap.visibility = View.VISIBLE
        Log.d("ISI", "$p0")
        tvCvNama.text = p0.getTitle()
        val sharedPref = getSharedPreferences("LoginInformation", 0)
        val idUserFromPref = sharedPref.getInt("id_user", 0)

        val mapInfo = markerInfo.get(p0)
        tvCvGender.text = mapInfo?.get("gender")
        var ToiletsId = mapInfo?.get("id_toilet")

        Log.d("MAPINFO", "$mapInfo")
//        val result = arrayOf<Float>().toFloatArray()
        var result = FloatArray(1)
        Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, p0.position.latitude, p0.position.longitude, result)
        Log.d("LATLNG", "${result[0]}")

        tvCvJarak.text = "${"%.2f".format(result[0])} meter"

//        map!!.animateCamera(CameraUpdateFactory.newLatLng(p0.position))

        btnCvDetails.setOnClickListener {
            Intent(this, DetailActivity::class.java).also {
                it.putExtra("ID", ToiletsId)
                it.putExtra("id_user", idUserFromPref)
                Log.d("MAPINFOID", "$ToiletsId")
                startActivity(it)
            }
            Log.d("IDTOIL", "$ToiletsId")
        }

        return false
    }

    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }


//    private fun placeMarkerOnMap(location: LatLng) {
//        // 1
//        val markerOptions = MarkerOptions().position(location)
//        // 2
//        val titleStr = getAddress(location)  // add these two lines
//        markerOptions.title(titleStr)
//        map.addMarker(markerOptions)
//    }

    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@MapsActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    // 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    // 2
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    fun onSearch(view: View){
        var searchQuery = findViewById<EditText>(R.id.etSearchHere)
        var textSearch = searchQuery.text.toString()

        Log.d("SEARCHQ", "$textSearch")
        Log.d("SEARCHQ", "$allToilets")
        var linearlayout = findViewById<LinearLayout>(R.id.llSearch)
        linearlayout.setBackgroundColor(resources.getColor(android.R.color.white))
        linearlayout.removeAllViews()
        for(i in 0 until allToilets.length()) {
            var namaToilet = allToilets.getJSONObject(i)["nama"].toString()
            var genderToilet = allToilets.getJSONObject(i)["gender"].toString()
            if (namaToilet.contains(textSearch, ignoreCase = true)){
                var searchResult = TextView(this)
                searchResult.text = namaToilet
                searchResult.onclick {
                    var lat = "${allToilets.getJSONObject(i)["latitude"]}" .toDouble()
                    var lon = "${allToilets.getJSONObject(i)["longitude"]}" .toDouble()
                    var toiletCoordinat = LatLng(lat, lon)
                    tvCvNama.text = namaToilet
                    tvCvGender.text = genderToilet
                    var resultDistance = FloatArray(1)
                    Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, lat, lon, resultDistance)
                    tvCvJarak.text = "${"%.2f".format(resultDistance[0])} meter"
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(toiletCoordinat, 17f))

                }
                searchResult.id = "${allToilets.getJSONObject(i)["id_toilet"]}" .toInt()
                linearlayout.addView(searchResult)

//                Log.d("SEARCH", "${allToilets.getJSONObject(i)["nama"]}")
            }

        }
    }

    fun onDeleteQ(view: View){
        var linearlayout = findViewById<LinearLayout>(R.id.llSearch)
        linearlayout.removeAllViews()
        var searchQuery = findViewById<EditText>(R.id.etSearchHere)
        var textSearch = searchQuery.text.toString()
        searchQuery.setText("")
    }


}
