package com.example.projectmp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivityRestaurantDetailBinding

class RestaurantDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityRestaurantDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        if(intent.hasExtra("res")){
            val myRes = intent.getSerializableExtra("res") as Restaurant

            binding.menuList.isNestedScrollingEnabled = false
            val menuListAdapter = MenuListAdapter(this,myRes.menus)
            binding.menuList.adapter = menuListAdapter

            binding.commentList.isNestedScrollingEnabled = false
            val commentListAdapter = CommentListAdapter(this,myRes.comments)
            binding.commentList.adapter = commentListAdapter

            binding.call.setOnClickListener {
                val input = myRes.number
                val myUri = Uri.parse("tel:${input}")
                val callIntent = Intent(Intent.ACTION_DIAL, myUri)
                startActivity(callIntent)
            }

            binding.name.setText(myRes.resName)
            binding.cate.setText(myRes.category)
            binding.location.setText(myRes.location)

            //menuList.adapter = myRes.menus
        }
    }
}