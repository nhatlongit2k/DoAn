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
import com.example.drivinglicenceadmin.model.Type

class AdapterExamRecycler(
    private val context: Context,
    private var examList: ArrayList<Exam>,
    private val onClick: (Exam)->Unit,
    private val onDelete: (Exam)->Unit,
    private val onEdit: (Exam)->Unit
): RecyclerView.Adapter<AdapterExamRecycler.ExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_exam, parent, false)
        return ExamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam: Exam = examList[position]
        holder.tvName.text = exam.name
        holder.tvInfor.text = exam.numberOfQuestion.toString() + " Câu/" + exam.time.toString() + " Phút"

        holder.clExamItem.setOnClickListener {
            onClick(exam)
        }

        holder.btDelete.setOnClickListener {
            onDelete(exam)
        }

        holder.btEdit.setOnClickListener {
            onEdit(exam)
        }
    }

    override fun getItemCount(): Int {
        return examList.size
    }

    fun setExams(examList: ArrayList<Exam>){
        this.examList = examList
        notifyDataSetChanged()
    }

    inner class ExamViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.tv_exam_item_name)
        val tvInfor: TextView = itemView.findViewById(R.id.tv_exam_item_infor)
        val btDelete: ImageView = itemView.findViewById(R.id.img_item_exam_delete)
        val btEdit: ImageView = itemView.findViewById(R.id.img_item_exam_edit)
        val clExamItem: ConstraintLayout = itemView.findViewById(R.id.cl_exam_item)
    }
}