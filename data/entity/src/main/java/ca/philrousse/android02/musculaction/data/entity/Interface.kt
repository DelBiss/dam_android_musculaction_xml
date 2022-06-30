package ca.philrousse.android02.musculaction.data.entity

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil

interface Card{
    val id: Long?
    var name:String
    var imageID:String

    fun getDrawable(context: Context): Drawable?{
        val uri = "@drawable/_${imageID.replace('-', '_').split(".")[0]}"
        val imageResource: Int = context.resources.getIdentifier(uri, null, context.packageName)
        Log.d("Image","$uri --> ${context.packageName} ->> $imageResource")
        if (imageResource == 0){
            return null
        }
        return ResourcesCompat.getDrawable(context.resources,imageResource,null)
    }

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem == newItem
        }
    }
}

interface DescriptiveCard: Card{
    var description:String

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<DescriptiveCard>() {
            override fun areItemsTheSame(oldItem: DescriptiveCard, newItem: DescriptiveCard): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DescriptiveCard, newItem: DescriptiveCard): Boolean =
                oldItem == newItem
        }
    }
}
