package com.example.projectmp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView

class DBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    companion object{
        val DB_NAME = "db.db"
        val DB_VERSION = 1
        val TABLE_NAME = "restaurant"
        val RESNAME = "resname"
        val CATEGORY = "category"
        val LOCATION = "location"
        val MENU_TABLE_NAME = "resmenu"
        val MENU = "menu"
    }

    fun getAllRecord(){
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        showRecord(cursor)
        cursor.close()
        db.close()
    }

    fun getAllMenu() : ArrayList<String> {
        val strsql = "select * from $MENU_TABLE_NAME;"
        val db = readableDatabase
        val menus = arrayListOf<String>()
        val cursor = db.rawQuery(strsql, null)
        if(cursor.count ==0)
            return menus
        cursor.moveToFirst()
        do {
            menus.add(cursor.getString(1))
        }while (cursor.moveToNext())
        cursor.close()
        db.close()
        return menus
    }

    private fun showRecord(cursor: Cursor){
        cursor.moveToFirst()
        val attrcount = cursor.columnCount
        val activitiy = context as SearchActivity
        activitiy.binding.tableLayout.removeAllViewsInLayout()
        val rowParam = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        val viewParam = TableRow.LayoutParams(0, 100, 1f)
        if(cursor.count ==0) return
        //레코드 추가하기
         do {
             val row = TableRow(activitiy)
             row.layoutParams = rowParam
             for(i in 0 until attrcount){
                 val textView = TextView(activitiy)
                 textView.layoutParams = viewParam
                 textView.text = cursor.getString(i)
                 textView.textSize = 10.0f
                 textView.setTextColor(Color.WHITE)
                 textView.gravity = Gravity.CENTER
                 row.addView(textView)
             }
             activitiy.binding.tableLayout.addView(row)
         }while(cursor.moveToNext())
    }

    fun insertRest(restaurant: Restaurant):Boolean{
        val values = ContentValues()
        values.put(RESNAME, restaurant.resName)
        values.put(CATEGORY, restaurant.category)
        values.put(LOCATION, restaurant.location)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values)>0
        db.close()
        return flag
    }

    fun insertMenu(name: String, menu: String): Boolean{
        val values = ContentValues()
        values.put(RESNAME, name)
        values.put(MENU, menu)
        val db = writableDatabase
        val flag = db.insert(MENU_TABLE_NAME, null, values)>0
        db.close()
        return flag
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val creat_table = "create table if not exists $TABLE_NAME("+
                "$RESNAME text, "+
                "$CATEGORY text, " +
                "$LOCATION text);"
        db!!.execSQL(creat_table)
        val creat_menu_table = "create table if not exists $MENU_TABLE_NAME("+
                "$RESNAME text, "+
                "$MENU text);"
        db!!.execSQL(creat_menu_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        val drop_menu_table = "drop table if exists $MENU_TABLE_NAME;"
        db!!.execSQL(drop_menu_table)
        onCreate(db)
    }

    fun findRest(str: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $RESNAME='$str';"
        val strsql2 = "select * from $TABLE_NAME where $CATEGORY='$str';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val cursor2 = db.rawQuery(strsql2, null)
        val flag = cursor.count !=0
        val flag2 = cursor2.count != 0
        showRecord(cursor)
        showRecord(cursor2)
        cursor.close()
        cursor2.close()
        db.close()
        if(flag){
            return true
        }
        if(flag2){
            return true
        }
        return false
    }
}