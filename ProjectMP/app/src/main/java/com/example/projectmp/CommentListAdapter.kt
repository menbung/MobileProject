package com.example.projectmp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmp.databinding.RowBinding

class CommentListAdapter(val context: Context, val commentList: ArrayList<Comment>?): BaseAdapter() {
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
        return commentList!!.size
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.commentitem, null)

        val listid = view.findViewById<TextView>(R.id.Listid)
        val listcom = view.findViewById<TextView>(R.id.Listcom)

        val comment = commentList?.get(position)
        if (comment != null) {
            listid.text = comment.userId
        }
        if (comment != null) {
            listcom.text = comment.comment
        }
        return view
    }
}