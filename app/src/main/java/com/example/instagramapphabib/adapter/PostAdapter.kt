package com.example.instagramapphabib.adapter

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramapphabib.CommentActivity
import com.example.instagramapphabib.MainActivity
import com.example.instagramapphabib.R
import com.example.instagramapphabib.model.Post
import com.example.instagramapphabib.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(private val mContext:Context,private val mPost:List<Post>)
    :RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    private var firebaseUser:FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        val post = mPost[position]

        Picasso.get().load(post.getPostimage()).into(holder.postImage)

        if (post.getDescription().equals("")){
            holder.description.visibility = View.GONE
        }else{
            holder.description.visibility = View.VISIBLE
            holder.description.setText(post.getDescription())
        }
        publisherInfo(holder.profileImage,holder.userName,holder.publisher,post.getPublisher())

        isLikes(post.getPostid(),holder.likeBUtton)
        numberOfLikes(holder.likes,post.getPostid())

        holder.likeBUtton.setOnClickListener {
            if (holder.likeBUtton.tag == "Like"){
                FirebaseDatabase.getInstance().reference
                    .child("Likes").child(post.getPostid()).child(firebaseUser!!.uid)
                    .setValue(true)
            }else{
                FirebaseDatabase.getInstance().reference
                    .child("Likes").child(post.getPostid()).child(firebaseUser!!.uid)
                    .removeValue()

                val intent = Intent(mContext,MainActivity::class.java)
                mContext.startActivity(intent)
            }
        }
        holder.commentButton.setOnClickListener {
            val intentComment = Intent(mContext,CommentActivity::class.java)
            intentComment.putExtra("postId",post.getPostid())
            intentComment.putExtra("publisherId",post.getPublisher())
            mContext.startActivity(intentComment)
        }

        holder.comments.setOnClickListener {
            val intentComment = Intent(mContext,CommentActivity::class.java)
            intentComment.putExtra("postId",post.getPostid())
            intentComment.putExtra("publisherId",post.getPublisher())
            mContext.startActivity(intentComment)
        }

    }



    private fun numberOfLikes(likes: TextView, postid: String) {
        val likesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(postid)

        likesRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    likes.text = p0.childrenCount.toString() + "likes"
                }
            }

        })
    }

    private fun isLikes(postid: String, likeBUtton: ImageView) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val likesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(postid)

        likesRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(firebaseUser!!.uid).exists()){
                    likeBUtton.setImageResource(R.drawable.heart_clicked)
                    likeBUtton.tag = "Liked"
                }else{
                    likeBUtton.setImageResource(R.drawable.heart)
                    likeBUtton.tag = "Like"
                }
            }

        })
    }

    class ViewHolder (@NonNull itemView:View):RecyclerView.ViewHolder(itemView){
        var profileImage:CircleImageView = itemView.findViewById(R.id.user_profile_image_post)
        var postImage:ImageView = itemView.findViewById(R.id.post_image_home)
        var likeBUtton:ImageView = itemView.findViewById(R.id.post_image_like_btn)
        var commentButton:ImageView = itemView.findViewById(R.id.post_image_comment_btn)
        var saveButton:ImageView = itemView.findViewById(R.id.post_save_btn)
        var userName:TextView = itemView.findViewById(R.id.post_user_name)
        var likes:TextView = itemView.findViewById(R.id.post_likes)
        var publisher:TextView = itemView.findViewById(R.id.post_publisher)
        var description:TextView = itemView.findViewById(R.id.post_description)
        var comments:TextView = itemView.findViewById(R.id.post_comments)
    }

    private fun publisherInfo(profileImage: CircleImageView, userName: TextView, publisher: TextView, publisher1: String) {
        val userRef = FirebaseDatabase.getInstance().reference.child("User").child(publisher1)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user = p0.getValue<User>(User::class.java)
                    Picasso.get().load(user?.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    userName.text = user?.getUsername()
                    publisher.text = user?.getFullname()
                }
            }

        })
    }



}