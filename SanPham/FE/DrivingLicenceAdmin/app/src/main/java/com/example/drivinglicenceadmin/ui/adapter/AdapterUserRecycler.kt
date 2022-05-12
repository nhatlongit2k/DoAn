package com.example.drivinglicenceadmin.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.drivinglicenceadmin.R
import com.example.drivinglicenceadmin.model.User
import com.squareup.picasso.Picasso


class AdapterUserRecycler(
    private val context: Context,
    private val account: User,
    private var userList: ArrayList<User>,
    private val onClick: (User)->Unit,
    private val onSwitchClick: (User)->Unit
): RecyclerView.Adapter<AdapterUserRecycler.UserViewHolder>() {

    var isTouched = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User = userList[position]
        holder.tvName.text = "${user.firstName} ${user.lastName}"
        holder.tvEmail.text = user.email
        if(!user.imageUrl.equals("")){
            Picasso.get().load(user.imageUrl).into(holder.imgAvatar)
        }
        holder.scAuthenUser.isChecked = user.activated
        if(user.authorities.contains("ROLE_ADMIN")){
            holder.tvAuthen.text = "ADMIN"
        }else{
            holder.tvAuthen.text = "USER"
        }

//        holder.scAuthenUser.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            user.activated = isChecked
//            Log.d("TAG", "onBindViewHolder: ${user.activated}")
//            onSwitchClick(user)
//        })
//
//        holder.scAuthenUser.setOnTouchListener(OnTouchListener { view, motionEvent ->
////            user.activated = holder.scAuthenUser.isActivated
////            Log.d("TAG", "onBindViewHolder: ${user.activated}")
//            user.activated = !user.activated
//            Log.d("TAG", "onBindViewHolder2: ${user.activated}")
//            onSwitchClick(user)
//            false
//        })

        if(user.id == account.id){
            holder.scAuthenUser.visibility = View.GONE
        }else{
            holder.scAuthenUser.visibility = View.VISIBLE
            holder.scAuthenUser.setOnTouchListener(OnTouchListener { view, motionEvent ->
                isTouched = true
                false
            })

            holder.scAuthenUser.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isTouched) {
                    isTouched = false
                    user.activated = isChecked
                    onSwitchClick(user)
                }
            })
        }

        holder.clUserItem.setOnClickListener {
            onClick(user)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUsers(userList: ArrayList<User>){
        this.userList = userList
        notifyDataSetChanged()
    }

    fun getUsers(): ArrayList<User>{
        return this.userList
    }

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.tv_user_name)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_user_email)
        val tvAuthen: TextView = itemView.findViewById(R.id.tv_authen_user)
        val imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar_user)
        val clUserItem: ConstraintLayout = itemView.findViewById(R.id.cl_user_item)
        val scAuthenUser: SwitchCompat = itemView.findViewById(R.id.sc_active_user)
    }
}