package ca.philrousse.android02.musculactionX.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.views.EmptyCard
import ca.philrousse.android02.musculaction.data.entity.views.ICard
import ca.philrousse.android02.musculaction.data.entity.views.ICardsCollection
import ca.philrousse.android02.musculaction.data.entity.views.ListComparator
import ca.philrousse.android02.musculactionX.R
import ca.philrousse.android02.musculactionX.databinding.ItemCardCollectionBinding

class CardsCollectionsAdapter(private val newCallback: () -> Unit = {}, private val onClick: (ICard) -> Unit = {}):
    ListAdapter<ICardsCollection, CardsCollectionsAdapter.CardCollectionsViewHolder>(ListComparator<ICardsCollection>()){

    class CardCollectionsViewHolder(itemView: View,private val newCallback: () -> Unit = {}, private val onClick: (ICard) -> Unit): RecyclerView.ViewHolder(itemView){
        private var binding: ItemCardCollectionBinding
        private var context: Context

        init {
            binding = ItemCardCollectionBinding.bind(itemView)
            context  = itemView.context
        }

        fun bind(item: ICardsCollection){
            binding.data = item
            val recyclerView: RecyclerView = binding.recyclerView

            recyclerView.adapter = if(item.child.isNotEmpty()){
                CardsAdapter(onClick = onClick).also {
                  it.submitList(item.child)
                }
            } else {
                CardsAdapter{
                    newCallback()
                }.also {
                    it.submitList(listOf(EmptyCard("Ajouter un exercise personnel")))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCollectionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_collection, parent, false)
        return CardCollectionsViewHolder(view, newCallback, onClick)
    }

    override fun onBindViewHolder(holder: CardCollectionsViewHolder, position: Int) {
        val produit = getItem(position)
        holder.bind(produit)
    }
}