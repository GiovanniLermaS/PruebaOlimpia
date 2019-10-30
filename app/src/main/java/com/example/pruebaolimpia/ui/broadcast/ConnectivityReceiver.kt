package com.example.pruebaolimpia.ui.broadcast

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.pruebaolimpia.ui.global.GlobalApp

class ConnectivityReceiver : BroadcastReceiver() {

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        var bluetoothReceiverListener: BluetoothReceiverListener? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        //Wifi connection
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (connectivityReceiverListener != null)
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        //Bluetooth connection
        if (bluetoothReceiverListener != null)
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_TURNING_ON ->
                        bluetoothReceiverListener!!.onBluetoothConnectionChanged(true)

                    BluetoothAdapter.STATE_TURNING_OFF ->
                        bluetoothReceiverListener!!.onBluetoothConnectionChanged(false)
                }
            }
    }

    fun isWifiConnected(): Boolean {
        val cm =
            GlobalApp().getInstance()!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun isBluetoothConnect(): Boolean =
        BluetoothAdapter.getDefaultAdapter().isEnabled

    interface BluetoothReceiverListener {
        fun onBluetoothConnectionChanged(isConnected: Boolean)
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}
