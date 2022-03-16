package io.github.fvrodas.tvserieschallenge.features.people.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ItemCardBinding

class PeopleRecyclerViewAdapter(private val listener: PeopleRecyclerViewAdapterListener) :
    ListAdapter<PersonEntity, ShowViewHolder>(PeopleDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        with(holder) {
            item.image?.let {
                Glide.with(itemView).load(Uri.parse(it)).into(viewBinding.showPosterImageview)
            } ?: kotlin.run {
                viewBinding.showPosterImageview.setImageResource(R.drawable.ic_person)
            }
            viewBinding.showTitleTextview.text = item.name
            itemView.setOnClickListener { listener.onItemPressed(item) }
        }
    }

    override fun onViewRecycled(holder: ShowViewHolder) {
        super.onViewRecycled(holder)
        with(holder) {
            Glide.with(itemView).clear(viewBinding.showPosterImageview)
        }
    }

    companion object {
        interface PeopleRecyclerViewAdapterListener {
            fun onItemPressed(person: PersonEntity)
        }
    }
}

class PeopleDiffUtils : DiffUtil.ItemCallback<PersonEntity>() {
    override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
        return oldItem == newItem
    }

}


class ShowViewHolder(val viewBinding: ItemCardBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    companion object {
        fun create(parent: ViewGroup): ShowViewHolder = ShowViewHolder(
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}