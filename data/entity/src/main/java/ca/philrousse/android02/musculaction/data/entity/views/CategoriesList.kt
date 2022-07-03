package ca.philrousse.android02.musculaction.data.entity.views

import androidx.room.Embedded
import androidx.room.Relation
import ca.philrousse.android02.musculaction.data.entity.Category
import ca.philrousse.android02.musculaction.data.entity.Image

data class CardCategory(
    @Embedded
    private val category: Category,

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image
): ICard {
    override val id: Long?
        get() = category.id
    override val name: String
        get() = category.name
    override val description: String
        get() = ""
    override val videos: List<String>
        get() = listOf()
}