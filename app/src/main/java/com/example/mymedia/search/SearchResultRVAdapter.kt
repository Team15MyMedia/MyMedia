package com.example.mymedia.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.data.Item
import com.example.mymedia.databinding.RvChannelItemBinding
import com.example.mymedia.databinding.RvVideosItemBinding

class SearchResultRVAdapter(val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<Item>()

    fun addItems(items: List<Item>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            1 -> {
                // 채널 레이아웃용 뷰 홀더 생성
                val binding = RvChannelItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ChannelViewHolder(binding)
            }
            2 -> {
                // 비디오 레이아웃용 뷰 홀더 생성
                val binding = RvVideosItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                VideosViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (holder) {
            is ChannelViewHolder -> {
                // 채널 레이아웃에 데이터 바인딩
                holder.bindChannel(item)
            }
            is VideosViewHolder -> {
                // 비디오 레이아웃에 데이터 바인딩
                holder.bindVideos(item)
            }
        }
    }

    class ChannelViewHolder(private val binding: RvChannelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindChannel(item: Item) = with(binding) {

            Glide.with(root.context)
                .load("https://i.pinimg.com/236x/f4/13/d0/f413d09e8b1b08b0138f63c033dd9237.jpg")
                .into(ivChannel)
        }
    }

    class VideosViewHolder(private val binding: RvVideosItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindVideos(item: Item) = with(binding) {

            Glide.with(root.context)
                .load("https://i.pinimg.com/236x/f4/13/d0/f413d09e8b1b08b0138f63c033dd9237.jpg")
                .into(ivVidoes)
        }
    }

}