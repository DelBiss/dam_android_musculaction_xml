package ca.philrousse.android02.musculaction.remote

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import ca.philrousse.android02.musculaction.data.entity.*
import ca.philrousse.android02.musculaction.data.entity.views.*
import com.google.firebase.firestore.*

private const val TAG = "DocumentData"



abstract class DocumentData (
    val snapshot: DocumentSnapshot
):IDataImage {
    val id:String
        get() = snapshot.reference.path
    val image: IDataImage get() =  this
    open val imageID: String? get() = (snapshot.getString("imageID") ?: "ic_broken_image")
    open var name:String = snapshot.getString("name")?:"Titre du document"
    open var description:String = snapshot.getString("description")?:""
    open var short_description:String? = snapshot.getString("short_description")
    fun delete(){
        snapshot.reference.delete()
    }

    override fun getDrawable(context: Context, default: Drawable?): Drawable? {
        return context.resources.getIdentifier(
            "@drawable/$imageID",
            null,
            context.packageName
        )?.let { resourceId: Int ->
            ResourcesCompat.getDrawable(context.resources,resourceId,null)
        }
    }



    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DocumentData) return false

        if (id != other.id) return false
        if (imageID != other.imageID) return false
        if (name != other.name) return false
        if (description != other.description) return false

        return true
    }
}

class CategoryItemDocument(snapshot: DocumentSnapshot):DocumentData(snapshot), ICard{
    override var video: String? = null
    override var short_description: String? = ""

    override var description: String
        get() = ""
        set(value) {}



    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CategoryItemDocument) return false
        if (!super.equals(other)) return false

        return true
    }
}

class CategoryDetailDocument(snapshot: DocumentSnapshot, exercises:List<ExerciseItemDocument>):DocumentData(snapshot),IViewCardsCollections{
    override var child: List<ICardsCollection> = exercises.groupBy {
        it.subcategory
    }.map { (subcategoryName, child) -> ExerciseGrouping(subcategoryName,child) }
    override var short_description: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CategoryDetailDocument) return false
        if (!super.equals(other)) return false

        if (child != other.child) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + child.hashCode()
        return result
    }
}

data class ExerciseGrouping(override var name: String, override var child: List<ICard>) :ICardsCollection{
    override val id: String?
        get() = ""


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExerciseGrouping) return false

        if (name != other.name) return false
        if (child != other.child) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + child.hashCode()
        return result
    }
}

class ExerciseItemDocument(snapshot: DocumentSnapshot):DocumentData(snapshot),ICard{
    var subcategory:String = snapshot.getString("subcategory")?:"Autre exercises"
    override var video: String? = null
    override var short_description: String? = snapshot.getString("short_description")?:"Autre exercises"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExerciseItemDocument) return false
        if (!super.equals(other)) return false

        if (subcategory != other.subcategory) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + subcategory.hashCode()
        return result
    }
}

class ExerciseDetailDocument(snapshot: DocumentSnapshot, override var child: List<ICard>):DocumentData(snapshot),IViewCards
class ExerciseDocumentDetail(snapshot: DocumentSnapshot) :DocumentData(snapshot), ICard {

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + order.hashCode()
        result = 31 * result + (video?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExerciseDocumentDetail) return false
        if (!super.equals(other)) return false

        if (video != other.video) return false

        return true
    }

    var order:Long = snapshot.getLong("order") ?: -1
    override var video: String? = if (snapshot.getString("video").isNullOrBlank()) null else snapshot.getString("video")
    override var short_description: String? = null

}

data class NewExercises(
    @Exclude var parentId:String,
    override var name: String = "",
    override var description:String = "",
    override var short_description: String? = "",
    var subcategory:String = "Exercise Personnel",
    var imageID: String = "ic_baseline_edit_24"
):IViewCards {
    @Exclude override val id: String?=null
    @Exclude override val image: IDataImage? = null
    @Exclude override var child: List<ICard> = emptyList()
}

data class NewDetail(
    override var name: String = "",
    override var description: String = "",
    override var video: String? = "",
    var order: Long = 99L
):ICard {

    @Exclude override val id: String?=null
    @Exclude override var short_description: String?=null
    @Exclude override val image: IDataImage?=null
}
