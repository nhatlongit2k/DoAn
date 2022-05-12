package com.example.drivinglicenceadmin.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.databinding.FragmentExamBinding
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceadmin.ui.activity.HomeActivity
import com.example.drivinglicenceadmin.ui.activity.MainActivity
import com.example.drivinglicenceadmin.ui.activity.NewExamActivity
import com.example.drivinglicenceadmin.ui.adapter.AdapterExamRecycler
import com.example.drivinglicenceadmin.ui.adapter.SpinnerType
import com.example.drivinglicenceadmin.viewmodel.ExamViewModel
import com.example.drivinglicenceadmin.viewmodel.TypeViewModel
import com.example.mvvm_retrofit.Status

class ExamFragment : Fragment() {

    lateinit var binding: FragmentExamBinding
    lateinit var homeActivity: HomeActivity
    private var typeList: ArrayList<Type> = ArrayList()
    var typeListToSpinner: ArrayList<SpinnerType> = ArrayList<SpinnerType>()

    private val examViewModel: ExamViewModel by lazy {
        ViewModelProvider(this, ExamViewModel.ExamViewModelFactory(requireActivity().application)
        ).get(ExamViewModel::class.java)
    }

    private val typeViewModel: TypeViewModel by lazy {
        ViewModelProvider(
            this,
            TypeViewModel.TypeViewModelFactory(requireActivity().application)
        ).get(
            TypeViewModel::class.java
        )
    }

    var examList: ArrayList<Exam> = ArrayList()

    private val adapterExamRecycler: AdapterExamRecycler by lazy {
        AdapterExamRecycler(requireContext(), examList, onItemClick, onDeleteClick, onEditClick)
    }

    private val controller by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExamBinding.inflate(inflater, container, false)

        homeActivity = activity as HomeActivity // Lay du lieu tu activity cha

        binding.rvExamLicence.setHasFixedSize(true)
        binding.rvExamLicence.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExamLicence.adapter = adapterExamRecycler

        getAllTypeData()

        Log.d("TAG", "onCreateView: ${homeActivity.token}")

        binding.slExamLicence.setOnRefreshListener {
            refreshData()
        }
        refreshData()

        binding.btnAddNewExam.setOnClickListener {
            val intent: Intent = Intent(activity, NewExamActivity::class.java)
            intent.putExtra(KEY_EXAM, KEY_NEW_EXAM)
            intent.putExtra(KEY_PASS_ACCOUNT, homeActivity.account)
            startActivityForResult(intent, 1)
        }

        return binding.root
    }

    private fun refreshData() {
        examViewModel.getExamsFromApi().observe(viewLifecycleOwner) {
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
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.slExamLicence.isRefreshing = false
                    }
                    Status.LOADING -> {
                        binding.slExamLicence.isRefreshing = true
                    }
                }
            }
        }
    }

    private val onItemClick: (Exam) -> Unit = {

    }

    private val onDeleteClick: (Exam) -> Unit = {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val id = it.id
                        if (id != null) {
                            examViewModel.deleteExamFromApi(id).observe(this) {
                                it?.let { resource ->
                                    when (resource.status) {
                                        Status.SUCCESS -> {
                                            resource.data?.let { result ->
                                                Log.d("TAG", "Ket qua: ${result.messange}")
                                                refreshData()
                                                Toast.makeText(context, result.messange, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        Status.ERROR -> {
                                            Log.d("TAG", "error: ${it.message}")
                                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                        }
                                        Status.LOADING -> {
                                        }
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

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Bạn có chắc muốn xóa?")
            .setPositiveButton("Xóa", dialogClickListener)
            .setNegativeButton("Hủy", dialogClickListener).show()
    }

    private val onEditClick: (Exam) -> Unit = {
        val intent: Intent = Intent(activity, NewExamActivity::class.java)
        intent.putExtra(KEY_EXAM, KEY_UPDATE_EXAM)
        intent.putExtra(KEY_PASS_ACCOUNT, homeActivity.account)
        intent.putExtra(KEY_PASS_EXAM, it)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivityResult: $requestCode")
        binding.spinnerTypeForExam.setSelection(0)
        refreshData()
    }

    private fun getAllTypeData() {
        typeViewModel.getTypesFromApi().observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { types ->
                            typeList.clear()
                            typeList.addAll(types)
//                            typeListToSpinner = ArrayList<SpinnerType>()
                            typeListToSpinner.add(SpinnerType(0.toLong(), "Tất cả"))
                            for (type in types) {
                                val id = type.id
                                if (id != null) {
                                    typeListToSpinner.add(SpinnerType(id, type.name))
                                }
                            }
                            var spinnerAdapter: ArrayAdapter<SpinnerType> =
                                ArrayAdapter<SpinnerType>(
                                    requireContext().applicationContext,
                                    R.layout.style_spinner,
                                    typeListToSpinner
                                )
                            binding.spinnerTypeForExam.adapter = spinnerAdapter

//                            for (i in 0 until typeListToSpinner.size) {
//                                if(typeListToSpinner[i].id == exam.typeId){
//                                    binding.spinnerType.setSelection(i)
//                                }
//                            }
                            binding.spinnerTypeForExam.onItemSelectedListener = object:
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    Log.d("TAG", "i: $p2, l: $p3")
//                                    if(typeListToSpinner[p2].id == 15.toLong()){
//                                        binding.edtTimeDoExam.setText("")
//                                        binding.edtTimeDoExam.isEnabled = false
//                                    }else{
//                                        binding.edtTimeDoExam.isEnabled = true
//                                    }
                                    if(typeListToSpinner[p2].id == 0.toLong()){
                                        refreshData()
                                    }
                                    else{
                                        Log.d("TAG", "typeListToSpinner[p2].id : ${typeListToSpinner[p2].id }")
                                        var examFilterList: ArrayList<Exam> = ArrayList()
                                        for (i in 0 until examList.size) {
                                            if(examList[i].typeId == typeListToSpinner[p2].id){
                                                examFilterList.add(examList[i])
                                            }
                                        }
                                        adapterExamRecycler.setExams(examFilterList)
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
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }
}