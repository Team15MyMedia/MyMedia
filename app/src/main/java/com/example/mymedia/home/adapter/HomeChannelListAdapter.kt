package com.example.mymedia.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.data.ChannelItem
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.ChannelItemBinding

class HomeChannelListAdapter :
    ListAdapter<ChannelItem, HomeChannelListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<ChannelItem>() {
            override fun areItemsTheSame(
                oldVideoItem: ChannelItem,
                newVideoItem: ChannelItem
            ): Boolean {
                return oldVideoItem.id == newVideoItem.id
            }

            override fun areContentsTheSame(
                oldVideoItem: ChannelItem,
                newVideoItem: ChannelItem
            ): Boolean {
                return oldVideoItem == newVideoItem
            }
        }) {

    // 롱클릭 리스너 인터페이스 정의
    interface OnItemLongClickListener {
        fun onItemLongClick(channelItem: ChannelItem)
    }

    private var onItemLongClickListener: OnItemLongClickListener? = null

    // 롱클릭 리스너 설정 메서드
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChannelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ChannelItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(channelItem: ChannelItem) {
            with(binding) {
                // 이미지 설정
                Glide.with(itemView.context)
                    .load(channelItem.thumbnail)
                    .into(channelImageView)
                channelImageView.clipToOutline = true

                itemView.setOnLongClickListener {
                    onItemLongClickListener?.onItemLongClick(channelItem)
                    true
                }
            }
        }
    }
}