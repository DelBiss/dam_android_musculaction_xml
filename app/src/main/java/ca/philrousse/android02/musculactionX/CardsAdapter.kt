package ca.philrousse.android02.musculactionX


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.IImageCard
import ca.philrousse.android02.musculaction.data.entity.ListComparator
import ca.philrousse.android02.musculactionX.databinding.CardSimpleBinding


class CardsAdapter:
    ListAdapter<IImageCard, CardsAdapter.CardViewHolder>(ListComparator<IImageCard>()){

        class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private var binding: CardSimpleBinding
            private var context:Context
            init {
                binding = CardSimpleBinding.bind(itemView)
                context  = itemView.context
            }

            fun bind(item:IImageCard){
                binding.cardData = item
                item.getDrawable(context)?.also {
                    binding.image.setImageDrawable(it)
                }


            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_simple, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val produit = getItem(position)
        holder.bind(produit)
    }
}