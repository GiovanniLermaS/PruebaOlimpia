package com.example.pruebaolimpia.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebaolimpia.R
import com.example.pruebaolimpia.data.AppDatabase
import com.example.pruebaolimpia.data.entities.User
import com.example.pruebaolimpia.util.Coroutines
import kotlinx.android.synthetic.main.activity_save.*

class SaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        Coroutines.main {
            val user = AppDatabase.invoke(this).getUserDao().getUser().value as User
            tvTextName.text = user.name
            tvTextId.text = user.identification
            tvTextAddress.text = user.address
            tvTextCity.text = user.city
            tvTextCountry.text = user.country
            //cIVImageProfile.setImageURI(user.image as Uri)
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
