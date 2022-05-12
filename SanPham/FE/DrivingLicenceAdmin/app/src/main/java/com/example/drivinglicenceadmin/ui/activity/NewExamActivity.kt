package com.example.drivinglicenceadmin.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.databinding.ActivityNewExamBinding
import com.example.drivinglicenceadmin.model.*
import com.example.drivinglicenceadmin.ui.adapter.AdapterQuestionRecycler
import com.example.drivinglicenceadmin.ui.adapter.SpinnerType
import com.example.drivinglicenceadmin.viewmodel.ExamViewModel
import com.example.drivinglicenceadmin.viewmodel.QuestionViewModel
import com.example.drivinglicenceadmin.viewmodel.TypeViewModel
import com.example.mvvm_retrofit.Status
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class NewExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewExamBinding
    private var typeList: ArrayList<Type> = ArrayList()
    private lateinit var newOrUpdate: String
    private lateinit var exam: Exam
    private lateinit var account: User
    var typeListToSpinner: ArrayList<SpinnerType> = ArrayList<SpinnerType>()
    var questionList: ArrayList<Question> = ArrayList()

    var strPath: String =""

    private val typeViewModel: TypeViewModel by lazy {
        ViewModelProvider(
            this,
            TypeViewModel.TypeViewModelFactory(application)
        ).get(
            TypeViewModel::class.java
        )
    }

    private val examViewModel: ExamViewModel by lazy {
        ViewModelProvider(this, ExamViewModel.ExamViewModelFactory(application)
        ).get(ExamViewModel::class.java)
    }

    private val questionViewModel: QuestionViewModel by lazy {
        ViewModelProvider(this, QuestionViewModel.QuestionViewModelFactory(application)
        ).get(QuestionViewModel::class.java)
    }

    private val adapterQuestionRecycler: AdapterQuestionRecycler by lazy {
        AdapterQuestionRecycler(this, questionList, onItemClick, onDeleteClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewExamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvQuestionLicence.setHasFixedSize(true)
        binding.rvQuestionLicence.layoutManager = LinearLayoutManager(this)
        binding.rvQuestionLicence.adapter = adapterQuestionRecycler

        newOrUpdate = intent.getStringExtra(KEY_EXAM).toString()
        account = intent.getSerializableExtra(KEY_PASS_ACCOUNT) as User
        if(newOrUpdate == KEY_NEW_EXAM){
            newExam()
        }else{
            exam = intent.getSerializableExtra(KEY_PASS_EXAM) as Exam
            loadQuestionWithExamId()
            setUpDisplay()
        }

        getAllTypeData()

        binding.btnAddNewQuestion.setOnClickListener {
            val intent: Intent = Intent(this, NewQuestionActivity::class.java)
            intent.putExtra(KEY_QUESTION, KEY_NEW_QUESTION)
            intent.putExtra(KEY_PASS_EXAM, exam)
            startActivityForResult(intent, 10)
        }

        binding.btExamAddOrUpdate.setOnClickListener {
            if(checkBeforeAddOrUpdate()){
                addExam()
                finish()
            }
        }

        binding.imgUploadExcel.setOnClickListener {
            Log.d("TAG", "imgUploadExcel: ")
            val intent: Intent = Intent(this, FilePickerActivity::class.java)
            intent.putExtra(
                FilePickerActivity.CONFIGS,
                Configurations.Builder().setCheckPermission(true).setShowFiles(true)
                    .setShowImages(false).setShowImages(false).setMaxSelection(1).setSuffixes("xls")
                    .setSkipZeroSizeFiles(true).build()
            )
            startActivityForResult(intent, 102)
        }

//        var typeList1: ArrayList<SpinnerType> = ArrayList<SpinnerType>()
//        typeList1.add(SpinnerType(1, "A1"))
//        typeList1.add(SpinnerType(2, "C1"))
//        typeList1.add(SpinnerType(3, "B1"))
//
//        var spinnerAdapter: ArrayAdapter<SpinnerType> = ArrayAdapter<SpinnerType>(applicationContext, android.R.layout.simple_spinner_dropdown_item, typeList1)
//        binding.spinnerType.adapter = spinnerAdapter
//
//        var spn: SpinnerType = binding.spinnerType.selectedItem as SpinnerType
//
//        Log.d("TAG", "onCreate: ${spn.id}")
    }

    private fun checkBeforeAddOrUpdate(): Boolean {
        if(binding.edtExamName.text.toString().equals("")){
            Toast.makeText(this, "Không được để tên đề trống!", Toast.LENGTH_SHORT).show()
            return false
        }
        if(typeListToSpinner[binding.spinnerType.selectedItemPosition].id != 15.toLong() && binding.edtTimeDoExam.text.toString().equals("")){
            Toast.makeText(this, "Không được để thời gian làm bài trống!", Toast.LENGTH_SHORT).show()
            return false
        }
        if(questionList.isEmpty()){
            Toast.makeText(this, "Đề ít nhất phải có một câu hỏi", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addExam() {
        val id = exam.id
        exam.numberOfQuestion = questionList.size.toLong();
        exam.typeId = typeListToSpinner[binding.spinnerType.selectedItemPosition].id
        if(exam.typeId == 15.toLong()){
            exam.time = null
        }else{
            exam.time = binding.edtTimeDoExam.text.toString().toLong()
        }
        exam.name = binding.edtExamName.text.toString()
        if(newOrUpdate == KEY_NEW_EXAM){
            exam.createBy = account.login
        }else{
            exam.updateBy = account.login
        }
        if (id != null) {
            examViewModel.updateExamToApi(id, exam).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { result ->
                                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
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

    private fun setUpDisplay() {
        binding.edtExamName.setText(exam.name)
        binding.edtTimeDoExam.setText(exam.time.toString())
        binding.btExamAddOrUpdate.setText("Sửa")
        Log.d("TAG", "typeListToSpinner size: ${typeListToSpinner.size}")
//        for (i in 0 until typeListToSpinner.size) {
//            if(typeListToSpinner[i].id == exam.id){
//                Log.d("TAG", "tim dc r: ${typeListToSpinner[i].id}")
//                binding.spinnerType.setSelection(i)
//            }
//        }
    }

    private fun loadQuestionWithExamId() {
        val id = exam.id
        if (id != null) {
            questionViewModel.getQuestionByExamId(id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.slQuestionLicence.isRefreshing = false
                            resource.data?.let { questions ->
                                questionList.clear()
                                questionList.addAll(questions)
                                adapterQuestionRecycler.setQuestions(questionList)
                            }

                        }
                        Status.ERROR -> {
                            Log.d("TAG", "error: ${it.message}")
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            binding.slQuestionLicence.isRefreshing = false
                        }
                        Status.LOADING -> {
                            binding.slQuestionLicence.isRefreshing = true
                        }
                    }
                }
            }
        }
    }

    private fun newExam() {
        exam = Exam(null, "tempExam", 0, 10.0F, null, null, "system", "", null, null, null, null)
        examViewModel.createExamToApi(exam).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { examResult ->
                            exam = examResult
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


    private fun getAllTypeData() {
        typeViewModel.getTypesFromApi().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { types ->
                            typeList.clear()
                            typeList.addAll(types)
//                            typeListToSpinner = ArrayList<SpinnerType>()
                            for (type in types) {
                                val id = type.id
                                if (id != null) {
                                    typeListToSpinner.add(SpinnerType(id, type.name))
                                }
                            }
                            var spinnerAdapter: ArrayAdapter<SpinnerType> =
                                ArrayAdapter<SpinnerType>(
                                    applicationContext,
                                    R.layout.style_spinner,
                                    typeListToSpinner
                                )
                            binding.spinnerType.adapter = spinnerAdapter

                            for (i in 0 until typeListToSpinner.size) {
                                if(typeListToSpinner[i].id == exam.typeId){
                                    binding.spinnerType.setSelection(i)
                                }
                            }
                            binding.spinnerType.onItemSelectedListener = object:
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    Log.d("TAG", "i: $p2, l: $p3")
                                    if(typeListToSpinner[p2].id == 15.toLong()){
                                        binding.edtTimeDoExam.setText("")
                                        binding.edtTimeDoExam.isEnabled = false
                                    }else{
                                        binding.edtTimeDoExam.isEnabled = true
                                    }
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    Log.d("TAG", "onNothingSelected: ")
                                }

                            }
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

    private val onItemClick: (Question) -> Unit = {
        val intent: Intent = Intent(this, NewQuestionActivity::class.java)
        intent.putExtra(KEY_QUESTION, KEY_UPDATE_QUESTION)
        intent.putExtra(KEY_PASS_EXAM, exam)
        intent.putExtra(KEY_PASS_QUESTION, it)
        startActivityForResult(intent, 10)
    }

    private val onDeleteClick: (Question) -> Unit = {
        Log.d("TAG", "id question: ${it.id}")
        val id = it.id
        if (id != null) {
            questionViewModel.deleteQuestionFromApi(id).observe(this) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { question ->
                                Log.d("TAG", " result: ${question.messange}")
                                Toast.makeText(this, question.messange, Toast.LENGTH_SHORT).show()
                                loadQuestionWithExamId()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10){
            loadQuestionWithExamId()
        }

        if(resultCode == RESULT_OK && data != null){
            var mediaFiles: ArrayList<MediaFile> = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)!!

            var path: String = mediaFiles.get(0).path

            when(requestCode){
                102 -> {
                    Log.d("TAG", "filePath: $path")
                    strPath = path
                    readExcelFileFromAssets(strPath)
                }
            }
        }
    }

    override fun onBackPressed() {
        if(newOrUpdate == KEY_NEW_EXAM){
            val id = exam.id
            if (id != null) {
                examViewModel.deleteExamFromApi(id).observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                resource.data?.let { result ->
                                    Log.d("TAG", "onBackPressed: ${result.messange}")
//                                    Toast.makeText(this, result.messange, Toast.LENGTH_SHORT).show()
                                    super.onBackPressed()
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
            super.onBackPressed()
        }
    }

    fun readExcelFileFromAssets(path: String) {
//        try {
//            var myInput: InputStream
//            // initialize asset manager
////            val assetManager = assets
//            //  open excel sheet
////            myInput = assetManager.open("myexcelsheet.xls")
//
//            val file: File = File(path)
//            myInput = FileInputStream(file)
//            // Create a POI File System object
//            val myFileSystem = POIFSFileSystem(myInput)
//            // Create a workbook using the File System
//            val myWorkBook = HSSFWorkbook(myFileSystem)
//            // Get the first sheet from workbook
//            val mySheet = myWorkBook.getSheetAt(0)
//            // We now need something to iterate through the cells.
//            val rowIter = mySheet.rowIterator()
//            var rowno = 0
//            while (rowIter.hasNext()) {
//                Log.e("TAG", " row no $rowno")
//                val myRow = rowIter.next() as HSSFRow
//                if (rowno != 0) {
//                    val cellIter = myRow.cellIterator()
//                    var colno = 0
//                    var sno = ""
//                    var date = ""
//                    var det = ""
//                    while (cellIter.hasNext()) {
//                        val myCell = cellIter.next() as HSSFCell
//                        if (colno == 0) {
//                            sno = myCell.toString()
//                        } else if (colno == 1) {
//                            date = myCell.toString()
//                        } else if (colno == 2) {
//                            det = myCell.toString()
//                        }
//                        colno++
//                        Log.e("TAG", " Index :" + myCell.columnIndex + " -- " + myCell.toString())
//                    }
//                }
//                rowno++
//            }
//        } catch (e: Exception) {
//            Log.e("TAG", "error $e")
//        }

        val listQuestionFromExcel = ArrayList<Question>()
        try {
            var myInput: InputStream
            // initialize asset manager
//            val assetManager = assets
            //  open excel sheet
//            myInput = assetManager.open("myexcelsheet.xls")

            val file: File = File(path)
            myInput = FileInputStream(file)
            // Create a POI File System object
            val myFileSystem = POIFSFileSystem(myInput)
            // Create a workbook using the File System
            val myWorkBook = HSSFWorkbook(myFileSystem)
            // Get the first sheet from workbook
            val mySheet = myWorkBook.getSheetAt(0)
            // We now need something to iterate through the cells.
            val rowIter = mySheet.rowIterator()
            var rowno = 0
            while (rowIter.hasNext()) {
                val myRow = rowIter.next() as HSSFRow
//                val question: Question = Question(null, "", null, "", false, exam.id!!, ArrayList<Answer>())
                if(rowno>=0){
                    val cellIter = myRow.cellIterator()
                    var colno = 0
                    if (cellIter.hasNext()){
                        val firstCell = cellIter.next() as HSSFCell
//                        Log.e("TAG", "firstCell: ${firstCell.toString()}")
                        if(colno == 0 && !firstCell.toString().equals("+")){
                            val content = firstCell.toString()
                            Log.e("TAG", "content: ${content.toString()}" )
                            var image: String? = null
                            var reason: String = ""
                            var isFallQuestion: Boolean = false
                            while (cellIter.hasNext()){
                                val myCell = cellIter.next() as HSSFCell
                                colno ++
                                when(colno){
                                    1 ->{
                                        if (!myCell.toString().equals("*")){
                                            image = myCell.toString()
                                        }
                                        Log.e("TAG", "imageUrl: ${image}" )
                                    }
                                    2 ->{
                                        reason = myCell.toString()
                                        Log.e("TAG", "reason: ${reason}" )
                                    }
                                    3 ->{
                                        if(myCell.toString().toFloat() != 0.0F){
                                            isFallQuestion = true
                                        }
                                        Log.e("TAG", "isFallQuestion: ${isFallQuestion}" )
                                    }
                                }
                            }
                            val id = exam.id
                            if(id!=null){
                                listQuestionFromExcel.add(Question(null, content, image, reason, isFallQuestion, id, ArrayList<Answer>()))
                            }
                        }else{
                            val fCell = firstCell.toString()
                            Log.e("TAG", "content: ${fCell.toString()}" )
                            var answerContent: String = ""
                            var isCorrect: Boolean = false
                            while (cellIter.hasNext()){
                                val myCell = cellIter.next() as HSSFCell
                                colno ++
                                when(colno){
                                    1 ->{
                                        answerContent = myCell.toString()
                                        Log.e("TAG", "answerContent: ${answerContent}" )
                                    }
                                    2 ->{
                                        if(myCell.toString().toFloat() != 0.0F){
                                            isCorrect = true
                                        }
                                    }
                                }
                            }
                            if(!listQuestionFromExcel.isEmpty()){
                                listQuestionFromExcel[listQuestionFromExcel.size-1].answers?.add(Answer(null, answerContent, null, isCorrect))
                                Log.d("TAG", "listAnswerSize: ${listQuestionFromExcel[listQuestionFromExcel.size-1].answers?.size}")
                            }
                        }
                    }
                }
                rowno++
            }
            Log.d("TAG", "listQuestion size: ${listQuestionFromExcel.size}")
            saveAllQuestionWithAnswer(listQuestionFromExcel)
        } catch (e: Exception) {
            Log.e("TAG", "error $e")
        }
    }

    private fun saveAllQuestionWithAnswer(listQuestionFromExcel: ArrayList<Question>) {
        Log.d("TAG", "listQuestionFromExcel: ${listQuestionFromExcel.size}")
        questionViewModel.saveAllQuestionWithAnswer(listQuestionFromExcel).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.slQuestionLicence.isRefreshing = false
                        resource.data?.let { questions ->
                            loadQuestionWithExamId()
                        }

                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        binding.slQuestionLicence.isRefreshing = false
                    }
                    Status.LOADING -> {
                        binding.slQuestionLicence.isRefreshing = true
                    }
                }
            }
        }
    }
}