package com.nada.bpaai.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nada.bpaai.data.remote.response.ListStoryItem
import com.nada.bpaai.databinding.ListStoriesBinding

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            getItem(position)?.let { data -> onItemClickCallback.onItemClicked(data) }
        }
    }

    class ViewHolder(private val binding: ListStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.apply {
                tvItemTitle.text = data.name
                tvItemSubtitle.text = data.description

                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(ivItemImage)
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}