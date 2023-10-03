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

class SearchResultVideoRVAdapter : ListAdapter<VideoItem, SearchResultVideoRVAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<VideoItem>(){
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem == newItem
        }

    }
) {

    interface OnItemClickListener {
        fun onItemClick(videoItem: VideoItem)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvVideosItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: RvVideosItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(videoItem: VideoItem) = with(binding) {

            Glide.with(root.context)
                .load(videoItem.thumbnail)
                .into(ivVideos)

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(videoItem)
                true
            }
        }
    }

}