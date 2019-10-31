package com.example.pruebaolimpia.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.pruebaolimpia.R
import com.google.android.material.textfield.TextInputLayout

fun View.setWarningsRequestL(): Boolean {
    return if (this is ViewGroup) {
        this.evaluateEditText()
    } else false
}

fun ViewGroup.evaluateEditText(): Boolean {
    val result = false
    this.invalidate()
    for (i in this.children)
        if (i.visibility == View.VISIBLE && i is TextInputLayout)
            if (i.editText?.setRequestWarning(context)!!) return true
    return result
}

fun EditText.setRequestWarning(context: Context): Boolean {
    return this.setWarning(context.getString(R.string.requiredField), context)
}

fun EditText.setWarning(message: String, context: Context): Boolean {
    return if (this.text?.isNotEmpty()!!) false
    else {
        this.requestFocus()
        val icWarnings = ContextCompat.getDrawable(context, R.drawable.ic_warning)
        icWarnings!!.setBounds(0, 0, icWarnings.intrinsicWidth, icWarnings.intrinsicHeight)
        this.setError(message, icWarnings)
        true
    }
}