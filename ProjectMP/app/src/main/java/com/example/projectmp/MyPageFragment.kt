package com.example.projectmp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectmp.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    var binding: FragmentMyPageBinding?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMyPageBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = pref?.getString("id", "")
        val passwd = pref?.getString("pw", "")
        if(id==""){
            binding!!.logoutView.visibility = View.VISIBLE
            binding!!.loginView.visibility = View.GONE
        }
        else{
            binding!!.logoutView.visibility = View.GONE
            binding!!.loginView.visibility = View.VISIBLE
            binding!!.idText.text = id
            binding!!.emailText.text = "test1234@konkuk.ac.kr"
            binding!!.nicknameText.text = passwd
        }
        val editor = pref?.edit()
        binding!!.apply {
            loginBtn.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
            logoutBtn.setOnClickListener {
                loginView.visibility = View.GONE
                logoutView.visibility = View.VISIBLE
                editor?.putString("id", "")
                editor?.putString("pw", "")
                editor?.commit()
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}