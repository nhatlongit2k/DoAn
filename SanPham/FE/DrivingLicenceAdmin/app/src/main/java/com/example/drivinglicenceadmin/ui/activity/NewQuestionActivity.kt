package com.example.drivinglicenceadmin.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.databinding.ActivityNewQuestionBinding
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceadmin.model.Question
import com.example.drivinglicenceadmin.ui.adapter.AdapterAnswerRecycler
import com.example.drivinglicenceadmin.ui.amazon_client.AmazonClient
import com.example.drivinglicenceadmin.ui.amazon_client.RealPathUtil
import com.example.drivinglicenceadmin.viewmodel.AnswerViewModel
import com.example.drivinglicenceadmin.viewmodel.QuestionViewModel
import com.example.mvvm_retrofit.Status
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class NewQuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewQuestionBinding
    private lateinit var newOrUpdate: String
    private lateinit var exam: Exam
    private lateinit var question: Question
    var isUploadImage: Boolean = false
    var amazonClient: AmazonClient = AmazonClient()
    var answerList: ArrayList<Answer> = ArrayList()

    private val answerViewModel: AnswerViewModel by lazy {
        ViewModelProvider(this, AnswerViewModel.AnswerViewModelFactory(application)
        ).get(AnswerViewModel::class.java)
    }

    private val questionViewModel: QuestionViewModel by lazy {
        ViewModelProvider(this, QuestionViewModel.QuestionViewModelFactory(application)
        ).get(QuestionViewModel::class.java)
    }

    private val adapterAnswerRecycler: AdapterAnswerRecycler by lazy {
        AdapterAnswerRecycler(this, answerList, onItemClick, onDeleteClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvAnswerLicence.setHasFixedSize(true)
        binding.rvAnswerLicence.layoutManager = LinearLayoutManager(this)
        binding.rvAnswerLicence.adapter = adapterAnswerRecycler

        newOrUpdate = intent.getStringExtra(KEY_QUESTION).toString()
        exam = intent.getSerializableExtra(KEY_PASS_EXAM) as Exam
        if(newOrUpdate == KEY_NEW_QUESTION){
            val id = exam.id
            if (id != null) {
                question = Question(null, "", null, "", false, id, null)
            }
            binding.btQuestionAddOrUpdate.text = "THÊM"
        }else{
            question = intent.getSerializableExtra(KEY_PASS_QUESTION) as Question
            Log.d("TAG", "question: ${question.id}")
            binding.btQuestionAddOrUpdate.text = "SỬA"
            binding.edtQuestionContentInput.setText(question.content)
            binding.edtQuestionReson.setText(question.reason)
            binding.cbIsFallQuestion.isChecked = question.isFallQuestion
            if(question.image != null){
                binding.imgQuestionInput.visibility = View.VISIBLE
                Picasso.get().load(question.image).into(binding.imgQuestionInput)
                binding.btUploadImage.setText("Hủy upload")
                isUploadImage=true
            }
            loadAnswerByQuestion()
        }

        binding.btUploadImage.setOnClickListener {
            if(!isUploadImage){
                isUploadImage = true
                onClickRequestPermission()
                binding.btUploadImage.setText("Hủy upload")
            }else{
                binding.imgQuestionInput.visibility = View.GONE
                isUploadImage = false
                question.image = null
                binding.btUploadImage.setText("UPLOAD ẢNH")
            }
        }

        binding.btnAddNewAnswer.setOnClickListener {
            this.showAddAnswerDialog(onDialogOkClick)
        }

        binding.btQuestionAddOrUpdate.setOnClickListener {
            question.content = binding.edtQuestionContentInput.text.toString()
            question.isFallQuestion = binding.cbIsFallQuestion.isChecked
            question.reason = binding.edtQuestionReson.text.toString()
            if(newOrUpdate == KEY_NEW_QUESTION){
                if(checkBeforeAddQuestion()){
                    addQuestionWithListAnswer()
                }
            }else{
                if(checkBeforeAddQuestion()){
                    updateQuestionWithListAnswer()
                }
            }
        }
    }

    private fun checkBeforeAddQuestion(): Boolean{
        if(binding.edtQuestionContentInput.text.toString().equals("") || binding.edtQuestionReson.text.toString().equals("")){
            Toast.makeText(this, "Không được để trường câu hỏi và trường lời giải trống!!", Toast.LENGTH_LONG).show()
            return false
        }
        if(answerList.size<2){
            Toast.makeText(this, "Câu hỏi phải có ít nhất 2 câu trả lời!!", Toast.LENGTH_LONG).show()
            return false
        }
        for (answer in answerList) {
            if(answer.isCorrect)
                return true
        }
        Toast.makeText(this, "Hãy chọn đáp án chính xác!!", Toast.LENGTH_LONG).show()
        return false
    }

    private fun addQuestionWithListAnswer() {
        question.answers = answerList
        questionViewModel.saveQuestionWithListAnswer(question).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Log.d("TAG", "them thanh cong: ")
                        resource.data?.let { result ->
                            Toast.makeText(this, result.messange, Toast.LENGTH_SHORT).show()
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

    private fun updateQuestionWithListAnswer() {
        question.answers = answerList
        questionViewModel.updateQuestionWithListAnswer(question).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Log.d("TAG", "update thanh cong: ")
                        resource.data?.let { result ->
                            Toast.makeText(this, result.messange, Toast.LENGTH_SHORT).show()
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

    private val onDialogOkClick: (String) -> Unit = {
        val id = question.id
        Log.d("TAG", "id: $id")
        if(id != null){
            val answer = Answer(null, it, id, false)
            answerList.add(answer)
        }else{
            val answer = Answer(null, it, null, false)
            answerList.add(answer)
        }
    }

    private fun onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery()
            return
        }
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery()
        } else {
            val permission = arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, KEY_REQUEST_CAMERA)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, KEY_REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KEY_REQUEST_CAMERA && resultCode == RESULT_OK){
            binding.imgQuestionInput.visibility = View.VISIBLE
            binding.imgQuestionInput.setImageURI(data?.data)
            val uri: Uri? = data?.data

//            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//            val uploadFile = amazonClient.uploadFile(test)
//            Log.d("TAG", "onActivityResult: $uploadFile")

            val realPathUtil: RealPathUtil = RealPathUtil()

            var realPath: String? = realPathUtil.getRealPath(this, uri!!)
            Log.d("TAG", "onActivityResult: $realPath")
            val file: File = File(realPath)

            GlobalScope.launch {
                val uploadFile = amazonClient.uploadFile(file)
                Log.d("TAG", "image: $uploadFile")
                question.image = uploadFile.toString()
            }
        }
    }

    private fun loadAnswerByQuestion() {
        val id = question.id
        if (id != null) {
            answerViewModel.getAnswerByQuestionId(id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Log.d("TAG", "loadAnswerByQuestion: ")
                            resource.data?.let { answers ->
                                answerList.clear()
                                answerList.addAll(answers)
                                adapterAnswerRecycler.setAnswers(answerList)
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

    private val onItemClick: (Answer) -> Unit = {

    }

    private val onDeleteClick: (Answer) -> Unit = {
        val id = it.id
        if (id != null) {
            answerViewModel.deleteAnswerFromApi(id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Log.d("TAG", "loadAnswerByQuestion: ")
                            resource.data?.let { result ->
                                Log.d("TAG", "error: ${result.messange}")
                                Toast.makeText(this, result.messange, Toast.LENGTH_SHORT).show()
                                loadAnswerByQuestion()
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
        }else{
            answerList.remove(it);
            adapterAnswerRecycler.setAnswers(answerList)
        }
    }
}