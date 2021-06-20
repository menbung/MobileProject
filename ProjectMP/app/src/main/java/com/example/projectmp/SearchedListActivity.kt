package com.example.projectmp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmp.databinding.ActivitySearchedListBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchedListActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchedListBinding
    lateinit var layoutManager : LinearLayoutManager
    lateinit var rdb: DatabaseReference
    lateinit var adapter:MyResAdapter
    var mBackWait:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchedListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        showLoadingDialog()
        rdb = FirebaseDatabase.getInstance().getReference("Restaurants")
        val query = rdb.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<Restaurant>().setQuery(query,Restaurant::class.java).build()

        if(option !=null){
            Log.v("option","제대로 가져올 수는 있는지"+option)
        }
        adapter = MyResAdapter(option)
        adapter.itemclickListener = object :MyResAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                //TODO("Not yet implemented")
                val intent = Intent(view.context, RestaurantDetailActivity::class.java)
                intent.putExtra("res",adapter.getItem(position))
                ContextCompat.startActivity(view.context, intent, null)
            }
        }

        binding.apply {
            showRes.layoutManager = layoutManager
            showRes.adapter = adapter

            HomeBtn.setOnClickListener {
                val intent = Intent(this@SearchedListActivity, MainActivity::class.java)
                startActivity(intent)
            }
            WorldcupBtn.setOnClickListener {
                val intent = Intent(this@SearchedListActivity, FoodWorldCupActivity::class.java)
                startActivity(intent)
            }
            MypageBtn.setOnClickListener {
                val intent = Intent(this@SearchedListActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            System.exit(0)
        }
    }

    private fun showLoadingDialog() {
        val dialog = LoadingDialog(this)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(2000)
            dialog.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }
}