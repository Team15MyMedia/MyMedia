package com.example.mymedia.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mymedia.data.VideoItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.databinding.RvHomeVideoItemBinding


class HomeCategoryVideoListAdapter :
    ListAdapter<VideoItem, HomeCategoryVideoListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(
                oldVideoItem: VideoItem,
                newVideoItem: VideoItem
            ): Boolean {
                return oldVideoItem.id == newVideoItem.id
            }

            override fun areContentsTheSame(
                oldVideoItem: VideoItem,
                newVideoItem: VideoItem
            ): Boolean {
                return oldVideoItem == newVideoItem
            }
        }) {

    // 롱클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(videoItem: VideoItem)
    }

    private var onItemClickListener: OnItemClickListener? = null

    // 롱클릭 리스너 설정 메서드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvHomeVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: RvHomeVideoItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) {
            with(binding) {
                // 이미지 설정
                Glide.with(itemView.context)
                    .load(videoItem.thumbnail.replace("/default.jpg", "/maxresdefault.jpg"))
                    .into(posterImageView)
                posterImageView.clipToOutline = true

                itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(videoItem)
                    true
                }
            }
        }
    }
}