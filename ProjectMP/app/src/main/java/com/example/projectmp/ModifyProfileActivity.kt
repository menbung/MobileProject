package com.example.projectmp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivityModifyProfileBinding

class ModifyProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityModifyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(intent.getIntExtra("num", 1))
    }

    private fun init(num: Int){
        val bar = supportActionBar
        bar!!.hide()
        binding.apply {
            when(num) {
                2 -> {
                    nicknameView.visibility = View.GONE
                    newPasswdView.visibility = View.GONE
                }
                3 -> {
                    nicknameView.visibility = View.GONE
                    emailView.visibility = View.GONE
                }
                else -> {
                    emailView.visibility = View.GONE
                    newPasswdView.visibility = View.GONE
                }
            }

            closeBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}