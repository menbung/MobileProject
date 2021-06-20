package com.example.projectmp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.projectmp.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dbHelper: DBHelper
    val arrayList = arrayListOf<String>("곱창", "짜장면", "짬뽕", "파스타", "조개구이", "조개찜", "족발",
        "보쌈", "닭발", "치킨", "피자", "떡볶이", "회", "해물찜", "해물탕", "돈까스", "햄버거"
        , "김치찌개", "된장찌개", "쌀국수", "생선구이", "카레라이스", "덮밥", "삼겹살"
        , "스테이크", "갈비", "냉면", "국밥", "닭볶음탕", "찜닭", "감자탕", "갈비찜")
    var mBackWait:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDB()
        init()
    }

    private fun initDB() {
        val dbfile = getDatabasePath("db.db")
        if(!dbfile.parentFile.exists()){
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){
            val file = resources.openRawResource(R.raw.db)
            val fileSize = file.available()
            val buffer = ByteArray(fileSize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }
    }

    fun init() {
        dbHelper = DBHelper(this)
        var random = Random().nextInt(arrayList.size)
        binding.recomMenu.text = arrayList[random]
        binding.apply {
            SearchBtn.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            WorldcupBtn.setOnClickListener {
                val intent = Intent(this@MainActivity, FoodWorldCupActivity::class.java)
                startActivity(intent)
            }
            ListBtn.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchedListActivity::class.java)
                startActivity(intent)
            }
            MypageBtn.setOnClickListener {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
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