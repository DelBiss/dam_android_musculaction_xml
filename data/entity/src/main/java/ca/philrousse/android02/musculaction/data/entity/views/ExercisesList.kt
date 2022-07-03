package ca.philrousse.android02.musculaction.data.entity.views

import androidx.room.Embedded
import androidx.room.Relation
import ca.philrousse.android02.musculaction.data.entity.Category
import ca.philrousse.android02.musculaction.data.entity.Exercise
import ca.philrousse.android02.musculaction.data.entity.Image
import ca.philrousse.android02.musculaction.data.entity.Subcategory

data class CategoryExercisesCollections(
    @Embedded
    private val category: Category = Category(),

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
): IViewCardsCollections {
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
): ICardsCollection {
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
): ICard {
    override val id: Long?
        get() = exercise.id
    override val name: String
        get() = exercise.name
    override val description: String
        get() = exercise.short_description
    override val videos: List<String>
        get() = listOf()
}