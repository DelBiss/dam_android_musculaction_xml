package ca.philrousse.android02.musculactionX.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import ca.philrousse.android02.musculaction.data.entity.Image
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
@BindingMethods(value = [
    BindingMethod(
        type = ImageView::class,
        attribute = "android:tint",
        method = "setImageTintList")])

class MuscuImageAdapter{
    companion object{
        @JvmStatic
        @BindingAdapter("musculactionImage", "defaultImg","isSmall")
        fun imageGetter(view: ImageView, musculactionImage: Image, defaultImg:Drawable,isSmall: Boolean=false) {
            val rUri = if(isSmall){
                musculactionImage.getSmallestResourceUri()
            } else {
                musculactionImage.getResourceUri()
            }
            val myId = view.resources.getIdentifier(rUri,null,view.context.packageName)

            if(myId != 0){
                view.setImageResource(myId)
            } else{
                view.setImageDrawable(defaultImg)
            }
        }
    }
}

