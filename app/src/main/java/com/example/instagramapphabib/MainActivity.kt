package com.example.instagramapphabib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.instagramapphabib.fragment.HomeFragment
import com.example.instagramapphabib.fragment.NotificationFragment
import com.example.instagramapphabib.fragment.ProfileFragment
import com.example.instagramapphabib.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId){
            R.id.nav_home ->{
                moveToFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_search ->{
                moveToFragment(SearchFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_add_post-> {
                item.isChecked = false
                startActivity(Intent(this@MainActivity,AddPostActivity::class.java))
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_add_post ->{
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_notification ->{
                moveToFragment(NotificationFragment())
                return@OnNavigationItemSelectedListener true
            }

            R.id.nav_profile ->{
                moveToFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }

        }

        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        moveToFragment(HomeFragment())

//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        navView.setOnNavigationItemReselectedListener(onNavigationItemSelectedListener)
//        moveToFragment(HomeFragment())


    }

    private fun moveToFragment(fragment: Fragment){
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }
}