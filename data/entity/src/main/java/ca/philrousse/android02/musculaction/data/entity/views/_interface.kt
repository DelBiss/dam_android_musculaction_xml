package ca.philrousse.android02.musculaction.data.entity.views

import androidx.recyclerview.widget.DiffUtil
import ca.philrousse.android02.musculaction.data.entity.IDataImage
import ca.philrousse.android02.musculaction.data.entity.Image

class ListComparator<V, T: IListElement<V>>:DiffUtil.ItemCallback<T>(){
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

}

interface IListElement<out T> {
    val name:String
    val id:Long?
    override fun equals(other: Any?): Boolean
    fun asRemoteObject():T
}

interface IImageDescription<out T>:IListElement<T>{
    val image: IDataImage?
    val description:String?
}

interface ICard<out T>: IImageDescription<T> {
    val video: String?
}

interface ICardsCollection<out T, out V>:IListElement<T> {
    val child:List<ICard<V>>
}

interface IViewCardsCollections<out T, out V>: IImageDescription<T> {
    val child:List<ICardsCollection<V>>
}

interface IViewCards<out T, out V>: IImageDescription<T> {
    val child:List<ICard<V>>
}

data class  EmptyCard(override val name: String) :ICard<Any?>{
    override val video: String?
        get() = null
    override val image: Image?
        get() = null
    override val description: String?
        get() = null
    override val id: Long?
        get() = null

    override fun asRemoteObject(): Any? {
        return null
    }

}