package com.example.projectmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmp.databinding.RowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MyResAdapter(options: FirebaseRecyclerOptions<Restaurant>):
    FirebaseRecyclerAdapter<Restaurant, MyResAdapter.ViewHolder>(options) {
    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }
    var itemclickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener {
                itemclickListener!!.OnItemClick(it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyResAdapter.ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyResAdapter.ViewHolder,
        position: Int,
        model: Restaurant
    ) {
        holder.binding.apply{
            Rname.text = model.resName
            rescate.text = "#" + model.category
            Rmenu.text = model.menus[0].menuName
        }
    }
}