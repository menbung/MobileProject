package com.example.projectmp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var dbHelper : DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        getALLRecord()
    }

    private fun getALLRecord() {
        dbHelper.getAllRecord()
    }

    private fun init() {
        dbHelper = DBHelper(this)

        binding.apply{

            btn.setOnClickListener {
                val str = EditSearch.text.toString()
                val result = dbHelper.findRest(str)
                if(result){
                    Toast.makeText(this@SearchActivity, "찾았어요!",
                        Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@SearchActivity, "찾는 가게가 없어요!",
                        Toast.LENGTH_SHORT).show()
                }

                clearInput()
            }
        }
    }

    private fun clearInput() {
        binding.EditSearch.text.clear()
    }
}