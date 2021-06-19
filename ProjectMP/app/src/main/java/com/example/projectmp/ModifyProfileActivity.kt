package com.example.projectmp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivityModifyProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ModifyProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityModifyProfileBinding
    lateinit var pref: SharedPreferences
    lateinit var db: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
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
        user = auth.currentUser!!
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
                        if (user!!.email.equals(emailEdit.text.toString())) {
                            Toast.makeText(this@ModifyProfileActivity, "변경된 값이 없습니다", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            updateUser(2)
                        }
                    }
                    3 -> {
                        if (newPasswdEdit.text.isBlank() || newPasswdAgainEdit.text.isBlank()) {
                            Toast.makeText(this@ModifyProfileActivity, "변경된 값이 없습니다", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            updateUser(3)
                        }
                    }
                    4 -> {
                        val builder = AlertDialog.Builder(this@ModifyProfileActivity)
                        builder.setTitle("회원탈퇴")
                        builder.setMessage("정말 회원탈퇴를 진행하시겠습니까?")
                        var listener = DialogInterface.OnClickListener { _, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    updateUser(4)
                                }
                            }
                        }
                        builder.setPositiveButton("탈퇴", listener)
                        builder.setNegativeButton("취소", listener)
                        builder.show()
                    }
                    else -> {
                        if (nickName.equals(nicknameEdit.text.toString())) {
                            Toast.makeText(this@ModifyProfileActivity, "변경된 값이 없습니다", Toast.LENGTH_SHORT).show()
                        }
                        else {
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

    private fun updateUser(num: Int) {
        if (binding.authPasswd.text.isBlank()){
            Toast.makeText(this@ModifyProfileActivity, "비밀번호 미입력", Toast.LENGTH_SHORT).show()
        }
        else {
            val credential = EmailAuthProvider.getCredential(user.email!!, binding.authPasswd.text.toString())
            user.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    when(num) {
                        2 -> {
                            updateEmail()
                        }
                        3 -> {
                            updatePasswd()
                        }
                        4 -> {
                            deleteUser()
                        }
                    }
                }
            }
        }
    }

    private fun deleteUser() {
        val id = pref.getString("id", "")
        user.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                db.child(id!!).removeValue()
                Toast.makeText(this@ModifyProfileActivity, "회원탈퇴 완료", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ModifyProfileActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }

    private fun updatePasswd() {
        val newPasswd = binding.newPasswdEdit.text.toString()
        if (newPasswd.equals(binding.newPasswdAgainEdit.text.toString())) {
            user.updatePassword(newPasswd)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ModifyProfileActivity, "비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
        } else {
            Toast.makeText(this@ModifyProfileActivity, "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateEmail() {
        val email = binding.emailEdit.text.toString()
        val id = pref.getString("id", "")
        user.updateEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                db.child(id!!).child("email").setValue(email)
                Toast.makeText(this@ModifyProfileActivity, "이메일 변경 완료", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ModifyProfileActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                Toast.makeText(this@ModifyProfileActivity, "잘못된 입력입니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}