package com.example.pruebaolimpia.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebaolimpia.R
import com.example.pruebaolimpia.data.AppDatabase
import com.example.pruebaolimpia.data.entities.User
import com.example.pruebaolimpia.util.Coroutines
import com.example.pruebaolimpia.util.setWarningsRequestL
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
    }

    override fun onClick(v: View?) {
        when (v) {
            btNextF -> {
                if (clForm.setWarningsRequestL())
                    return
                else {
                    val user = User()
                    user.name = etxName.text.toString()
                    user.identification = etxId.text.toString()
                    user.address = etxAddress.text.toString()
                    user.city = etxCity.text.toString()
                    user.country = etxCountry.text.toString()
                    user.phone = etxPhone.text.toString()
                    Coroutines.main { AppDatabase.invoke(this).getUserDao().upsert(user) }
                    startActivity(Intent(this, PhotoActivity::class.java))
                }
            }
        }
    }
}
