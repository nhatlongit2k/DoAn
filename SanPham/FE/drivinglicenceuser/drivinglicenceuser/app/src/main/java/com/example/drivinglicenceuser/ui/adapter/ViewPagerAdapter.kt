package com.example.drivinglicenceuser.ui.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.drivinglicenceadmin.KEY_DO_OR_RESULT
import com.example.drivinglicenceadmin.KEY_PASS_QUESTION
import com.example.drivinglicenceadmin.KEY_PASS_RESULT_QUESTION
import com.example.drivinglicenceadmin.model.Question
import com.example.drivinglicenceuser.model.ResultQuestion
import com.example.drivinglicenceuser.ui.activity.DoExamActivity
import com.example.drivinglicenceuser.ui.activity.HomeActivity
import com.example.drivinglicenceuser.ui.fragment.QuestionFragment

class ViewPagerAdapter(val fm: FragmentManager, var behavior: Int, val questionList: List<Question>, val resultQuestionList: List<ResultQuestion>, val isForExamOrResult: String) : FragmentStatePagerAdapter(fm, behavior) {
    override fun getCount(): Int {
        return questionList.size
    }

    override fun getItem(position: Int): Fragment {
        val questionFragment: QuestionFragment = QuestionFragment()
        val bundle = Bundle()
        bundle.putSerializable(KEY_PASS_QUESTION, questionList[position])
        resultQuestionList[position].questionId = questionList[position].id
        bundle.putSerializable(KEY_PASS_RESULT_QUESTION, resultQuestionList[position])
        bundle.putString(KEY_DO_OR_RESULT, isForExamOrResult)
        questionFragment.arguments = bundle
        return questionFragment
    }
}