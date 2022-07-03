package ca.philrousse.android02.musculactionX

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.views.ICard
import ca.philrousse.android02.musculaction.data.entity.views.ICardsCollection
import ca.philrousse.android02.musculaction.data.entity.views.ListComparator
import ca.philrousse.android02.musculactionX.databinding.CardCollectionBinding

class CardsCollectionsAdapter(private val onClick: (ICard) -> Unit = {}):
    ListAdapter<ICardsCollection, CardsCollectionsAdapter.CardCollectionsViewHolder>(ListComparator<ICardsCollection>()){

    class CardCollectionsViewHolder(itemView: View,private val onClick: (ICard) -> Unit): RecyclerView.ViewHolder(itemView){
        private var binding: CardCollectionBinding
        private var context: Context

        init {
            binding = CardCollectionBinding.bind(itemView)
            context  = itemView.context
        }

        fun bind(item: ICardsCollection){
            binding.data = item
            val recyclerView: RecyclerView = binding.recyclerView
            val adapter = CardsAdapter(onClick = onClick)
            recyclerView.adapter = adapter
            adapter.submitList(item.child)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCollectionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_collection, parent, false)
        return CardCollectionsViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CardCollectionsViewHolder, position: Int) {
        val produit = getItem(position)
        holder.bind(produit)
    }
}