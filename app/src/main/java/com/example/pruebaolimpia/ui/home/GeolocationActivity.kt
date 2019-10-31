package com.example.pruebaolimpia.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pruebaolimpia.R
import com.example.pruebaolimpia.data.AppDatabase
import com.example.pruebaolimpia.util.broadcast.ConnectivityReceiver
import com.example.pruebaolimpia.util.broadcast.GpsReceiver
import com.example.pruebaolimpia.util.Coroutines
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_geolocation.*

class GeolocationActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, ConnectivityReceiver.ConnectivityReceiverListener,
    GpsReceiver.GpsReceiverListener, LocationListener, View.OnClickListener {

    private val MY_PERMISSIONS_REQUEST_LOCATION = 99

    private var latLngOrigin: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geolocation)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkConnection()
        checkGps()
    }

    private var alertDialogBuilder: AlertDialog.Builder? = null

    private var alertDialog: AlertDialog? = null

    private var mGoogleMap: GoogleMap? = null

    private val MY_PERMISSION_FINE_LOCATION = 101

    private var mGoogleApiClient: GoogleApiClient? = null

    private var mLocationRequest: LocationRequest? = null

    // Check Connection to network
    private fun checkConnection() {
        val isConnected = ConnectivityReceiver().isWifiConnected()
        if (!isConnected) {
            alertDialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            alertDialogBuilder!!.setTitle(this.getString(R.string.errorInternet))
            alertDialogBuilder!!.setMessage(this.getString(R.string.noInternet))
            alertDialogBuilder!!.setCancelable(false)
            alertDialog = alertDialogBuilder!!.create()
            alertDialog!!.show()
        } else {
            //  alertDialog.dismiss();
        }
    }

    // Void verify GPS active in device
    private fun checkGps() {
        val isEnabled = GpsReceiver().isEnabled()
        if (!isEnabled) {
            alertDialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            alertDialogBuilder!!.setTitle(this.getString(R.string.error_gps))
            alertDialogBuilder!!.setMessage(this.getString(R.string.no_gps))
            alertDialogBuilder!!.setCancelable(false)
            alertDialogBuilder!!.setPositiveButton(this.getString(R.string.habilitar)) { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            alertDialog = alertDialogBuilder!!.create()
            alertDialog!!.show()
        } else {
            //   alertDialog.dismiss();
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            false
        } else true
    }

    override fun onClick(v: View?) {
        when (v) {
            btNextG -> {
                Coroutines.main {
                    val userDao = AppDatabase.invoke(this).getUserDao()
                    val user = userDao.getUser()
                    user.latitude = latLngOrigin?.latitude
                    user.longitude = latLngOrigin?.longitude
                    userDao.upsert(user)
                    startActivity(Intent(this, BluetoothWifiActivity::class.java))
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap!!.isMyLocationEnabled = true
            buildGoogleApiClient()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_FINE_LOCATION
            )
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0: Int) {}

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            Toast.makeText(this, "no internet", Toast.LENGTH_LONG).show()
            alertDialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            alertDialogBuilder!!.setTitle(getString(R.string.errorInternet))
            alertDialogBuilder!!.setMessage(getString(R.string.noInternet))
            alertDialogBuilder!!.setCancelable(false)
            alertDialog = alertDialogBuilder!!.create()
            alertDialog!!.show()
        } else alertDialog!!.dismiss()
    }

    override fun onGpsConnectionChanged(isEnabled: Boolean) {
        if (!isEnabled) {
            Toast.makeText(this, "no gps", Toast.LENGTH_LONG).show()
            alertDialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            alertDialogBuilder!!.setTitle(getString(R.string.error_gps))
            alertDialogBuilder!!.setMessage(getString(R.string.no_gps))
            alertDialogBuilder!!.setCancelable(false)
            alertDialogBuilder!!.setPositiveButton(getString(R.string.habilitar)) { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            alertDialog = alertDialogBuilder!!.create()
            alertDialog!!.show()
        } else alertDialog!!.dismiss()
    }

    override fun onLocationChanged(location: Location?) {
        latLngOrigin = LatLng(location!!.latitude, location.longitude)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLngOrigin))
        mGoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))
        if (mGoogleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (mGoogleApiClient == null)
                            buildGoogleApiClient()
                        mGoogleMap!!.isMyLocationEnabled = true
                    }
                } else checkLocationPermission()
                return
            }
        }
    }
}
