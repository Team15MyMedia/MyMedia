package com.example.mymedia.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.data.ChannelItem
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.RvChannelItemBinding
import com.example.mymedia.databinding.RvVideosItemBinding

class SearchResultVideoRVAdapter : RecyclerView.Adapter<SearchResultVideoRVAdapter.ViewHolder>() {

    private val list = ArrayList<VideoItem>()

    fun addItems(videoItems: List<VideoItem>) {
        list.addAll(videoItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvVideosItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    class ViewHolder(private val binding: RvVideosItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(videoItem: VideoItem) = with(binding) {

            Glide.with(root.context)
                .load(videoItem.thumbnail)
                .into(ivVidoes)
        }
    }

}