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
import ca.philrousse.android02.musculactionX.databinding.ItemDetailSectionBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController


class CardsDetailAdapter:
    ListAdapter<ICard, CardsDetailAdapter.CardDetailViewHolder>(ListComparator<ICard>()){

        class CardDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private var binding: ItemDetailSectionBinding
            private var context:Context
            private var currentCard:ICard? = null
            init {

                binding = ItemDetailSectionBinding.bind(itemView)
                context  = itemView.context


            }
            fun bind(item: ICard){
                currentCard = item
                binding.data = item

                item.video?.also {
                    binding.videoPlayer.enableAutomaticInitialization = false
                    val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            // We're using pre-made custom ui
                            val defaultPlayerUiController =
                                DefaultPlayerUiController(binding.videoPlayer, youTubePlayer)
                            defaultPlayerUiController.showFullscreenButton(false)





                            binding.videoPlayer.setCustomPlayerUi(defaultPlayerUiController.rootView)
                            youTubePlayer.cueVideo(it, 0f)
                        }
                    }
                    // Disable iFrame UI
                    val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
                    binding.videoPlayer.initialize(listener, options)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_section, parent, false)
        return CardDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardDetailViewHolder, position: Int) {
        val produit = getItem(position)
        holder.bind(produit)
    }
}