package ca.philrousse.android02.musculaction.data.entity.views

import androidx.room.Embedded
import androidx.room.Relation
import ca.philrousse.android02.musculaction.data.entity.Category
import ca.philrousse.android02.musculaction.data.entity.IDataCategory
import ca.philrousse.android02.musculaction.data.entity.Image

data class CardCategory(
    @Embedded
    private val category: IDataCategory,

    @Relation(
        parentColumn = "imageID",
        entityColumn = "id",
        entity = Image::class
    )
    override val image: Image?
): ICard {
    override val id: String?
        get() = category.id

    override var name: String = ""
        get() = category.name
    override var description: String = ""
        get() = ""
    override var video: String? = null
        get() = null
    override var short_description: String? = null
}