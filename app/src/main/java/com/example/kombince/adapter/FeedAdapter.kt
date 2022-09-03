package com.example.kombince.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kombince.R
import com.example.kombince.view.model.Post

class FeedAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedAdapter.PostHolder>() {

    class PostHolder(var view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return postList.size
    }


}