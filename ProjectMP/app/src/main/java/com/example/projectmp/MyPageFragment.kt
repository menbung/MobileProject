package com.example.projectmp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projectmp.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyPageFragment : Fragment() {
    var binding: FragmentMyPageBinding?=null
    var auth: FirebaseAuth?=null
    var db: DatabaseReference?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("Users")
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = pref?.getString("id", "")
        val nickName = pref?.getString("nickname", "")
        val user = auth!!.currentUser
        if (user != null) {
            initView(user.email!!)
            binding!!.logoutView.visibility = View.GONE
            binding!!.loginView.visibility = View.VISIBLE
            binding!!.idText.text = id
            binding!!.emailText.text = user.email
            binding!!.nicknameText.text = nickName
        }
        else {
            binding!!.logoutView.visibility = View.VISIBLE
            binding!!.loginView.visibility = View.GONE
        }
        binding!!.apply {
            loginBtn.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
            logoutBtn.setOnClickListener {
                auth?.signOut()
                binding!!.logoutView.visibility = View.VISIBLE
                binding!!.loginView.visibility = View.GONE
            }
            nicknameBtn.setOnClickListener {
                val intent = Intent(activity, ModifyProfileActivity::class.java)
                intent.putExtra("num", 1)
                startActivity(intent)
            }
            emailBtn.setOnClickListener {
                val intent = Intent(activity, ModifyProfileActivity::class.java)
                intent.putExtra("num", 2)
                startActivity(intent)
            }
            passwdBtn.setOnClickListener {
                val intent = Intent(activity, ModifyProfileActivity::class.java)
                intent.putExtra("num", 3)
                startActivity(intent)
            }
            withdrawalBtn.setOnClickListener {
                val intent = Intent(activity, ModifyProfileActivity::class.java)
                intent.putExtra("num", 4)
                startActivity(intent)
            }
            emailAuthBtn.setOnClickListener {
                val intent = Intent(activity, EmailAuthActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initView(email: String) {
        val query = db!!.orderByChild("email")!!.equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null) {
                    for (userSnapshot in snapshot.children) {
                        val userInfo = userSnapshot.getValue(User::class.java)
                        binding!!.idText.text = userInfo?.id
                        binding!!.emailText.text = userInfo?.email
                        binding!!.nicknameText.text = userInfo?.nickname
                    }
                }
                else
                    Toast.makeText(context, "자동 로그인 에러", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Find Data error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        auth = null
        db = null
    }
}