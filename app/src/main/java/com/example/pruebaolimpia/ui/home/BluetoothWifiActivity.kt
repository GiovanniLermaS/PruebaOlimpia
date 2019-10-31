package com.example.pruebaolimpia.ui.home

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebaolimpia.R
import com.example.pruebaolimpia.data.AppDatabase
import com.example.pruebaolimpia.util.broadcast.ConnectivityReceiver
import com.example.pruebaolimpia.util.global.GlobalApp
import com.example.pruebaolimpia.util.Coroutines
import kotlinx.android.synthetic.main.activity_bluetooth_wifi.*

class BluetoothWifiActivity : AppCompatActivity(),
    ConnectivityReceiver.ConnectivityReceiverListener,
    ConnectivityReceiver.BluetoothReceiverListener, View.OnClickListener {

    private val connectivityReceiver =
        ConnectivityReceiver()

    override fun onStart() {
        super.onStart()
        val connectivityReceiver = ConnectivityReceiver()
        val intentWifi = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(connectivityReceiver, intentWifi)

        val intentBluetooth = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(connectivityReceiver, intentBluetooth)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_wifi)

        tvIsWifi.text =
            if (connectivityReceiver.isWifiConnected()) getString(R.string.yes) else getString(R.string.no)
        tvIsBluetooth.text =
            if (connectivityReceiver.isBluetoothConnect()) getString(R.string.yes) else getString(R.string.no)
    }

    override fun onClick(v: View?) {
        when (v) {
            btNextB -> {
                Coroutines.main {
                    val userDao = AppDatabase.invoke(this).getUserDao()
                    val user = userDao.getUser()
                    user.isWifi = if (connectivityReceiver.isWifiConnected()) 1 else 0
                    user.isBluetooth = if (connectivityReceiver.isBluetoothConnect()) 1 else 0
                    userDao.upsert(user)
                    startActivity(Intent(this, SaveActivity::class.java))
                }
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        tvIsWifi.text = if (isConnected) getString(R.string.yes) else getString(R.string.no)
    }

    override fun onBluetoothConnectionChanged(isConnected: Boolean) {
        tvIsBluetooth.text = if (isConnected) getString(R.string.yes) else getString(R.string.no)
    }

    override fun onResume() {
        super.onResume()
        GlobalApp.mInstance?.setConnectivityListener(this)
        GlobalApp.mInstance?.setBluettothListener(this)
    }
}
