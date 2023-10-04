package com.example.mymedia.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.MypageVideoItemBinding

class MyPageFavoriteListAdapter : ListAdapter<VideoItem, MyPageFavoriteListAdapter.ViewHolder>(
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPageFavoriteListAdapter.ViewHolder {
        return ViewHolder(
            MypageVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyPageFavoriteListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: MypageVideoItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) {
            with(binding) {
                // 이미지 설정
                Glide.with(itemView.context)
                    .load(videoItem.thumbnail.replace("/default.jpg", "/maxresdefault.jpg"))
                    .into(favoriteImageView)
                favoriteImageView.clipToOutline = true

                favoriteTitleTextView.text = videoItem.title

                itemView.setOnLongClickListener {
                    onItemLongClickListener?.onItemLongClick(videoItem)
                    true
                }
            }
        }
    }
}
