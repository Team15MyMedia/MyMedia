package com.example.mymedia.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.data.ChannelItem
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.RvChannelItemBinding
import com.example.mymedia.databinding.RvVideosItemBinding

class SearchResultChannelRVAdapter : ListAdapter<ChannelItem, SearchResultChannelRVAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ChannelItem>(){
        override fun areItemsTheSame(oldItem: ChannelItem, newItem: ChannelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChannelItem, newItem: ChannelItem): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvChannelItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: RvChannelItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(channelItem: ChannelItem) = with(binding) {

            Glide.with(root.context)
                .load(channelItem.thumbnail)
                .into(ivChannel)
        }
    }


}