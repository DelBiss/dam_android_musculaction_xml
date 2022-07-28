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
    var name:String
    val id: String?
    override fun equals(other: Any?): Boolean

}

interface IImageDescription:IListElement{
    val image: IDataImage?
    var description: String
    var short_description: String?
}

interface ICard: IImageDescription {
    var video: String?

}

interface ICardsCollection:IListElement {
    var child:List<ICard>
}

interface IViewCardsCollections: IImageDescription {
    var child:List<ICardsCollection>
}

interface IViewCards: IImageDescription {
    var child:List<ICard>
}

data class  EmptyCard(override var name: String) :ICard{
    override var video: String? = null
    override var short_description: String? = null
    override val image: Image? = null
    override var description: String = ""
    override val id: String? = null
}