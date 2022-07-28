package ca.philrousse.android02.musculaction.data.local.json

import android.util.Log
import ca.philrousse.android02.musculaction.data.entity.*
import ca.philrousse.android02.musculaction.data.local.database.MusculactionLocalDAO

class MAJson(private val data:List<MAJsonCategory>){

    private fun collectImageEntity():List<Image>{
        val images = mutableListOf<Image>()
        data.forEach {
            images.addAll(it.collectImageEntity())
        }
        return images
    }

    private fun collectUniqueImageEntity():Set<Image>{
        val images = mutableListOf(MAJsonImage(file ="ic_baseline_add_24", url = "").entity)
        images.addAll(collectImageEntity())
        val imagesMap = mutableMapOf<String,Image>()
        images.forEach {
           imagesMap.getOrPut(it.id) {it}.update(it)
        }
        return imagesMap.values.toSet()
    }

    fun insert(dao: MusculactionLocalDAO){
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
            .joinToString("_")

    val id:String get() {
        val rIdSplit = fileWithoutExt.split("_")
        if (rIdSplit.last() == "small") {
            return rIdSplit.dropLast(1)
                .joinToString(separator = "_")
        }
        return rIdSplit.joinToString(separator = "_")
    }
    private val smallResource:String? get() {
        val rIdSplit = fileWithoutExt.split("_")
        if (rIdSplit.last() == "small"){
            return fileWithoutExt
        }
        return null
    }
    private val resource:String? get() {
        val rIdSplit = fileWithoutExt.split("_")
        if (rIdSplit.last() != "small"){
            return fileWithoutExt
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
            imageID = image.firstOrNull()?.id,
            isUserGenerated = false
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

    fun insert(dao: MusculactionLocalDAO){
        val id = dao.insert(entity)
        //Add an empty category for personal exercise
        val subCatPersonnel = Subcategory(
            name = "Exercises Personnel",
            parentId = id,
            isUserGenerated = true
        )
        dao.insert(subCatPersonnel)
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
            parentId = parentId,
            isUserGenerated = false
        )
    }

    fun collectImageEntity():List<Image>{
        val returnList = mutableListOf<Image>()
        exercises.forEach {
            returnList.addAll(it.collectImageEntity())
        }
        return returnList
    }

    fun insert(dao: MusculactionLocalDAO, parentId:Long){
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
            short_description = short_description,
            description = description,
            imageID = image.firstOrNull()?.id,
            parentId = parentId,
            isUserGenerated = false
        )
    }

    fun insert(dao: MusculactionLocalDAO, parentId:Long){
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
            parentId = parentId,
            isUserGenerated = false
        )
    }

    fun insert(dao: MusculactionLocalDAO, parentId:Long){
        this.parentId = parentId
        val id = dao.insert(entity)
        video?.forEach {
            dao.insert(ExerciseDetailVideo(
                    videoUrl = it,
                    parentId = id,
                    isUserGenerated = false
                )
            )
        }
    }
}