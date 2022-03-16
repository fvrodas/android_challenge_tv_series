package io.github.fvrodas.tvserieschallenge.features.favorite_shows.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ItemShowAltBinding
import io.github.fvrodas.tvserieschallenge.features.shows.adapters.ShowsDiffUtils

class FavoriteShowsListRecyclerViewAdapter(private val listener: FavoriteShowsRecyclerViewAdapterListener) :
    ListAdapter<ShowEntity, FavoriteShowViewHolder>(ShowsDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteShowViewHolder {
        return FavoriteShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteShowViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        val previous = if (holder.adapterPosition - 1 >= 0) {
            getItem(holder.adapterPosition - 1)
        } else null
        with(holder) {
            viewBinding.letterTextview.text = item.name?.first().toString()
            previous?.let {
                viewBinding.letterTextview.visibility =
                    if (item.name?.first() != it.name?.first()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            } ?: kotlin.run {
                viewBinding.letterTextview.visibility = View.VISIBLE
            }
            item.poster?.let {
                Glide.with(itemView).load(Uri.parse(it)).into(viewBinding.showPosterImageview)
            } ?: kotlin.run {
                viewBinding.showPosterImageview.setImageResource(R.drawable.ic_shows)
            }
            viewBinding.showTitleTextview.text = item.name
            itemView.setOnClickListener { listener.onItemPressed(item) }
            itemView.setOnLongClickListener { listener.onItemLongPressed(item) }
        }
    }

    override fun onViewRecycled(holder: FavoriteShowViewHolder) {
        super.onViewRecycled(holder)
        with(holder) {
            Glide.with(itemView).clear(viewBinding.showPosterImageview)
        }
    }

    companion object {
        interface FavoriteShowsRecyclerViewAdapterListener {
            fun onItemPressed(show: ShowEntity)
            fun onItemLongPressed(show: ShowEntity): Boolean
        }
    }
}

class FavoriteShowViewHolder(val viewBinding: ItemShowAltBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {
    companion object {
        fun create(parent: ViewGroup): FavoriteShowViewHolder = FavoriteShowViewHolder(
            ItemShowAltBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}