package ca.philrousse.android02.musculaction.data.local.json

import android.util.Log
import ca.philrousse.android02.musculaction.data.entity.*
import ca.philrousse.android02.musculaction.data.local.database.MusculactionDAO

class MAJson(private val data:List<MAJsonCategory>){

    private fun collectImageEntity():List<Image>{
        val images = mutableListOf<Image>()
        data.forEach {
            images.addAll(it.collectImageEntity())
        }
        return images
    }

    private fun collectUniqueImageEntity():Set<Image>{
        val images = collectImageEntity()
        val imagesMap = mutableMapOf<String,Image>()
        images.forEach {
           imagesMap.getOrPut(it.id) {it}.update(it)
        }
        return imagesMap.values.toSet()
    }

    fun insert(dao: MusculactionDAO){
        collectUniqueImageEntity().forEach {
            dao.insert(it)
        }
        data.forEach {
            it.insert(dao)
        }
    }
}

data class MAJsonImage(
    val url:String,
    val file:String
){
    private val fileWithoutExt:String get() =
        file.split(".")
            .dropLast(1)
            .joinToString("-")

    val id:String get() =
        fileWithoutExt.split("-")
            .drop(1)
            .joinToString(separator = "_")

    private val smallResource:String? get() {
        val rIdSplit = fileWithoutExt.split("-")
        if (rIdSplit.first() == "140"){
            return "_" + rIdSplit.joinToString(separator = "_")
        }
        return null
    }
    private val resource:String? get() {
        val rIdSplit = fileWithoutExt.split("-")
        if (rIdSplit.first() == "intro"){
            return "_" + rIdSplit.joinToString(separator = "_")
        }
        return null
    }
    val entity:Image get() {
        Log.d("MAJsonImage","$this: $id, $smallResource, $resource")
        return Image(id, smallResource, resource)
    }
}

data class MAJsonCategory(
    val title:String,
    val lien:String,
    val description:String,
    val image: List<MAJsonImage>,
    val subcategories:List<MAJsonSubcategory>
) {
    private val entity: Category get() {
        Log.d("MAJsonCategory","$title: $image")
        return Category(
            name = title,
            description = description,
            imageID = image.firstOrNull()?.id
        )
    }

    fun collectImageEntity():List<Image>{
        val returnList = mutableListOf<Image>()
        image.firstOrNull()?.also {
            returnList.add(it.entity)
        }
        subcategories.forEach {
            returnList.addAll(it.collectImageEntity())
        }
        return returnList
    }

    fun insert(dao: MusculactionDAO){
        val id = dao.insert(entity)
        subcategories.forEach {
            it.insert(dao, id)
        }
    }
}
data class MAJsonSubcategory(
    val title: String,
    val exercises: List<MAJsonExercise>,
    var parentId: Long? = null
){
    private val entity: Subcategory get() {
        return Subcategory(
            name = title,
            parentId = parentId
        )
    }

    fun collectImageEntity():List<Image>{
        val returnList = mutableListOf<Image>()
        exercises.forEach {
            returnList.addAll(it.collectImageEntity())
        }
        return returnList
    }

    fun insert(dao: MusculactionDAO, parentId:Long){
        this.parentId = parentId
        val id = dao.insert(entity)
        exercises.forEach {
            it.insert(dao, id)
        }
    }
}
data class MAJsonExercise(
    val title: String,
    val lien: String,
    val category:String,
    val short_description:String,
    val image_short:List<MAJsonImage>,
    val image: List<MAJsonImage>,
    val description: String,
    val sections: List<MAJsonSection>,
    var parentId: Long? = null
){
    private val entity: Exercise get() {
        return Exercise(
            name = title,
            description = description,
            imageID = image.firstOrNull()?.id,
            parentId = parentId
        )
    }

    fun insert(dao: MusculactionDAO, parentId:Long){
        this.parentId = parentId
        val id = dao.insert(entity)
        sections.forEach {
            it.insert(dao, id)
        }
    }

    fun collectImageEntity():List<Image>{
        val returnList = mutableListOf<Image>()

        image.firstOrNull()?.also {
            returnList.add(it.entity)
        }
        image_short.firstOrNull()?.also {
            returnList.add(it.entity)
        }

        return returnList
    }
}

data class MAJsonSection(
    val title: String,
    val content:String,
    val video:List<String>?,
    var parentId: Long? = null
){
    private val entity: ExerciseDetail get() {
        return ExerciseDetail(
            name = title,
            description = content,
            parentId = parentId
        )
    }

    fun insert(dao: MusculactionDAO, parentId:Long){
        this.parentId = parentId
        val id = dao.insert(entity)
        video?.forEach {
            dao.insert(ExerciseDetailVideo(
                    videoUrl = it,
                    parentId = id
                )
            )
        }
    }
}