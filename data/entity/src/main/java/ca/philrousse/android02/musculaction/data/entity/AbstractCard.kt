package ca.philrousse.android02.musculaction.data.entity

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil

abstract class Card(
    open var name:String="N/D",
    open var imageID:String="N/D",
    open val id: Long? = null
){
    fun getDrawable(context: Context): Drawable?{

        val uri = "@drawable/_${imageID.replace('-', '_').split(".")[0]}"
        val imageResource: Int = context.resources.getIdentifier(uri, null, context.packageName)
        Log.d("Image","$uri --> ${context.packageName} ->> $imageResource")
        if (imageResource == 0){
            return null
        }
        return ResourcesCompat.getDrawable(context.resources,imageResource,null)
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other !is Card) return false
        if(
            this.id == other.id &&
            this.name == other.name &&
            this.imageID == other.imageID
        ) return true
        return false
    }

    override fun hashCode(): Int{
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "id=$id,name=$name,imageID=$imageID"
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem == newItem
        }
    }
}

abstract class DescriptiveCard(
    override var name:String="N/D",
    open var description:String="N/D",
    override var imageID:String="N/D",
    override val id: Long? = null
): Card(name, imageID, id) {


    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<DescriptiveCard>() {
            override fun areItemsTheSame(oldItem: DescriptiveCard, newItem: DescriptiveCard): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DescriptiveCard, newItem: DescriptiveCard): Boolean =
                oldItem == newItem
        }
    }
}
