package com.example.mymedia.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymedia.data.VideoItem
import com.example.mymedia.databinding.RvSearchItemBinding


class SearchRVAdapter : RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {

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
            RvSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: RvSearchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) = with(binding) {

//            Glide.with(root.context)
//                .load(item.url)
//                .into(ivMedia)

            Glide.with(root.context)
                .load("https://i.pinimg.com/236x/f4/13/d0/f413d09e8b1b08b0138f63c033dd9237.jpg")
                .into(ivMedia)


            tvTitle.text = videoItem.title
        }
    }

}