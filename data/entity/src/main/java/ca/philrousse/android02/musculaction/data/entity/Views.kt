package ca.philrousse.android02.musculaction.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CardCategory(
    @Embedded
    private val category: Category,

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image
):ICard {
    override val id: Long?
        get() = category.id
    override val name: String
        get() = category.name
    override val description: String
        get() = category.description
    override val videos: List<String>
        get() = listOf()
}

data class CategoryExercisesCollections(
    @Embedded
    private val category:Category=Category(),

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Subcategory::class
    )

    override val child: List<SubcategoryExercisesCardsCollection> = listOf()
):IViewCardsCollections {
    override val id: Long?
        get() = category.id
    override val name: String
        get() = category.name
    override val description: String
        get() = category.description
}

data class SubcategoryExercisesCardsCollection(
    @Embedded
    private val subcategory: Subcategory = Subcategory(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Exercise::class
    )
    override val child:List<CardExercise> = listOf()
):ICardsCollection {
    override val id: Long?
        get() = subcategory.id
    override val name: String
        get() = subcategory.name
}

data class CardExercise(
    @Embedded
    private val exercise: Exercise,

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image
):ICard {
    override val id: Long?
        get() = exercise.id
    override val name: String
        get() = exercise.name
    override val description: String
        get() = exercise.description
    override val videos: List<String>
        get() = listOf()
}

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
):IViewCards{
    override val id: Long?
        get() = exercise.id
    override val name: String
        get() = exercise.name
    override val description: String
        get() = exercise.description

}

data class CardExerciseDetail(
    @Embedded
    val detail:ExerciseDetail = ExerciseDetail(),

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = ExerciseDetailVideo::class
    )
    private val videosList:List<ExerciseDetailVideo> = listOf()
):ICard {
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