package ca.philrousse.android02.musculaction.data.entity.views

import androidx.room.Embedded
import androidx.room.Relation
import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetailVideo
import ca.philrousse.android02.musculaction.data.entity.Image

data class ExerciseView(
    @Embedded
    val exercise: Exercise = Exercise(),

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image? = null,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetail::class
    )
    override var child: List<ICard> = listOf()
): IViewCards {

    constructor(parentId:String, image: String?=null):this(Exercise(parentId = parentId, imageID = image))

    override val id: String?
        get() = exercise.id

    override var name: String
        get() = exercise.name
        set(value) {
            exercise.name = value
        }
    override var description: String
        get() = exercise.description
        set(value) {
            exercise.description = value
        }
    override var short_description: String?
        get() = exercise.short_description
        set(value) {
            if (value != null) {
                exercise.short_description = value
            }
        }
}

data class CardExerciseDetail(
    @Embedded
    val detail: ExerciseDetail = ExerciseDetail(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetailVideo::class
    )
    var videosList:List<ExerciseDetailVideo> = listOf()
): ICard {
    constructor(name:String, parentId: String?):this(ExerciseDetail(name, parentId = parentId))
    override val id: String?
        get() = detail.id
    override var name: String
        get() = detail.name
        set(value) {
            detail.name = value
        }
    override val image: Image?
        get() = null
    override var description: String
        get() = detail.description
        set(value) {
            detail.description = value
        }
    override var video: String?
        get() = videosList.firstOrNull()?.let {
            it.videoUrl.split("/").lastOrNull()?.split("?")?.firstOrNull()
        }
        set(value) {
            value?.also { videoUrl ->
                videosList.firstOrNull()?.let {
                    it.videoUrl = videoUrl
                }?: run{
                    videosList = listOf(ExerciseDetailVideo(videoUrl))
                }
            }
        }
    override var short_description: String? = null
}