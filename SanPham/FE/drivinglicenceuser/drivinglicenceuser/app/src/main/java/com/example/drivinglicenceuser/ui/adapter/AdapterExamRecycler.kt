package com.example.drivinglicenceuser.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceuser.R

class AdapterExamRecycler(
    private val context: Context,
    private var examList: ArrayList<Exam>,
    private val onDoExamClick: (Exam)->Unit,
    private val seeAnswerClick: (Exam)->Unit
): RecyclerView.Adapter<AdapterExamRecycler.ExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_exam, parent, false)
        return ExamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam: Exam = examList[position]
        holder.tvName.text = exam.name
        var time: String = ""
        Log.d("TAG", "onBindViewHolder: ${exam.time}")
        if(exam.time.toString().equals("0")){
            time = "Không giới hạn"
        }else{
            time = "${exam.time.toString()} Phút"
        }
        holder.tvInfor.text = exam.numberOfQuestion.toString() + " Câu/" + time

        if(exam.status != 0.toLong()){
            if(exam.pass){
//                holder.tvDoExam.setBackgroundColor(Color.YELLOW)
                holder.tvDoExam.setBackgroundResource(R.drawable.background_do_again_when_pass)
                holder.tvDoExam.setText("LÀM LẠI")
            }else{
//                holder.tvDoExam.setBackgroundColor(Color.RED)
                holder.tvDoExam.setBackgroundResource(R.drawable.background_do_agian_when_fail)
                holder.tvDoExam.setText("LÀM LẠI")
            }
        }else{
            holder.tvDoExam.setBackgroundResource(R.drawable.background_aqua)
            holder.tvDoExam.setText("LÀM BÀI")
        }
//        holder.clExamItem.setOnClickListener {
//            onClick(exam)
//        }
        holder.tvDoExam.setOnClickListener {
            onDoExamClick(exam)
        }

        holder.clExamItem.setOnClickListener {
            seeAnswerClick(exam)
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
        val imgIcon: ImageView = itemView.findViewById(R.id.img_exam_chose)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_exam_name)
        val tvInfor: TextView = itemView.findViewById(R.id.tv_item_exam_infor)
        val tvDoExam: TextView = itemView.findViewById(R.id.tv_item_do_exam)
        val clExamItem: ConstraintLayout = itemView.findViewById(R.id.cl_exam_item)
    }
}