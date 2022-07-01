package ca.philrousse.android02.musculaction.data.entity

import android.content.Context
import android.graphics.drawable.Drawable

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil

class ListComparator<T:IListElement>:DiffUtil.ItemCallback<T>(){
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

}

interface IListElement{
    var name:String
    val id:Long?
    override fun equals(other: Any?): Boolean

    companion object{
        fun equals(me:IListElement, other: Any?): Boolean{
            if (me === other) return true
            if (other !is IListElement) return false
            if(
                me.id == other.id &&
                me.name == other.name
            ) return true
            return false
        }
    }
}

interface IChildItem{
    var parentId:Long?
    val id:Long?
    override fun equals(other: Any?): Boolean

    companion object{
        fun equals(me:IChildItem, other: Any?): Boolean{
            if (me === other) return true
            if (other !is IChildItem) return false
            if(
                me.id == other.id &&
                me.parentId == other.parentId
            ) return true
            return false
        }
    }
}

interface IImageCard : IListElement {
    var imageID: String
    fun getDrawable(context: Context): Drawable? = Companion.getDrawable(context, imageID)

    companion object{
        private fun getDrawableResource(context: Context,uri:String):Int?{
            val imageResource: Int = context.resources.getIdentifier(uri, null, context.packageName)
            if(imageResource == 0){
                return null
            }
            return imageResource
        }

        private fun getDrawable(context: Context, imageID: String): Drawable?{
            val uri = "@drawable/_${imageID.replace('-', '_').split(".")[0]}"
            val imageResource = getDrawableResource(context, uri)?:run {
                getDrawableResource(context, "@drawable/ic_broken_image")
            }
            return imageResource?.let {
                ResourcesCompat.getDrawable(context.resources,imageResource,null)
            }
        }

        fun equals(me:IImageCard, other: Any?): Boolean{
            if (me === other) return true
            if (other !is IImageCard) return false
            if(
                me.imageID == other.imageID &&
                IListElement.equals(me,other)
            ) return true
            return false
        }

    }
}

interface IDescriptiveCard : IListElement {
    var description: String
    companion object{
        fun equals(me:IDescriptiveCard, other: Any?): Boolean{
            if (me === other) return true
            if (other !is IDescriptiveCard) return false
            if(
                me.description == other.description &&
                IListElement.equals(me,other)
            ) return true
            return false
        }
    }
}

interface IDescriptiveImageCard:IImageCard,IDescriptiveCard{

    companion object{
        fun equals(me:IDescriptiveImageCard, other: Any?): Boolean{
            if (me === other) return true
            if (other !is IDescriptiveImageCard) return false
            if(
                IImageCard.equals(me,other) &&
                IDescriptiveCard.equals(me,other)
            ) return true
            return false
        }
    }
}
