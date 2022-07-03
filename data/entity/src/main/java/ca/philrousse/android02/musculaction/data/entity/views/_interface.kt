package ca.philrousse.android02.musculaction.data.entity.views

import androidx.recyclerview.widget.DiffUtil
import ca.philrousse.android02.musculaction.data.entity.Image

class ListComparator<T: IListElement>:DiffUtil.ItemCallback<T>(){
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

}

interface IListElement{
    val name:String
    val id:Long?
    override fun equals(other: Any?): Boolean
}

interface ICard: IListElement {
    val image: Image?
    val description:String?
    val video: String?
}

interface ICardsCollection: IListElement {
    val child:List<ICard>
}

interface IViewCardsCollections: IListElement {
    val image: Image?
    val description:String?
    val child:List<ICardsCollection>
}

interface IViewCards: IListElement {
    val image: Image?
    val description:String?
    val child:List<ICard>
}
