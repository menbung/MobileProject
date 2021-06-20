package com.example.projectmp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.projectmp.databinding.ActivityFoodWorldCupBinding
import java.util.*
import kotlin.collections.ArrayList

class FoodWorldCupActivity : AppCompatActivity() {
    lateinit var binding: ActivityFoodWorldCupBinding
    lateinit var dbHelper: DBHelper
    var menues= ArrayList<String>()
    var round=16 //16강
    var count=0
    var roundSet1=ArrayList<String>()
    var roundSet2=ArrayList<String>()
    var flag=false
    var mBackWait:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodWorldCupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        nextRound()
        ininData()
        setData()
    }

    private fun init() {
        dbHelper = DBHelper(this)
        menues = dbHelper.getAllMenu()
        binding.apply {
            btn1.setOnClickListener {
                if(flag){
                    roundSet2.add(btn1.text.toString())
                }
                else {
                    roundSet1.add(btn1.text.toString())
                }
                if (round==1) {
                    roundText.text = "우승"
                    btn2.text = roundSet1.get(0)
                    btn1.text = "1등 메뉴는!"
                }
                else {
                    setData()
                }
            }
            btn2.setOnClickListener {
                if(flag){
                    roundSet2.add(btn2.text.toString())
                }
                else{
                    roundSet1.add(btn2.text.toString())
                }
                if (round==1) {
                    roundText.text = "우승"
                    btn1.text = "1등 메뉴는!"
                }
                else {
                    setData()
                }
            }
            HomeBtn.setOnClickListener {
                val intent = Intent(this@FoodWorldCupActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            ListBtn.setOnClickListener {
                val intent = Intent(this@FoodWorldCupActivity, SearchedListActivity::class.java)
                startActivity(intent)
            }
            MypageBtn.setOnClickListener {
                val intent = Intent(this@FoodWorldCupActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun ininData() {
        var i=0
        val random= Random()
        while(i<round*2){
            var rnum=random.nextInt(menues.size)
            if(menues.get(rnum) in roundSet1){
                i--
            }
            else{
                roundSet1.add(menues.get(rnum))
            }
            i++
        }
    }

    private fun setData() {
        count++
        if (count > round) {
            nextRound()
            count++
        }
        if(round==1){
            binding.roundText.setText("결승")
        }
        else {
            binding.roundText.setText((round * 2).toString() + "강  " + count.toString() + "/" + round.toString())
        }
        val random = Random()
        if (flag) {
            var randomnum = random.nextInt(roundSet1.size)
            var menu1 = roundSet1.get(randomnum)
            roundSet1.removeAt(randomnum)
            binding.btn1.setText(menu1)
            randomnum = random.nextInt(roundSet1.size)
            var menu2 = roundSet1.get(randomnum)
            roundSet1.removeAt(randomnum)
            binding.btn2.setText(menu2)
        } else {
            var randomnum = random.nextInt(roundSet2.size)
            var menu1 = roundSet2.get(randomnum)
            roundSet2.removeAt(randomnum)
            binding.btn1.setText(menu1)
            randomnum = random.nextInt(roundSet2.size)
            var menu2 = roundSet2.get(randomnum)
            roundSet2.removeAt(randomnum)
            binding.btn2.setText(menu2)
        }
    }

    private fun nextRound(){
        flag = !flag
        count = 0
        if (flag) {
            roundSet2.clear()
        }
        else {
            roundSet1.clear()
        }
        round=round/2
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            System.exit(0)
        }
    }
}