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
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceadmin.model.Question

class AdapterQuestionRecycler(
    private val context: Context,
    private var questionList: ArrayList<Question>,
    private val onClick: (Question)->Unit,
    private val onDelete: (Question)->Unit
): RecyclerView.Adapter<AdapterQuestionRecycler.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question: Question = questionList[position]
        holder.tvQuestionNumber.text = "Câu " + (position+1).toString()
        holder.tvQuestionContent.text = question.content
        if(question.isFallQuestion){
            holder.tvQuestionIsFallQuestion.visibility = View.VISIBLE
        }else{
            holder.tvQuestionIsFallQuestion.visibility = View.GONE
        }

        holder.clQuestionItem.setOnClickListener {
            onClick(question)
        }

        holder.btDelete.setOnClickListener {
            onDelete(question)
        }
    }

    fun setQuestions(questionList: ArrayList<Question>){
        this.questionList = questionList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    inner class QuestionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvQuestionNumber: TextView = itemView.findViewById(R.id.tv_question_number)
        val tvQuestionContent: TextView = itemView.findViewById(R.id.tv_question_item_content)
        val tvQuestionIsFallQuestion: TextView = itemView.findViewById(R.id.tv_is_fall_question)
        val btDelete: ImageView = itemView.findViewById(R.id.img_question_item_delete)
        val clQuestionItem: ConstraintLayout = itemView.findViewById(R.id.cl_question_item)
    }
}