package com.example.projectmp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivityEmailAuthBinding
import com.google.firebase.auth.FirebaseAuth

class EmailAuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmailAuthBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user!!.isEmailVerified) {
            binding.notifyText.text = "이미 인증된 메일입니다."
            binding.sendEmail.visibility = View.GONE
        }
        else {
            val mailDomain = user.email!!.split("@")[1]
            if (mailDomain.equals("konkuk.ac.kr")) {
                user.sendEmailVerification().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "인증 메일 발송", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.notifyText.text = "계정의 이메일 주소로 인증 메일을 발송했습니다." +
                        "\n만약 메일이 보이지 않다면 재발송 버튼을 눌러주세요." +
                        "\n인증 후 로그아웃 및 재 로그인 하시기바랍니다."
            }
            else {
                binding.notifyText.text = "건대 메일이 아닙니다.\n건대 메일로 변경 후 시도해주시기 바랍니다."
                binding.sendEmail.visibility = View.GONE
            }
        }
        binding.apply {
            closeBtn.setOnClickListener {
                finish()
            }
            sendEmail.setOnClickListener {
                user.sendEmailVerification().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@EmailAuthActivity, "인증 메일 발송", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}