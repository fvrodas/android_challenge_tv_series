package io.github.fvrodas.tvserieschallenge.features.shows.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ItemShowBinding

class ShowsRecyclerViewAdapter : ListAdapter<ShowEntity, ShowViewHolder>(ShowsDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        with(holder) {
            item.poster?.let {
                Glide.with(itemView).load(Uri.parse(it)).into(viewBinding.showPosterImageview)
            } ?: kotlin.run {
                viewBinding.showPosterImageview.setImageResource(R.drawable.ic_shows)
            }
            viewBinding.showTitleTextview.text = item.name
        }
    }

    override fun onViewRecycled(holder: ShowViewHolder) {
        super.onViewRecycled(holder)
        with(holder) {
            Glide.with(itemView).clear(viewBinding.showPosterImageview)
        }
    }
}

class ShowsDiffUtils : DiffUtil.ItemCallback<ShowEntity>() {
    override fun areItemsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean {
        return oldItem == newItem
    }

}


class ShowViewHolder(val viewBinding: ItemShowBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    companion object {
        fun create(parent: ViewGroup): ShowViewHolder = ShowViewHolder(
            ItemShowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}