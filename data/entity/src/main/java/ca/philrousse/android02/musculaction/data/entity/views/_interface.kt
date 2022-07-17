package ca.philrousse.android02.musculaction.data.entity.views

import androidx.recyclerview.widget.DiffUtil
import ca.philrousse.android02.musculaction.data.entity.IDataImage
import ca.philrousse.android02.musculaction.data.entity.Image

class ListComparator<T: IListElement>:DiffUtil.ItemCallback<T>(){
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

}

interface IListElement {
    val name:String
    val id:Long?
    override fun equals(other: Any?): Boolean

}

interface IImageDescription:IListElement{
    val image: IDataImage?
    val description:String?
}

interface ICard: IImageDescription {
    val video: String?
}

interface ICardsCollection:IListElement {
    val child:List<ICard>
}

interface IViewCardsCollections: IImageDescription {
    val child:List<ICardsCollection>
}

interface IViewCards: IImageDescription {
    val child:List<ICard>
}

data class  EmptyCard(override val name: String) :ICard{
    override val video: String?
        get() = null
    override val image: Image?
        get() = null
    override val description: String?
        get() = null
    override val id: Long?
        get() = null

}