package com.example.tempo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tempo.R
import com.example.tempo.databinding.ItemListContentBinding
import com.example.tempo.data.model.Article

class NewsListAdapter constructor(val glide:RequestManager): RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    var clickListener:((itemView:View , article: Article)-> Unit)? = null
    var articles:MutableList<Article> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = articles[position]
        holder.tvTitle.text = item.title
        holder.tvSource.text = item.source.name

        glide.load(item.urlToImage)
            .centerCrop()
            .placeholder(R.drawable.ic_news_placeholder)
            .into(holder.ivPic);

        with(holder.itemView) {
            tag = item
            setOnClickListener {
                clickListener?.invoke(it,item)
            }
        }
    }

    override fun getItemCount() = articles.size
    fun addNews(pagingList: MutableList<Article>) {
        articles.addAll(pagingList)
    }

    fun appendNews(pagingList: MutableList<Article>) {
        val oldSize = articles.size
        articles.addAll(pagingList)
        notifyItemRangeInserted(oldSize,pagingList.size)
    }

    fun clearNews(){
        articles.clear()
        notifyDataSetChanged()
    }

    fun searchNews(query:String){
       articles = articles.filter { it.title.equals(query)}.toMutableList()
    }

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvTitle: AppCompatTextView = binding.tvTitle
        val tvSource: AppCompatTextView = binding.tvSource
        val ivPic: AppCompatImageView = binding.ivPic
    }

}