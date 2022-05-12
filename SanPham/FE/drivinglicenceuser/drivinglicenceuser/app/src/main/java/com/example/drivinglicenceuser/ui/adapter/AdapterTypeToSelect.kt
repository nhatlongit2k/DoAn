package com.example.drivinglicenceuser.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceuser.R

class AdapterTypeToSelect(
    private val context: Context,
    private var typeList: ArrayList<Type>,
    private val onClick: (Type)->Unit
): RecyclerView.Adapter<AdapterTypeToSelect.TypeToSelectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeToSelectViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_type_to_select, parent, false)
        return TypeToSelectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TypeToSelectViewHolder, position: Int) {
        val type: Type = typeList[position]
        holder.tvTitle.text = type.name
        holder.tvDes.text = type.des

        holder.clTypeItem.setOnClickListener {
            onClick(type)
        }
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    fun setTypes(typeList: ArrayList<Type>){
        this.typeList = typeList
        notifyDataSetChanged()
    }

    inner class TypeToSelectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.tv_type_slect_item_name)
        val tvDes: TextView = itemView.findViewById(R.id.tv_type_select_item_des)
        val clTypeItem: ConstraintLayout = itemView.findViewById(R.id.cl_type_select_item)
    }
}