package com.example.projectmp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmp.databinding.RowBinding

class MenuListAdapter(val context: Context, val menuList: ArrayList<Menu>): BaseAdapter() {
    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }
    var itemclickListener: MyResAdapter.OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener {
                itemclickListener!!.OnItemClick(it, adapterPosition)
            }
        }
    }

    override fun getCount(): Int {
        return menuList.size
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.menuitem, null)

        val listmenu = view.findViewById<TextView>(R.id.Listmenu)
        val listcate = view.findViewById<TextView>(R.id.Listcate)
        val listprice = view.findViewById<TextView>(R.id.Listprice)

        val menu = menuList?.get(position)
        if (menu != null) {
            listmenu.text = menu.menuName
        }
        if (menu != null) {
            listcate.text = menu.category
        }
        if (menu != null) {
            listprice.text = menu.price.toString()
        }
        return view
    }
}