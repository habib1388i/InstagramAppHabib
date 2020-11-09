package com.example.instagramapphabib

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.awt.font.TextAttribute

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
       btn_singin_link.setOnClickListener {
           startActivity(Intent(this,LoginActivity::class.java))
       }
        btn_register.setOnClickListener {
            createAccount()
        }

    }



    private fun createAccount() {
        val fullName= fullname_register.text.toString()
        val userName= username_register.text.toString()
        val email= email_register.text.toString()
        val password= password_register.text.toString()

        when{

            TextUtils.isEmpty(fullName) -> Toast.makeText(this,"Full name is required",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(userName) -> Toast.makeText(this,"User name is required",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this,"Email is required",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this,"Password is required",Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Register")
                progressDialog.setMessage("Please wait. . .")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->

                    if (task.isSuccessful){
                        seveUserInfo(fullName,userName,email,progressDialog)
                    }else{
                        val message = task.exception!!.toString()
                        Toast.makeText(this,"eror: $message",Toast.LENGTH_LONG).show()
                        mAuth.signOut()
                        progressDialog.dismiss()
                    }

                }
            }

        }

    }

    private fun seveUserInfo(fullName: String, userName: String, email: String, progressDialog: ProgressDialog) {

        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("User")
        val userMap = HashMap<String,Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName.toLowerCase()
        userMap["username"] = userName.toLowerCase()
        userMap["email"] = email
        userMap["bio"] = "Hey Iam student at IDN Boarding School "
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/com.example.instagramapphabib/b1547d7f-9028-4dca-9d68-f38d100725a4"

        usersRef.child(currentUserID).setValue(userMap).addOnCompleteListener { task ->

            if (task.isSuccessful){
                progressDialog.dismiss()
                Toast.makeText(this,"Accont sudah dibuat", Toast.LENGTH_LONG).show()
                val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }else{
                val massage = task.exception!!.toString()
                Toast.makeText(this,"Eror: $massage", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()
            }


            
        }

    }

}