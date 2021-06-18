package com.example.projectmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivityModifyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ModifyProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityModifyProfileBinding
    lateinit var pref: SharedPreferences
    lateinit var db: DatabaseReference
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(intent.getIntExtra("num", 1))
    }

    private fun init(num: Int){
        val bar = supportActionBar
        bar!!.hide()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("Users")
        pref = getSharedPreferences("login", Context.MODE_PRIVATE)
        val user = auth.currentUser
        val id = pref.getString("id", "")
        val nickName = pref.getString("nickname", "")
        binding.apply {
            when(num) {
                2 -> {
                    modifyTitle.text = "이메일 변경"
                    confirm.text = "이메일 변경"
                    emailEdit.setText(user!!.email)
                    nicknameView.visibility = View.GONE
                    newPasswdView.visibility = View.GONE
                }
                3 -> {
                    modifyTitle.text = "비밀번호 변경"
                    confirm.text = "비밀번호 변경"
                    nicknameView.visibility = View.GONE
                    emailView.visibility = View.GONE
                }
                4 -> {
                    modifyTitle.text = "회원탈퇴"
                    confirm.text = "탈퇴"
                    nicknameView.visibility = View.GONE
                    emailView.visibility = View.GONE
                    newPasswdView.visibility = View.GONE
                }
                else -> {
                    modifyTitle.text = "닉네임 변경"
                    confirm.text = "닉네임 변경"
                    nicknameEdit.setText(nickName)
                    emailView.visibility = View.GONE
                    newPasswdView.visibility = View.GONE
                    passwdView.visibility = View.GONE
                }
            }

            closeBtn.setOnClickListener {
                finish()
            }

            confirm.setOnClickListener {
                val editor = pref.edit()
                when(num) {
                    2 -> {
                        user!!.updateEmail(emailEdit.text.toString()).addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                db.child(id!!).child("email").setValue(emailEdit.text.toString())
                                Toast.makeText(this@ModifyProfileActivity, "이메일 변경 완료", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ModifyProfileActivity, MainActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                            }
                            else {
                                Toast.makeText(this@ModifyProfileActivity, "잘못된 입력입니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    3 -> {
                        if (newPasswdEdit.text.toString().equals(newPasswdAgainEdit.text.toString())) {
                            user!!.updatePassword(newPasswdEdit.text.toString())
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@ModifyProfileActivity, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                        }
                        else {
                            Toast.makeText(this@ModifyProfileActivity, "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                    4 -> {
                        user!!.delete().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ModifyProfileActivity, "회원탈퇴 완료", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else -> {
                        db.child(id!!).child("nickname").setValue(nicknameEdit.text.toString())
                        editor.putString("nickname", nicknameEdit.text.toString())
                        editor.commit()
                        Toast.makeText(this@ModifyProfileActivity, "닉네임 변경 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ModifyProfileActivity, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}