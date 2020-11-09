package com.example.instagramapphabib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramapphabib.R
import com.example.instagramapphabib.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_item_layout.view.*

class MyImageAdapter(private val mContext:Context,mPost:List<Post>):RecyclerView.Adapter<MyImageAdapter.ViewHolder?>() {

    private var mPost: List<Post>? = null

    init {
        this.mPost = mPost
    }
    class ViewHolder(@NonNull itemView: View):RecyclerView.ViewHolder(itemView) {

        var postImage : ImageView = itemView.findViewById(R.id.post_image_grid)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImageAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.image_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost!!.size
    }

    override fun onBindViewHolder(holder: MyImageAdapter.ViewHolder, position: Int) {
        val post : Post = mPost!![position]

        Picasso.get().load(post.getPostimage()).into(holder.postImage)
    }
}