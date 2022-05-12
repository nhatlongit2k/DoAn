package com.example.drivinglicenceuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.viewmodel.ExamViewModel
import com.example.drivinglicenceadmin.viewmodel.QuestionViewModel
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.ActivityHomeBinding
import com.example.drivinglicenceuser.databinding.ActivitySelectExamBinding
import com.example.drivinglicenceuser.model.ResultQuestion
import com.example.drivinglicenceuser.ui.adapter.AdapterExamRecycler
import com.example.drivinglicenceuser.viewmodel.ResultQuestionViewModel
import com.example.drivinglicenceuser.viewmodel.ResultTestViewModel
import com.example.mvvm_retrofit.Status

class SelectExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectExamBinding
    var examList: ArrayList<Exam> = ArrayList()
    var token = ""
    lateinit var account: User
    lateinit var type: Type
    var resultQuestions : ArrayList<ResultQuestion> = ArrayList()
    private val adapterExamRecycler: AdapterExamRecycler by lazy {
        AdapterExamRecycler(this, examList, onDoExamClick, seeAnswerClick)
    }

    private val examViewModel: ExamViewModel by lazy {
        ViewModelProvider(this, ExamViewModel.ExamViewModelFactory(application)
        ).get(ExamViewModel::class.java)
    }

    private val resultQuestionViewModel: ResultQuestionViewModel by lazy {
        ViewModelProvider(this, ResultQuestionViewModel.ResultQuestionViewModelFactory(application)
        ).get(ResultQuestionViewModel::class.java)
    }

    private val resultTestViewModel: ResultTestViewModel by lazy {
        ViewModelProvider(this, ResultTestViewModel.ResultTestViewModelFactory(application)
        ).get(ResultTestViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectExamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.getStringExtra(TOKEN).toString()
        type = intent.getSerializableExtra(KEY_PASS_TYPE) as Type
        account = intent.getSerializableExtra(ACCOUNT) as User
        binding.rvSelectExamLicence.setHasFixedSize(true)
        binding.rvSelectExamLicence.layoutManager = LinearLayoutManager(this)
        binding.rvSelectExamLicence.adapter = adapterExamRecycler

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        getExamForUser()
    }

    fun getExamForUser(){
        val id = type.id
        if (id != null) {
            examViewModel.getExamForUserWithTypeId("Bearer $token", id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.slExamLicence.isRefreshing = false
                            resource.data?.let { types ->
                                examList.clear()
                                examList.addAll(types)
                                adapterExamRecycler.setExams(examList)
                            }

                        }
                        Status.ERROR -> {
                            Log.d("TAG", "error: ${it.message}")
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            binding.slExamLicence.isRefreshing = false
                        }
                        Status.LOADING -> {
                            binding.slExamLicence.isRefreshing = true
                        }
                    }
                }
            }
        }
    }

    private val onDoExamClick: (Exam) -> Unit = {
        val intent: Intent = Intent(this, DoExamActivity::class.java)
        intent.putExtra(TOKEN, token)
        intent.putExtra(KEY_PASS_EXAM, it)
        intent.putExtra(ACCOUNT, account)
        intent.putExtra(KEY_DO_OR_RESULT, KEY_DO_EXAM)
        if(it.status!=0.toLong()){
            val id = it.idResultTest
            Log.d("TAG", "it.idResultTest: $id")
            if (id != null) {
                resultTestViewModel.deleteResultTest(id).observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                resource.data?.let { result ->
                                    Toast.makeText(this, "Làm lại bài thi ", Toast.LENGTH_SHORT).show()
//                                    startActivity(intent)
                                    startActivityForResult(intent, 1)
                                }

                            }
                            Status.ERROR -> {
                                Log.d("TAG", "error: ${it.message}")
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }
                            Status.LOADING -> {
                            }
                        }
                    }
                }
            }
        }else{
//            startActivity(intent)
            startActivityForResult(intent, 1)
        }
    }

    private val seeAnswerClick: (Exam) -> Unit = {
        val intent: Intent = Intent(this, DoExamActivity::class.java)
        intent.putExtra(TOKEN, token)
        intent.putExtra(KEY_PASS_EXAM, it)
        intent.putExtra(ACCOUNT, account)
        if(it.status!=0.toLong()){
            val id = it.idResultTest
            if (id != null) {
                resultQuestionViewModel.getResultQuestionsByResultTestId(id).observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                resource.data?.let { result ->
                                    resultQuestions.clear()
                                    resultQuestions.addAll(result)
                                    Toast.makeText(this, "Xem lại bài thi ", Toast.LENGTH_SHORT).show()
                                    intent.putExtra(RESULT_QUESTION ,resultQuestions)
                                    intent.putExtra(KEY_DO_OR_RESULT, KEY_SEE_RESULT)
                                    startActivity(intent)
                                }
                            }
                            Status.ERROR -> {
                                Log.d("TAG", "error: ${it.message}")
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }
                            Status.LOADING -> {
                            }
                        }
                    }
                }
            }
        }else{
            Toast.makeText(this, "Bạn chưa làm đề này!", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivityResult: $requestCode")
        getExamForUser()
    }
}