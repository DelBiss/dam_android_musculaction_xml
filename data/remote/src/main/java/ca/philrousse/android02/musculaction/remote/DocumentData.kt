package ca.philrousse.android02.musculaction.remote

import android.util.Log
import ca.philrousse.android02.musculaction.data.entity.*
import ca.philrousse.android02.musculaction.data.entity.views.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.flow.*

private const val TAG = "DocumentData"

class PathIdDirectory{
    class PathIdType{
        private val reference = mutableMapOf<String,Long>()
        private var current:Long = 1L
        fun getId(path:String):Long{
            return reference[path] ?: kotlin.run {
                current.also {
                    reference[path] = it
                    current += 1
                }
            }
        }

        fun getPath(id: Long):String?{
            return reference.filter {
                it.value == id
            }.keys.firstOrNull()
        }

        override fun toString(): String {
            return reference.toString()
        }
    }

    val directory = mutableMapOf<String,PathIdType>()

    fun getPath(type: String, id: Long):String?{
        return getForType(type).getPath(id)
    }

    fun getId(type: String, path: String):Long{
        return getForType(type).getId(path)
    }

    private fun getForType(type:String):PathIdType{
        return directory[type] ?: kotlin.run {
            PathIdType().also {
                directory[type] = it
            }
        }
    }

    companion object {
        @Volatile
        private var instance: PathIdDirectory? = null

        fun getInstance(): PathIdDirectory {
            return instance ?: synchronized(this) {
                instance ?: PathIdDirectory().also {
                    instance = it
                }
            }
        }
    }
}


abstract class DocumentData (
    protected val snapshot: QueryDocumentSnapshot
) {
    abstract val type:String
    private val path:String
        get() = snapshot.reference.path
    open val id:Long get() = PathIdDirectory.getInstance().getId(type,path)
}

class ImageDocument(private val snapshot: QueryDocumentSnapshot){
    val id:String? get() = (snapshot.data["file"] as String?)?.split(".")?.dropLast(1)?.joinToString(separator = ".")
}


class CategoryDocument(snapshot: QueryDocumentSnapshot):DocumentData(snapshot) {
    override val type: String = "category"
    private val name: String get() = (snapshot.data["title"] ?: "NoName") as String
    private val description: String get() = (snapshot.data["description"] ?: "") as String
    private val isUserGenerated: Boolean get() = (snapshot.data["isUserGenerated"] ?: false) as Boolean

    private fun asCategory(): Flow<Category>{
        return snapshot.reference.collection("image").getAsFlow().map { qs->
            Category(
                name = name,
                description = description,
                imageID = qs?.firstOrNull()?.let {

                    ImageDocument(it).id
                },
                id = id,
                isUserGenerated
            )
        }

    }

    fun asCardCategory(): Flow<CardCategory> {
        return asCategory().map {
            CardCategory(
                category = it,
                image = it.imageID?.let { id -> Image(id,id,id)}
            )
        }
    }

    fun asCategoryExercisesCollections():Flow<CategoryExercisesCollections>{
        return snapshot.combineCollectionListAsFlow("subcategories"){
            SubcategoryDocument(it).asSubcategoryExercisesCardsCollection()
        }.filterNotNull().zip(asCategory()){child,cat->
            CategoryExercisesCollections(
                category = cat,
                image = cat.imageID?.let { id -> Image(id,id,id)},
                child = child
            )
        }

    }
}

//
class VideoDocument(snapshot: QueryDocumentSnapshot):DocumentData(snapshot){
    override val type: String = "ExerciseDetailVideo"
    private val videoUrl: String get() = (snapshot.data["value"] ?: "") as String

    fun asExerciseDetailVideo(): ExerciseDetailVideo{
        return ExerciseDetailVideo(
            videoUrl = videoUrl,
            id = id
        )
    }
}


class ExerciseDetailDocument(snapshot: QueryDocumentSnapshot):DocumentData(snapshot) {
    override val type: String = "ExerciseDetail"
    private val name: String get() = (snapshot.data["title"] ?: "NoName") as String
    private val description: String get() = (snapshot.data["content"] ?: "") as String
    private val isUserGenerated: Boolean get() = (snapshot.data["isUserGenerated"] ?: false) as Boolean

    private fun asExerciseDetail(): ExerciseDetail{
        return ExerciseDetail(
                name = name,
                description = description,
                id = id,
                isUserGenerated = isUserGenerated
            )


    }

    fun asCardExerciseDetail(): Flow<CardExerciseDetail> {
        return snapshot.transformCollectionListAsFlow("video") {
            VideoDocument(it).asExerciseDetailVideo()
        }.map {
            CardExerciseDetail(
                detail = asExerciseDetail(),
                videosList = it ?: listOf()
            )
        }
    }
}


class ExerciseDocument(snapshot: QueryDocumentSnapshot):DocumentData(snapshot) {
    override val type: String = "Exercise"
    private val name: String get() = (snapshot.data["title"] ?: "NoName") as String
    private val description: String get() = (snapshot.data["description"] ?: "") as String
    private val short_description: String get() = (snapshot.data["short_description"] ?: "") as String
    private val isUserGenerated: Boolean
        get() = (snapshot.data["isUserGenerated"] ?: false) as Boolean

    private fun asExercise(): Flow<Exercise> {
        return snapshot.reference.collection("image").getAsFlow().map { qs->
            Exercise(
                name = name,
                short_description = short_description,
                description = description,
                imageID = qs?.firstOrNull()?.let {

                    ImageDocument(it).id
                },
                id = id,
                isUserGenerated = isUserGenerated
            )
        }


    }

    fun asCardExercise(): Flow<CardExercise> {
        return asExercise().map {
            CardExercise(
                exercise = it,
                image = it.imageID?.let { id -> Image(id,id,id)}
            )
        }
    }

    fun asExerciseView(){
        val a = snapshot.combineCollectionListAsFlow("sections"){
            ExerciseDetailDocument(it).asCardExerciseDetail()
        }.combine(asExercise()){ detail, ex ->
            ExerciseView(
                exercise = ex,
                image = ex.imageID?.let { id -> Image(id,id,id)},
                child = detail ?: listOf()
            )
        }
    }
}

class SubcategoryDocument(snapshot: QueryDocumentSnapshot):DocumentData(snapshot) {
    override val type: String = "Subcategory"
    private val name: String get() = (snapshot.data["title"] ?: "NoName") as String
    private val isUserGenerated: Boolean
        get() = (snapshot.data["isUserGenerated"] ?: false) as Boolean

    private fun asSubcategory(): Subcategory {
        return Subcategory(
            name = name,
            id = id,
            isUserGenerated = isUserGenerated
        )
    }

    fun asSubcategoryExercisesCardsCollection(): Flow<SubcategoryExercisesCardsCollection> {
        return snapshot.combineCollectionListAsFlow("exercises"){
            ExerciseDocument(it).asCardExercise()
        }.filterNotNull().map {
            SubcategoryExercisesCardsCollection(
                subcategory = asSubcategory(),
                child = it
            )
        }
    }
}

