@file:Suppress("unused")

package ca.philrousse.android02.musculaction.data.entity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ca.philrousse.android02.musculaction.data.entity.views.IListElement

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

    fun getResourceUri():String?{
        return (resource ?: smallResource)?.let {
            "@drawable/$it"
        }
    }

    fun getSmallestResourceUri():String?{
        return (smallResource ?: resource)?.let {
            "@drawable/$it"
        }
    }
    private fun getResourceId(context: Context):Int?{
        return getResourceId(context, resource ?: smallResource) ?: getResourceId(context, brokenImageResourceString)
    }

    private fun getSmallestResourceId(context: Context):Int?{
        return getResourceId(context,  smallResource ?: resource) ?: getResourceId(context, brokenImageResourceString)
    }

    private fun getDrawable(context: Context, resourceId: Int?):Drawable?{
        return resourceId?.let {
            ResourcesCompat.getDrawable(context.resources,it,null)
        }
    }
    fun getDrawable(context: Context,default:Drawable?):Drawable?{
        return getDrawable(context, getResourceId(context)) ?: default
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
    override var name:String="",
    var description:String="",
    var imageID:String?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null,
    val isUserGenerated:Boolean = true
):IListElement{
    override fun toString(): String {
        return "Category[#$id](name=$name, imageId=$imageID)"
    }
}

@Entity
data class Subcategory(
    override var name:String="",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null,
    val isUserGenerated:Boolean = true
):IListElement{
    override fun toString(): String {
        return "Subcategory[#$id](name=$name, parentId=${parentId})"
    }
}



@Entity(foreignKeys = [ForeignKey(entity = Subcategory::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("parentId"),
    onDelete = ForeignKey.CASCADE)]
)
data class Exercise(
    override var name:String="",
    var short_description:String="",
    var description:String="",
    var imageID:String?=null,
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null,
    val isUserGenerated:Boolean = true
):IListElement{
    override fun toString(): String {
        return "Exercise[#$id](name=$name, imageId=$imageID, parentId=${parentId})"
    }
}

@Entity(foreignKeys = [ForeignKey(entity = Exercise::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("parentId"),
    onDelete = ForeignKey.CASCADE)])
data class ExerciseDetail(
    override var name:String="",
    var description:String="",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    override val id:Long?=null,
    val isUserGenerated:Boolean = true
):IListElement{
    override fun toString(): String {
        return "ExerciseDetail[#$id](name=$name, parentId=${parentId})"
    }
}

@Entity(foreignKeys = [ForeignKey(entity = ExerciseDetail::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("parentId"),
    onDelete = ForeignKey.CASCADE)])
data class ExerciseDetailVideo (
    var videoUrl:String="",
    var parentId:Long?=null,
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null,
    val isUserGenerated:Boolean = true
){
    override fun toString(): String {
        return "ExerciseDetailVideo[#$id](videoUrl=$videoUrl, parentId=${parentId})"
    }
}