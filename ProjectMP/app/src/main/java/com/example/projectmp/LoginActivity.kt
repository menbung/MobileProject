package com.example.projectmp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var pref: SharedPreferences
    lateinit var db: DatabaseReference
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val bar = supportActionBar
        bar!!.hide()
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("Users")
        pref = getSharedPreferences("login", Context.MODE_PRIVATE)
        binding.apply {
            signIn.setOnClickListener {
                if (idEdit.text.isBlank() || passwdEdit.text.isBlank()) {
                    Toast.makeText(this@LoginActivity, "모두 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else {
                    val query = db.orderByChild("id").equalTo(idEdit.text.toString())
                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.getValue(User::class.java) != null) {
                                showLoadingDialog()
                                if (snapshot != null) {
                                    for (userSnapshot in snapshot.children) {
                                        val userInfo = userSnapshot.getValue(User::class.java)
                                        loginUserId(
                                            userInfo!!.email,
                                            passwdEdit.text.toString(),
                                            userInfo.nickname
                                        )
                                    }
                                }
                            }
                            else
                                Toast.makeText(this@LoginActivity, "아이디가 존재하지 않습니다", Toast.LENGTH_SHORT).show()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@LoginActivity, "Find Data error", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
            signUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            closeBtn.setOnClickListener {
                finish()
            }
        }
    }

    private fun loginUserId(email: String, passwd: String, nickname: String) {
        auth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "환영합니다 " + nickname + "님!", Toast.LENGTH_SHORT).show()
                val editor = pref.edit()
                editor.putString("id", binding.idEdit.text.toString())
                editor.putString("nickname", nickname)
                editor.commit()
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoadingDialog() {
        val dialog = LoadingDialog(this)
        CoroutineScope(Main).launch {
            dialog.show()
            delay(2000)
            dialog.dismiss()
        }
    }
}