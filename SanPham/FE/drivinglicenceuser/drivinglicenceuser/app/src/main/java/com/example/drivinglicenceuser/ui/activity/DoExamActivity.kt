package com.example.drivinglicenceuser.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceadmin.model.Question
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.viewmodel.QuestionViewModel
import com.example.drivinglicenceuser.databinding.ActivityDoExamBinding
import com.example.drivinglicenceuser.model.ResultQuestion
import com.example.drivinglicenceuser.model.ResultTest
import com.example.drivinglicenceuser.ui.adapter.ViewPagerAdapter
import com.example.drivinglicenceuser.viewmodel.ResultTestViewModel
import com.example.mvvm_retrofit.Status
import kotlinx.coroutines.*
import java.util.*


class DoExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoExamBinding

    var token: String = ""
    lateinit var account: User
    lateinit var exam: Exam
    lateinit var resultTest: ResultTest
    var doOrSeeResult = ""
    var resultQuestions: ArrayList<ResultQuestion> = ArrayList()
    var questionList: ArrayList<Question> = ArrayList()

    var time: Long = 0
    internal lateinit var countDownTimer: CountDownTimer
    var job: Job? = null

    private val questionViewModel: QuestionViewModel by lazy {
        ViewModelProvider(
            this, QuestionViewModel.QuestionViewModelFactory(application)
        ).get(QuestionViewModel::class.java)
    }

    private val resultTestViewModel: ResultTestViewModel by lazy {
        ViewModelProvider(
            this, ResultTestViewModel.ResultTestViewModelFactory(application)
        ).get(ResultTestViewModel::class.java)
    }
    lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = intent.getStringExtra(TOKEN).toString()
        exam = intent.getSerializableExtra(KEY_PASS_EXAM) as Exam
        account = intent.getSerializableExtra(ACCOUNT) as User
        doOrSeeResult = intent.getStringExtra(KEY_DO_OR_RESULT).toString()
        binding = ActivityDoExamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (exam.typeId == TYPE_ANOTHER) {
            loadQuestionLearn()
            binding.tvTime.visibility = View.GONE
            binding.tvEndExam.visibility = View.GONE
        } else if (doOrSeeResult.equals(KEY_DO_EXAM)) {
            loadQuestionWithExamId()
            binding.tvEndExam.setOnClickListener {
                getResultOfExam()
                endExam()
            }
            setTimeCouting()
        } else if (doOrSeeResult.equals(KEY_SEE_RESULT)) {
            resultQuestions =
                intent.getSerializableExtra(RESULT_QUESTION) as ArrayList<ResultQuestion>
            loadQuestionForSeeResult()
            Log.d("TAG", "onCreate: ${resultQuestions.size}")
            binding.tvEndExam.visibility = View.GONE
            binding.tvTime.visibility = View.GONE
        }

        binding.vpQuestion.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
//                binding.tvQuestionCurrent.setText("${position + 1}/${questionList.size}")
                binding.tvQuestionCurrent.setText("Câu(${position + 1}/${questionList.size})")
                binding.tvQuestionNumber.setText("${position + 1}/${questionList.size}")
                if (position == 0) {
                    binding.tvBackQuestion.visibility = View.INVISIBLE
                    binding.tvNextQuestion.visibility = View.VISIBLE
                } else if (position == questionList.size - 1) {
                    binding.tvBackQuestion.visibility = View.VISIBLE
                    binding.tvNextQuestion.visibility = View.INVISIBLE
                } else {
                    binding.tvBackQuestion.visibility = View.VISIBLE
                    binding.tvNextQuestion.visibility = View.VISIBLE
                }
            }
        })

        binding.tvBackQuestion.setOnClickListener {
            binding.vpQuestion.setCurrentItem(binding.vpQuestion.currentItem - 1)
        }
        binding.tvNextQuestion.setOnClickListener {
            binding.vpQuestion.setCurrentItem(binding.vpQuestion.currentItem + 1)
        }

        binding.imgBack.setOnClickListener {
            if (doOrSeeResult.equals(KEY_DO_EXAM)) {
                getResultOfExam()
                showAreYouSureDialog()
            } else {
                finish()
            }
        }
    }

    private fun showAreYouSureDialog() {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        resultTestViewModel.saveAllResult(resultTest).observe(this) {
                            it?.let { resource ->
                                when (resource.status) {
                                    Status.SUCCESS -> {
                                        resource.data?.let { result ->
                                            Toast.makeText(
                                                this,
                                                "Nộp bài thành công ",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
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
                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Bạn có chắc muốn thoát? Nếu thoát  kết quả sẽ được tính kể cả khi bạn chưa làm xong đề!")
            .setPositiveButton("Thoát", dialogClickListener)
            .setNegativeButton("Hủy", dialogClickListener).show()
    }

    private fun setTimeCouting() {
        time = exam.time * 60 * 1000
        val coutnDownInterval: Long = 1000
        countDownTimer = object : CountDownTimer(time, coutnDownInterval) {
            override fun onTick(p0: Long) {
                val minuteLeft: Long = p0 / 1000 / 60
                val secondLeft: Long = (p0 / 1000) % 60
                if (secondLeft >= 10) {
                    binding.tvTime.setText("$minuteLeft : $secondLeft")
                } else {
                    binding.tvTime.setText("$minuteLeft : 0$secondLeft")
                }
            }

            override fun onFinish() {
                getResultOfExam()
                endTimeExam()
            }
        }
        countDownTimer.start()
    }

    private fun loadQuestionForSeeResult() {
        val id = exam.id
        if (id != null) {
            questionViewModel.getAllQuestionWithAnswerInExam(id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { questions ->
                                questionList.clear()
                                questionList.addAll(questions)
                                viewPagerAdapter = ViewPagerAdapter(
                                    supportFragmentManager,
                                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                    questionList,
                                    resultQuestions,
                                    KEY_SEE_RESULT
                                )
                                binding.vpQuestion.adapter = viewPagerAdapter
                                binding.tvQuestionCurrent.setText("Câu(1/${questionList.size})")
                                binding.tvQuestionNumber.setText("1/${questionList.size}")
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
    }

    //day
    private fun loadQuestionLearn() {
        val id = exam.id
        if (id != null) {
            questionViewModel.getAllQuestionWithAnswerInExam(id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { questions ->
                                questionList.clear()
                                questionList.addAll(questions)
                                setUpResultQuestion()
                                viewPagerAdapter = ViewPagerAdapter(
                                    supportFragmentManager,
                                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                    questionList,
                                    resultQuestions,
                                    KEY_LEARN
                                )
                                binding.vpQuestion.adapter = viewPagerAdapter
                                Log.d("TAG", "loadQuestionLearn: ${questionList.size}")
                                binding.tvQuestionCurrent.setText("Câu(1/${questionList.size})")
                                binding.tvQuestionNumber.setText("1/${questionList.size}")
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
    }

    private fun getResultOfExam() {
        var numberCorrectQuestion: Long = 0
        var score: Float = 0F
        var isPass: Boolean = true
        for (i in 0 until resultQuestions.size) {
            Log.d(
                "TAG",
                "resultQuestions[$i]: ${resultQuestions[i].isCorrect},  id answer: ${resultQuestions[i].idAnswer}"
            )
            if (resultQuestions[i].isCorrect == true) {
                numberCorrectQuestion++
            } else {
                if (questionList[i].isFallQuestion) {
                    isPass = false
                }
            }
            score = (numberCorrectQuestion / exam.numberOfQuestion * 10).toFloat()

        }
        if (isPass) {
            if (numberCorrectQuestion < 21) {
                isPass = false
            }
        }
        resultTest = ResultTest(
            null,
            score,
            numberCorrectQuestion,
            isPass,
            account.id,
            exam.id,
            resultQuestions
        )
    }

    private fun endExam() {
//        val resultQuestions: ArrayList<ResultQuestion> = ArrayList()
//        for(i in 0 until questionList.size){
//            val resultQuestion = ResultQuestion(null, null, false, null, questionList[i].id)
//            for(j in 0 until questionList[i].answers!!.size){
////                questionList[i].answers!![j].isCorrect = false
//                if(questionList[i].answers?.get(j)?.isCorrect == true){
//                    resultQuestion.idAnswer = questionList[i].answers?.get(j)?.id
//                }
//            }
//            resultQuestions.add(resultQuestion)
//        }
//        resultTest = ResultTest(null, null, null, )

        resultTestViewModel.saveAllResult(resultTest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { result ->
                            Toast.makeText(this, "Nộp bài thành công ", Toast.LENGTH_SHORT).show()
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
        countDownTimer.cancel()
        this.showResultExamDialog(resultTest, onSeeResultClick, onOkClick)
    }

    private fun endTimeExam() {
//        val resultQuestions: ArrayList<ResultQuestion> = ArrayList()
//        for(i in 0 until questionList.size){
//            val resultQuestion = ResultQuestion(null, null, false, null, questionList[i].id)
//            for(j in 0 until questionList[i].answers!!.size){
////                questionList[i].answers!![j].isCorrect = false
//                if(questionList[i].answers?.get(j)?.isCorrect == true){
//                    resultQuestion.idAnswer = questionList[i].answers?.get(j)?.id
//                }
//            }
//            resultQuestions.add(resultQuestion)
//        }
//        resultTest = ResultTest(null, null, null, )

        resultTestViewModel.saveAllResult(resultTest).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { result ->
                            Toast.makeText(this, "Nộp bài thành công ", Toast.LENGTH_SHORT).show()
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
        this.showResultExamDialog(resultTest, onSeeResultClick, onOkClick)
    }

    private val onSeeResultClick: () -> Unit = {
        viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            questionList,
            resultQuestions,
            KEY_SEE_RESULT
        )
        binding.vpQuestion.adapter = viewPagerAdapter
        binding.tvEndExam.visibility = View.GONE
        binding.tvTime.visibility = View.GONE
        binding.tvQuestionCurrent.setText("1/${questionList.size}")
        doOrSeeResult = KEY_SEE_RESULT
    }

    private val onOkClick: () -> Unit = {
        finish()
    }

    private fun loadQuestionWithExamId() {
        val id = exam.id
        if (id != null) {
            questionViewModel.getAllQuestionWithAnswerInExam(id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { questions ->
                                questionList.clear()
                                questionList.addAll(questions)
//                                setUplistquestion()
                                setUpResultQuestion()
                                viewPagerAdapter = ViewPagerAdapter(
                                    supportFragmentManager,
                                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                    questionList,
                                    resultQuestions,
                                    KEY_DO_EXAM
                                )
                                binding.vpQuestion.adapter = viewPagerAdapter
                                binding.tvQuestionCurrent.setText("Câu(1/${questionList.size})")
                                binding.tvQuestionNumber.setText("1/${questionList.size}")
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
    }

    private fun setUpResultQuestion() {
        for (i in 0 until questionList.size) {
            resultQuestions.add(ResultQuestion(null, null, null, null, questionList[i].id))
        }
    }

    private fun setUplistquestion() {
        for (i in 0 until questionList.size) {
            for (j in 0 until questionList[i].answers!!.size) {
                questionList[i].answers?.get(j)?.isCorrect = false
            }
        }
    }
}