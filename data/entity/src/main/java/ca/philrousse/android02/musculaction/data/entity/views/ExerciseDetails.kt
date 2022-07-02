package ca.philrousse.android02.musculaction.data.entity.views

import androidx.room.Embedded
import androidx.room.Relation
import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.ExerciseDetailVideo
import ca.philrousse.android02.musculaction.data.entity.Image

data class ExerciseView(
    @Embedded
    private val exercise: Exercise = Exercise(),

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetail::class
    )
    override val child:List<CardExerciseDetail> = listOf()
): IViewCards {
    override val id: Long?
        get() = exercise.id
    override val name: String
        get() = exercise.name
    override val description: String
        get() = exercise.description

}

data class CardExerciseDetail(
    @Embedded
    val detail: ExerciseDetail = ExerciseDetail(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetailVideo::class
    )
    private val videosList:List<ExerciseDetailVideo> = listOf()
): ICard {
    override val id: Long?
        get() = detail.id
    override val name: String
        get() = detail.name
    override val image: Image?
        get() = null
    override val description: String
        get() = detail.description
    override val videos: List<String>
        get() = videosList.map {
            it.videoUrl
        }
}