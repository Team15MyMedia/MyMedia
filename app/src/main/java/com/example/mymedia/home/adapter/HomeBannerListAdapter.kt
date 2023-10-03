package com.example.mymedia.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mymedia.data.VideoItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.databinding.RvHomeBannerItemBinding

class HomeBannerListAdapter :
    ListAdapter<VideoItem, HomeBannerListAdapter.ViewHolder>(
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
            RvHomeBannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: RvHomeBannerItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(videoItem.thumbnail.replace("default_live.jpg", "hqdefault_live.jpg"))
                    .into(bannerImg)

                itemView.setOnLongClickListener {
                    onItemLongClickListener?.onItemLongClick(videoItem)
                    true
                }
            }
        }
    }
}