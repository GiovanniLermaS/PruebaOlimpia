package com.example.pruebaolimpia.ui.home

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebaolimpia.R
import com.example.pruebaolimpia.data.AppDatabase
import com.example.pruebaolimpia.util.Coroutines
import kotlinx.android.synthetic.main.activity_save.*

class SaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        Coroutines.main {
            val userDao = AppDatabase.invoke(this).getUserDao()
            val user = userDao.getUser()
            val uriImage = Uri.parse(user.image)
            tvTextName.text = user.name
            tvTextId.text = user.identification
            tvTextAddress.text = user.address
            tvTextCity.text = user.city
            tvTextCountry.text = user.country
            tvTextCountry.text = user.country
            tvTextPhone.text = user.phone
            cIVImageProfile.setImageURI(uriImage)
            tvTextLatitude.text = user.latitude.toString()
            tvTextLongitude.text = user.longitude.toString()
            tvTextIsWifi.text = when (user.isWifi) {
                0 -> getString(R.string.no)
                else -> getString(R.string.yes)
            }
            tvTextIsBluetooth.text = when (user.isBluetooth) {
                0 -> getString(R.string.no)
                else -> getString(R.string.yes)
            }

        }
    }
}
