package com.example.drivinglicenceuser.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.model.ResultQuestion

class AdapterAnswerLearnRecycler(
    private val context: Context,
    private var answerList: ArrayList<Answer>
): RecyclerView.Adapter<AdapterAnswerLearnRecycler.AnswerLearnViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerLearnViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false)
        return AnswerLearnViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnswerLearnViewHolder, position: Int) {
        val answer: Answer = answerList[position]
        holder.tvNumber.text = (position+1).toString()
        holder.tvContent.text = answer.content
        holder.tvContent.setTextColor(Color.BLACK)
        if(answer.isCorrect){
            holder.tvContent.setTextColor(Color.parseColor("#64B55F"))
        }
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    fun setAnswers(answerList: ArrayList<Answer>){
        this.answerList = answerList
        notifyDataSetChanged()
    }

    inner class AnswerLearnViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvNumber: TextView = itemView.findViewById(R.id.tv_answer_number)
        val tvContent: TextView = itemView.findViewById(R.id.tv_answer_content)
        val clAnswerItem: ConstraintLayout = itemView.findViewById(R.id.cl_answer_item)
    }
}