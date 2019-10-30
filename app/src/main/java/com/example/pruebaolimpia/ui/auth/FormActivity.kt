package com.example.pruebaolimpia.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebaolimpia.R
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
    }

    override fun onClick(v: View?) {
        when (v) {
            btNextF ->
                startActivity(Intent(this, PhotoActivity::class.java))
        }
    }
}
