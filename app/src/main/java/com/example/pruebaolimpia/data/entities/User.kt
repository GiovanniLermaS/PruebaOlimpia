package com.example.pruebaolimpia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var name: String? = null,
    var identification: String? = null,
    var address: String? = null,
    var city: String? = null,
    var country: String? = null,
    var phone: String? = null,
    var image: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var isWifi: Int? = null,
    var isBluetooth: Int? = null
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}