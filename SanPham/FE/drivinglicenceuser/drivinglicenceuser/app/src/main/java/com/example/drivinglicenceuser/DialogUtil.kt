package com.example.drivinglicenceadmin

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.DialogChangePasswordBinding
import com.example.drivinglicenceuser.databinding.DialogExamResultBinding
import com.example.drivinglicenceuser.databinding.DialogSelectTypeExamBinding
import com.example.drivinglicenceuser.model.ResultTest
import com.example.drivinglicenceuser.ui.adapter.AdapterTypeToSelect

fun Context.showChangePasswordDialog(
    onOkClick: (String, String)->Unit
){
    val binding = DialogChangePasswordBinding.inflate(LayoutInflater.from(this))
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.setCanceledOnTouchOutside(false)
    val window: Window? = dialog.window
    window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.attributes?.gravity = Gravity.CENTER
    binding.edtDialogOldPassword.requestFocus()
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    binding.tvDialogConfirm.setOnClickListener {
        if(binding.edtDialogNewPassword.text.toString().equals(binding.edtDialogConfirmNewPassword.text.toString())){
            onOkClick(binding.edtDialogOldPassword.text.toString(), binding.edtDialogConfirmNewPassword.text.toString())
            dialog.dismiss()
        }
    }

    binding.tvDialogCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun Context.showSlectTypeDialog(
    typeList: ArrayList<Type>,
    onOkClick: (Type)->Unit
){

    val adapterTypeToSelect: AdapterTypeToSelect by lazy {
        AdapterTypeToSelect(this, typeList, onOkClick)
    }

    val binding = DialogSelectTypeExamBinding.inflate(LayoutInflater.from(this))
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.setCanceledOnTouchOutside(false)
    val window: Window? = dialog.window
    window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.attributes?.gravity = Gravity.CENTER
//    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    binding.rvDialogTypeLicence.setHasFixedSize(true)
    binding.rvDialogTypeLicence.layoutManager = LinearLayoutManager(this)
    binding.rvDialogTypeLicence.adapter = adapterTypeToSelect

    adapterTypeToSelect.setTypes(typeList)

    binding.tvDialogCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun Context.showResultExamDialog(
    resultTest: ResultTest,
    onSeeResultClick: ()->Unit,
    onOkClick: ()->Unit
){

    val binding = DialogExamResultBinding.inflate(LayoutInflater.from(this))
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.setCanceledOnTouchOutside(false)
    val window: Window? = dialog.window
    window?.setLayout(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.attributes?.gravity = Gravity.CENTER
//    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    if(!resultTest.isPass){
        binding.tvResultTileExam.setText("Rất tiếc")
        binding.tvResultExam.setText("Bạn chưa đạt điều kiện vượt qua bài thi!")
        binding.tvNumberCorrectAnswer.setText("Đúng ${resultTest.numberCorrectQuestion}/25 câu")
        binding.imgIsPass.setImageResource(R.drawable.ic_baseline_dangerous_192)
        binding.tvResultExam.setTextColor(Color.RED)
        binding.tvResultTileExam.setTextColor(Color.RED)
        binding.tvNumberCorrectAnswer.setTextColor(Color.RED)
    }

    binding.tvDialogSeeResult.setOnClickListener {
        onSeeResultClick()
        dialog.dismiss()
    }

    binding.tvDialogOk.setOnClickListener {
        onOkClick()
        dialog.dismiss()
    }

    dialog.show()
}