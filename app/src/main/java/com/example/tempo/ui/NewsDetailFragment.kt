package com.example.tempo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tempo.R
import com.example.tempo.databinding.FragmentNewsDetailBinding
import com.example.tempo.data.model.Article
import com.example.tempo.openLink
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {
    private var article: Article? = null
    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                //load item here
                article = (activity as MainActivity).gson.fromJson(it.get(ARG_ITEM_ID).toString(),
                    Article::class.java)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        val rootView = binding?.root

        binding?.toolbarLayout?.title = article?.source?.name
        binding?.toolbarBg?.let {
            (activity as MainActivity).glide.load(article?.urlToImage).centerCrop().placeholder(R.drawable.ic_news_placeholder).into(it)
        }

        binding?.fab?.setOnClickListener {
            context?.openLink(article?.url.toString())
        }

        val tvDesc = binding?.itemDesc
        val tvAuthor = binding?.itemAuthor
        val tvContent = binding?.itemContent
        val tvDate = binding?.itemDate
        val tvSource = binding?.itemSource
        val tvTitle = binding?.itemTitle
        // Show the placeholder content as text in a TextView.
        article?.let {
            tvDesc?.text = it.description
            tvAuthor?.text = it.author
            tvContent?.text = it.content
            tvDate?.text = it.publishedAt
            tvSource?.text = it.source?.name
            tvTitle?.text = it.title
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}