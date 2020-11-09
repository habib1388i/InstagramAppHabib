package com.example.instagramapphabib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramapphabib.R
import com.example.instagramapphabib.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class CommentAdapter(private val mContext: Context, private val mComment: MutableList<com.example.instagramapphabib.model.Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.comment_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mComment.size
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val comment = mComment[position]

        holder.commentTV.text = comment.getComment()

        //create method
        //mengambil data user
        getUserInfo(holder.imageProfilComment, holder.userNameCommentTV, comment.getPublisher())
    }

    class ViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var imageProfilComment: CircleImageView = itemView.findViewById(R.id.user_profile_image_comment)
        var userNameCommentTV: TextView = itemView.findViewById(R.id.user_name_comment)
        var commentTV: TextView = itemView.findViewById(R.id.comment_comment)
    }

    private fun getUserInfo(
        imageProfileComment: CircleImageView,
        userNameComment: TextView,
        publisher: String
    ) {

        val userRef = FirebaseDatabase.getInstance().reference.child("User").child(publisher)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.getValue(User::class.java)

                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                        .into(imageProfileComment)
                    userNameComment.text = user!!.getUsername()
                }
            }

            override fun onCancelled(p0: DatabaseError) {}

        })
    }
}