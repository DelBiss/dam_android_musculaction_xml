package ca.philrousse.android02.musculaction.data.entity.views

import androidx.room.Embedded
import androidx.room.Relation
import ca.philrousse.android02.musculaction.data.entity.*

data class CategoryExercisesCollections(
    @Embedded
    private val category: Category = Category(),

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image? = null,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Subcategory::class
    )

    override var child: List<ICardsCollection> = listOf()
): IViewCardsCollections {
    override val id: String?
        get() = category.id
    override var name: String
        get() = category.name
        set(value) {category.name = value}
    override var description: String
        get() = category.description
        set(value) {category.description = value}
    override var short_description: String?=null
}

data class SubcategoryExercisesCardsCollection(
    @Embedded
    val subcategory: Subcategory = Subcategory(),
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId",
        entity = Exercise::class
    )
    override var child: List<ICard> = listOf()
): ICardsCollection {
    override val id: String?
        get() = subcategory.id
    override var name: String
        get() = subcategory.name
        set(value) {subcategory.name = value}
}

data class CardExercise(
    @Embedded
    private val exercise: Exercise,

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image?
): ICard {
    override val id: String?
        get() = exercise.id
    override var name: String
        get() = exercise.name
        set(value) {exercise.name = value}
    override var description: String
        get() = exercise.description
        set(value) {exercise.description = value}
    override var video: String? = null
    override var short_description: String?
        get() = exercise.short_description
        set(value) {exercise.short_description = value?:""}

}