package ca.philrousse.android02.musculactionX.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.musculaction.data.entity.views.CardExerciseDetail
import ca.philrousse.android02.musculaction.data.entity.views.ICard
import ca.philrousse.android02.musculaction.data.entity.views.ListComparator
import ca.philrousse.android02.musculactionX.R
import ca.philrousse.android02.musculactionX.databinding.LayoutEditSectionBinding

class ExerciseDetailSectionEditAdapter(private val onClick: (ICard) -> Unit = {}):
    ListAdapter<ICard, ExerciseDetailSectionEditAdapter.ExerciseDetailSectionEditViewHolder>(ListComparator<ICard>()){

        class ExerciseDetailSectionEditViewHolder(itemView: View, private val onClick: (ICard) -> Unit): RecyclerView.ViewHolder(itemView){
            private var binding: LayoutEditSectionBinding


            init {
                binding = LayoutEditSectionBinding.bind(itemView)

                binding.actionDelete.setOnClickListener {
                    binding.data?.let {
                        onClick(it)
                    }
                }
            }

            fun bind(item: ICard){
                binding.data = item
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDetailSectionEditViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_edit_section, parent, false)
        return ExerciseDetailSectionEditViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ExerciseDetailSectionEditViewHolder, position: Int) {
        val produit = getItem(position)
        holder.bind(produit)
    }
}