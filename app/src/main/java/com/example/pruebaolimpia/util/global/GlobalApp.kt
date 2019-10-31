package com.example.pruebaolimpia.util.global

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.pruebaolimpia.util.broadcast.ConnectivityReceiver
import com.example.pruebaolimpia.util.broadcast.GpsReceiver

class GlobalApp : Application() {

    companion object {
        var mInstance: GlobalApp? = null
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    @Synchronized
    fun getInstance(): GlobalApp? {
        return mInstance
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    fun setBluettothListener(listener: ConnectivityReceiver.BluetoothReceiverListener) {
        ConnectivityReceiver.bluetoothReceiverListener = listener
    }

    fun setGpsListener(listener: GpsReceiver.GpsReceiverListener) {
        GpsReceiver.gpsReceiverListener = listener
    }
}