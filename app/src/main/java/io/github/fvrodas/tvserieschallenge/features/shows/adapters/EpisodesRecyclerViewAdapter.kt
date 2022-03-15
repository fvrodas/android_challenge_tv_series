package io.github.fvrodas.tvserieschallenge.features.shows.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.fvrodas.core.domain.entities.EpisodeEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ItemEpisodeBinding

class EpisodesRecyclerViewAdapter(private val listener: EpisodeRecyclerViewAdapterListener) :
    ListAdapter<EpisodeEntity, EpisodeViewHolder>(EpisodesDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        val previous = if (holder.adapterPosition - 1 >= 0) {
            getItem(holder.adapterPosition - 1)
        } else null
        with(holder) {
            viewBinding.seasonTextview.text =
                itemView.resources.getString(R.string.season_template, item.season)
            previous?.let {
                viewBinding.seasonTextview.visibility = if (item.season != it.season) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            } ?: kotlin.run {
                viewBinding.seasonTextview.visibility = View.VISIBLE
            }
            viewBinding.episodeNumberTextview.text =
                itemView.resources.getString(R.string.episode_template, item.number)
            viewBinding.episodeNameTextview.text = item.name
            itemView.setOnClickListener { listener.onItemPressed(item) }
        }
    }

    override fun onViewRecycled(holder: EpisodeViewHolder) {
        super.onViewRecycled(holder)
        with(holder) {
            viewBinding.seasonTextview.visibility = View.GONE
        }
    }

    companion object {
        interface EpisodeRecyclerViewAdapterListener {
            fun onItemPressed(episode: EpisodeEntity)
        }
    }
}

class EpisodesDiffUtils : DiffUtil.ItemCallback<EpisodeEntity>() {
    override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem == newItem
    }

}


class EpisodeViewHolder(val viewBinding: ItemEpisodeBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    companion object {
        fun create(parent: ViewGroup): EpisodeViewHolder = EpisodeViewHolder(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}