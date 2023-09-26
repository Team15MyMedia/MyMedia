package com.example.mymedia.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mymedia.data.Item
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymedia.R
import com.example.mymedia.databinding.VideoItemBinding

class HomeVideoListAdapter : ListAdapter<Item, HomeVideoListAdapter.ViewHolder>(ItemDiffCallback()) {

    // 롱클릭 리스너 인터페이스 정의
    interface OnItemLongClickListener {
        fun onItemLongClick(item: Item)
    }

    private var onItemLongClickListener: OnItemLongClickListener? = null

    // 롱클릭 리스너 설정 메서드
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: VideoItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            with(binding) {
                posterImageView.setImageResource(R.drawable.img_netflix)

                itemView.setOnLongClickListener {
                    onItemLongClickListener?.onItemLongClick(item)
                    true
                }
            }
        }
    }
}

private class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id // 아이템의 고유 식별자를 비교
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem // 아이템의 내용을 비교
    }
}
