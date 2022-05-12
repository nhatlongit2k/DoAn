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

class AdapterAnswerResultRecycler(
    private val context: Context,
    private var answerList: ArrayList<Answer>,
    private var resultQuestion: ResultQuestion
): RecyclerView.Adapter<AdapterAnswerResultRecycler.AnswerResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerResultViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false)
        return AnswerResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnswerResultViewHolder, position: Int) {
        val answer: Answer = answerList[position]
        holder.tvNumber.text = (position+1).toString()
        holder.tvContent.text = answer.content
//        if(answer.isCorrect){
//            holder.tvContent.setTextColor(Color.parseColor("#00FA9A"))
//        }else{
//            holder.tvContent.setTextColor(Color.BLACK)
//        }

//        holder.clAnswerItem.setOnClickListener {
//            for(i in 0 .. answerList.size-1){
//                answerList[i].isCorrect = false
//            }
//            answer.isCorrect = true
//            notifyDataSetChanged()
//        }
        holder.tvContent.setTextColor(Color.BLACK)
        if(answer.id == resultQuestion.idAnswer && !answer.isCorrect){
            holder.tvContent.setTextColor(Color.RED)
        }
        if(answer.isCorrect){
            holder.tvContent.setTextColor(Color.parseColor("#64B55F"))
        }

//        holder.clAnswerItem.setOnClickListener {
//            resultQuestion.idAnswer = answer.id
//            resultQuestion.isCorrect = answer.isCorrect
//            notifyDataSetChanged()
//        }
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    fun setAnswers(answerList: ArrayList<Answer>){
        this.answerList = answerList
        notifyDataSetChanged()
    }

    inner class AnswerResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvNumber: TextView = itemView.findViewById(R.id.tv_answer_number)
        val tvContent: TextView = itemView.findViewById(R.id.tv_answer_content)
        val clAnswerItem: ConstraintLayout = itemView.findViewById(R.id.cl_answer_item)
    }
}