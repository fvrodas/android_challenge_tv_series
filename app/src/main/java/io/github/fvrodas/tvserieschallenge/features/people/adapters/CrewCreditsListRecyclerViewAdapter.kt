package io.github.fvrodas.tvserieschallenge.features.people.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.fvrodas.core.domain.entities.CrewCreditEntity
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ItemShowAltBinding

class CrewCreditsListRecyclerViewAdapter(private val listener: CrewCreditsRecyclerViewAdapterListener) :
    ListAdapter<CrewCreditEntity, FavoriteShowViewHolder>(CrewCreditsDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteShowViewHolder {
        return FavoriteShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavoriteShowViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        val previous = if (holder.adapterPosition - 1 >= 0) {
            getItem(holder.adapterPosition - 1)
        } else null
        with(holder) {
            viewBinding.letterTextview.text = item.type
            previous?.let {
                viewBinding.letterTextview.visibility =
                    if (item.type != it.type) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            } ?: kotlin.run {
                viewBinding.letterTextview.visibility = View.VISIBLE
            }
            item.show?.poster?.let {
                Glide.with(itemView).load(Uri.parse(it)).into(viewBinding.showPosterImageview)
            } ?: kotlin.run {
                viewBinding.showPosterImageview.setImageResource(R.drawable.ic_shows)
            }
            viewBinding.showTitleTextview.text = item.show?.name
            itemView.setOnClickListener { listener.onItemPressed(item.show!!) }
        }
    }

    override fun onViewRecycled(holder: FavoriteShowViewHolder) {
        super.onViewRecycled(holder)
        with(holder) {
            Glide.with(itemView).clear(viewBinding.showPosterImageview)
        }
    }

    companion object {
        interface CrewCreditsRecyclerViewAdapterListener {
            fun onItemPressed(show: ShowEntity)
        }
    }
}

class CrewCreditsDiffUtils : DiffUtil.ItemCallback<CrewCreditEntity>() {
    override fun areItemsTheSame(oldItem: CrewCreditEntity, newItem: CrewCreditEntity): Boolean {
        return oldItem.show?.id == newItem.show?.id
    }

    override fun areContentsTheSame(oldItem: CrewCreditEntity, newItem: CrewCreditEntity): Boolean {
        return oldItem == newItem
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