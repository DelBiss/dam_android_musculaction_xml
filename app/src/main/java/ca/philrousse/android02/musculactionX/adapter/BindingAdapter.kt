package ca.philrousse.android02.musculactionX.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("textInputLayout", "defaultText")
fun mirrorInputLayout(view: TextView, textInputLayout: TextInputLayout, defaultText: String) {
    val tilValue = textInputLayout.editText?.text?.toString()
    if(tilValue.isNullOrBlank()){
        view.text = defaultText
    } else {
        view.text = tilValue
    }
}

