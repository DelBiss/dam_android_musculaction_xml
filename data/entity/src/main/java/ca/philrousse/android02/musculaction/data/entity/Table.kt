package ca.philrousse.android02.musculaction.data.entity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey
    val id:String,
    var smallResource:String?=null,
    var resource:String?=null
){
    fun update(other:Image){
        if(other.id == id){
            other.smallResource?.also {
                smallResource = it
            }

            other.resource?.also {
                resource = it
            }
        }
    }

    private fun getResourceId(context: Context, uri:String?):Int?{
        uri?.let {
            val imageResourceId = context.resources.getIdentifier("@drawable/$uri",null,context.packageName)
            if(imageResourceId == 0){
                return null
            }
            return imageResourceId
        }
        return null
    }

    fun getResourceId(context: Context):Int?{
        return getResourceId(context, resource ?: smallResource) ?: getResourceId(context, brokenImageResourceString)
    }

    fun getSmallestResourceId(context: Context):Int?{
        return getResourceId(context,  smallResource ?: resource) ?: getResourceId(context, brokenImageResourceString)
    }

    private fun getDrawable(context: Context, resourceId: Int?):Drawable?{
        return resourceId?.let {
            ResourcesCompat.getDrawable(context.resources,it,null)
        }
    }
    fun getDrawable(context: Context):Drawable?{
        return getDrawable(context, getResourceId(context))
    }
    fun getSmallestDrawable(context:Context):Drawable?{
        return getDrawable(context, getSmallestResourceId(context))
    }
    companion object {
        private const val brokenImageResourceString = "ic_broken_image"
    }
}

@Entity
data class Category(
    var name:String="N/D",
    var description:String="N/D",
    var imageID:String?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "Category[#$id](name=$name, imageId=$imageID)"
    }
}

@Entity
data class Subcategory(
    var name:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "Subcategory[#$id](name=$name, parentId=${parentId})"
    }
}

@Entity
data class Exercise(
    var name:String="N/D",
    var description:String="N/D",
    var imageID:String?=null,
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "Exercise[#$id](name=$name, imageId=$imageID, parentId=${parentId})"
    }
}

@Entity
data class ExerciseDetail(
    var name:String="N/D",
    var description:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "ExerciseDetail[#$id](name=$name, parentId=${parentId})"
    }

}

@Entity
data class ExerciseDetailVideo (
    var videoUrl:String="N/D",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null
){
    override fun toString(): String {
        return "ExerciseDetailVideo[#$id](videoUrl=$videoUrl, parentId=${parentId})"
    }
}