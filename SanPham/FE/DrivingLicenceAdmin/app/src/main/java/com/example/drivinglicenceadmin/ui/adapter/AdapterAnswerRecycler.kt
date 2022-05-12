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
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Exam

class AdapterAnswerRecycler(
    private val context: Context,
    private var answerList: ArrayList<Answer>,
    private val onClick: (Answer)->Unit,
    private val onDelete: (Answer)->Unit,
): RecyclerView.Adapter<AdapterAnswerRecycler.AnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false)
        return AnswerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer: Answer = answerList[position]
        holder.tvNumber.text = (position+1).toString()
        holder.tvContent.text = answer.content
        if(answer.isCorrect){
            holder.imgIsCorrect.visibility = View.VISIBLE
        }else{
            holder.imgIsCorrect.visibility = View.GONE
        }

//        holder.clAnswerItem.setOnClickListener {
//            onClick(answer)
//        }

        holder.btDelete.setOnClickListener {
            onDelete(answer)
        }

        holder.clAnswerItem.setOnClickListener {
            for(i in 0 .. answerList.size-1){
                answerList[i].isCorrect = false
            }
            answer.isCorrect = true
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    fun setAnswers(answerList: ArrayList<Answer>){
        this.answerList = answerList
        notifyDataSetChanged()
    }

    inner class AnswerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvNumber: TextView = itemView.findViewById(R.id.tv_answer_number)
        val tvContent: TextView = itemView.findViewById(R.id.tv_answer_content)
        val imgIsCorrect: ImageView = itemView.findViewById(R.id.img_correct_answer)
        val btDelete: ImageView = itemView.findViewById(R.id.img_delete_answer)
        val clAnswerItem: ConstraintLayout = itemView.findViewById(R.id.cl_answer_item)
    }
}