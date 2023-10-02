package com.example.mymedia.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.RvSearchItemBinding
import com.example.mymedia.databinding.VideoItemBinding
import com.example.mymedia.home.adapter.HomeCategoryVideoListAdapter
import com.example.mymedia.home.getHighQualityThumbnailUrl


class SearchRVAdapter : ListAdapter<VideoItem, SearchRVAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<VideoItem>(){

        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem == newItem
        }

    }
) {

    // 롱클릭 리스너 인터페이스 정의
    interface OnItemLongClickListener {
        fun onItemLongClick(videoItem: VideoItem)
    }

    private var onItemLongClickListener: OnItemLongClickListener? = null

    // 롱클릭 리스너 설정 메서드
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RvSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: RvSearchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) = with(binding) {

            Glide.with(root.context)
                .load(videoItem.thumbnail)
                .into(ivMedia)


            tvTitle.text = videoItem.title

            itemView.setOnLongClickListener {
                onItemLongClickListener?.onItemLongClick(videoItem)
                true
            }
        }
    }

}