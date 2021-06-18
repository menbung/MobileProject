package com.example.projectmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var db: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        val bar = supportActionBar
        bar!!.hide()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("Users")
        pref = getSharedPreferences("login", Context.MODE_PRIVATE)
        binding.apply {
            closeBtn.setOnClickListener {
                finish()
            }
            signUpBtn.setOnClickListener {
                if (idEdit.text.isBlank() || emailEdit.text.isBlank() || passWdEdit.text.isBlank() || nicknameEdit.text.isBlank()) {
                    Toast.makeText(this@SignUpActivity, "모두 입력 해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    if (passWdEdit.text.toString().equals(passWdAgainEdit.text.toString())) {
                        val query = db.orderByChild("id").equalTo(idEdit.text.toString())
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.getValue(User::class.java) != null)
                                    Toast.makeText(this@SignUpActivity, "계정이 이미 존재 합니다", Toast.LENGTH_SHORT).show()
                                else
                                    createUser(emailEdit.text.toString(), passWdEdit.text.toString())
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@SignUpActivity, "Find Data error", Toast.LENGTH_SHORT).show()
                            }

                        })
                    } else {
                        Toast.makeText(this@SignUpActivity, "비밀번호 불일치", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun createUser(email: String, passwd: String) {
        auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                updateDB()
                val editor = pref.edit()
                editor.putString("id", binding.idEdit.text.toString())
                editor.putString("nickname", binding.nicknameEdit.text.toString())
                editor.commit()
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDB() {
        val user = User(binding.idEdit.text.toString(), "404", binding.nicknameEdit.text.toString(),
            binding.emailEdit.text.toString(), false)
        db.child(binding.idEdit.text.toString()).setValue(user)
    }
}