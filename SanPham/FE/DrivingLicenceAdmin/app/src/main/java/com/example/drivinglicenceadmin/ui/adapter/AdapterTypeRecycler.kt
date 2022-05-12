package com.example.drivinglicenceadmin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.drivinglicenceadmin.R
import com.example.drivinglicenceadmin.model.Type

class AdapterTypeRecycler(
    private val context:Context,
    private var typeList: ArrayList<Type>,
    private val onClick: (Type)->Unit,
    private val onDelete: (Type)->Unit,
    private val onEdit: (Type)->Unit
): RecyclerView.Adapter<AdapterTypeRecycler.TypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_type_category, parent, false)
        return TypeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type: Type = typeList[position]
        holder.tvTitle.text = type.name
        holder.tvDes.text = type.des

        holder.clTypeItem.setOnClickListener {
            onClick(type)
        }

        holder.btDelete.setOnClickListener {
            onDelete(type)
        }

        holder.btEdit.setOnClickListener {
            onEdit(type)
        }
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    fun setTypes(typeList: ArrayList<Type>){
        this.typeList = typeList
        notifyDataSetChanged()
    }

    inner class TypeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.tv_type_item_name)
        val tvDes: TextView = itemView.findViewById(R.id.tv_type_item_des)
        val btDelete: ImageView = itemView.findViewById(R.id.img_type_item_delete)
        val btEdit: ImageView = itemView.findViewById(R.id.img_type_item_edit)
        val clTypeItem: ConstraintLayout = itemView.findViewById(R.id.cl_type_item)
    }
}