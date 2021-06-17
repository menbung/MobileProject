package com.example.projectmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val bar = supportActionBar
        bar!!.hide()
        pref = getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = pref.edit()
        binding.apply {
            tmpBtn.setOnClickListener {
                editor.putString("id", "testid")
                editor.putString("pw", "testpw")
                editor.commit()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }
}