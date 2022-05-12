package com.example.drivinglicenceuser.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Question
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.FragmentLoginBinding
import com.example.drivinglicenceuser.databinding.FragmentQuestionBinding
import com.example.drivinglicenceuser.model.ResultQuestion
import com.example.drivinglicenceuser.ui.adapter.AdapterAnswerLearnRecycler
import com.example.drivinglicenceuser.ui.adapter.AdapterAnswerRecycler
import com.example.drivinglicenceuser.ui.adapter.AdapterAnswerResultRecycler
import com.squareup.picasso.Picasso

class QuestionFragment : Fragment() {

    lateinit var binding: FragmentQuestionBinding
    lateinit var question: Question
    lateinit var resultQuestion: ResultQuestion
    lateinit var isForExamOrResult: String
    var answerList: ArrayList<Answer> = ArrayList()

    private val adapterAnswerRecycler: AdapterAnswerRecycler by lazy {
        AdapterAnswerRecycler(requireContext(), answerList, resultQuestion)
    }

    private val adapterAnswerResultRecycler: AdapterAnswerResultRecycler by lazy {
        AdapterAnswerResultRecycler(requireContext(), answerList, resultQuestion)
    }

    private val adapterAnswerLearnRecycler: AdapterAnswerLearnRecycler by lazy {
        AdapterAnswerLearnRecycler(requireContext(), answerList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val bundleRecevie = arguments
        if(bundleRecevie!=null){
            question = bundleRecevie.get(KEY_PASS_QUESTION) as Question
            resultQuestion = bundleRecevie.get(KEY_PASS_RESULT_QUESTION) as ResultQuestion
            isForExamOrResult = bundleRecevie.getString(KEY_DO_OR_RESULT, "")
            val answers = question.answers
            if(answers != null){
                answerList = answers
            }
            if(isForExamOrResult.equals(KEY_DO_EXAM)){
                setUpViewForDoExam()
            }else if(isForExamOrResult.equals(KEY_SEE_RESULT)){
                setUpViewForSeeResult()
            }else{
                setUpViewForLearn()
            }

        }
        return binding.root
    }

    private fun setUpViewForLearn() {
        binding.tvContentQuestionFragment.setText(question.content)
        binding.tvResonQuestionFragment.visibility = View.VISIBLE
        binding.tvResonQuestionFragment.setText(question.reason)
        binding.rvAnswerQuestionFragment.setHasFixedSize(true)
        binding.rvAnswerQuestionFragment.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAnswerQuestionFragment.adapter = adapterAnswerLearnRecycler
        if(question.image != null){
            if(!question.image.equals("")){
                binding.imgQuestionFragment.visibility = View.VISIBLE
                Picasso.get().load(question.image).into(binding.imgQuestionFragment)
            }
        }
        adapterAnswerLearnRecycler.setAnswers(answerList)
    }

    private fun setUpViewForSeeResult() {
        binding.tvContentQuestionFragment.setText(question.content)
        binding.tvResonQuestionFragment.visibility = View.VISIBLE
        binding.tvResonQuestionFragment.setText(question.reason)
        binding.tvIsCorrect.visibility = View.VISIBLE
        if(resultQuestion.isCorrect != true){
            binding.tvIsCorrect.setText("Sai")
            binding.tvIsCorrect.setTextColor(Color.RED)
        }
        binding.rvAnswerQuestionFragment.setHasFixedSize(true)
        binding.rvAnswerQuestionFragment.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAnswerQuestionFragment.adapter = adapterAnswerResultRecycler
        if(question.image != null){
            if(!question.image.equals("")){
                binding.imgQuestionFragment.visibility = View.VISIBLE
                Picasso.get().load(question.image).into(binding.imgQuestionFragment)
            }
        }
        adapterAnswerRecycler.setAnswers(answerList)
    }

    private fun setUpViewForDoExam() {
        binding.rvAnswerQuestionFragment.setHasFixedSize(true)
        binding.rvAnswerQuestionFragment.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAnswerQuestionFragment.adapter = adapterAnswerRecycler

        binding.tvContentQuestionFragment.setText(question.content)
        if(question.image != null){
            if(!question.image.equals("")){
                binding.imgQuestionFragment.visibility = View.VISIBLE
                Picasso.get().load(question.image).into(binding.imgQuestionFragment)
            }
        }
        adapterAnswerRecycler.setAnswers(answerList)
    }
}