package ca.philrousse.android02.musculactionX.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.views.ICard
import ca.philrousse.android02.musculaction.data.entity.views.ListComparator
import ca.philrousse.android02.musculactionX.R
import ca.philrousse.android02.musculactionX.databinding.ItemCardSimpleBinding


class CardsAdapter(private val onClick: (ICard) -> Unit = {}):
    ListAdapter<ICard, CardsAdapter.CardViewHolder>(ListComparator<ICard>()){

        class CardViewHolder(itemView: View,private val onClick: (ICard) -> Unit): RecyclerView.ViewHolder(itemView){
            private var binding: ItemCardSimpleBinding
            private var context:Context
            private var currentCard:ICard? = null
            init {
                binding = ItemCardSimpleBinding.bind(itemView)
                context  = itemView.context

                itemView.setOnClickListener {
                    currentCard?.let {
                        onClick(it)
                    }
                }
            }

            fun bind(item: ICard){
                currentCard = item
                binding.data = item
//                item.image?.getResourceId(context)?.also {
//                    binding.image = it
//                }
//                item.image?.getSmallestResourceId(context)?.also {
//                    binding.imageSmall = it
//                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_simple, parent, false)
        return CardViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val produit = getItem(position)
        holder.bind(produit)
    }
}