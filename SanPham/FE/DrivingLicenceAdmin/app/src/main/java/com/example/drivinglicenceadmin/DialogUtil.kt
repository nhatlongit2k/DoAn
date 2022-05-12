package com.example.drivinglicenceadmin

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.drivinglicenceadmin.databinding.DialogAnswerBinding
import com.example.drivinglicenceadmin.databinding.DialogChangePasswordBinding
import com.example.drivinglicenceadmin.databinding.DialogTypeBinding
import com.example.drivinglicenceadmin.model.Type

fun Context.showCreateDialogInputType(
    onOkClick: (Type)->Unit,
//    onCancelClick: ()->Unit,
){
    val binding = DialogTypeBinding.inflate(LayoutInflater.from(this))
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
    binding.edtDialogName.requestFocus()
    binding.tvDialogAddOrUpdate.text = "Thêm mới"
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//    binding.edtDialogInput.text = Editable.Factory.getInstance().newEditable(text)
//    binding.tvDialogDone.isEnabled = text.isNotEmpty()

    binding.tvDialogAddOrUpdate.setOnClickListener {
        var type = Type(null, binding.edtDialogName.text.toString(), binding.edtDialogDes.text.toString())
        onOkClick(type)
        dialog.dismiss()
    }

    binding.tvDialogCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun Context.showUpdateDialogInputType(
    type: Type,
    onOkClick: (Type)->Unit
){
    val binding = DialogTypeBinding.inflate(LayoutInflater.from(this))
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
    binding.tvDialogAddOrUpdate.text = "Sửa"
    binding.edtDialogName.setText(type.name)
    binding.edtDialogDes.setText(type.des)
    binding.edtDialogName.requestFocus()
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    binding.tvDialogAddOrUpdate.setOnClickListener {
//        var type = Type(null, binding.edtDialogName.text.toString(), binding.edtDialogDes.text.toString())
        type.name = binding.edtDialogName.text.toString()
        type.des = binding.edtDialogDes.text.toString()
        onOkClick(type)
        dialog.dismiss()
    }

    binding.tvDialogCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun Context.showAddAnswerDialog(
    onOkClick: (String)->Unit
){
    val binding = DialogAnswerBinding.inflate(LayoutInflater.from(this))
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
    binding.tvDialogAddOrUpdate.text = "Thêm"
    binding.dialogAnswerTitle.text = "Thêm mới câu trả lời"
    binding.edtDialogAnswerContent.requestFocus()
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    binding.tvDialogAddOrUpdate.setOnClickListener {
        if (binding.edtDialogAnswerContent.text.toString().equals("")){
            Toast.makeText(this, "Không được để trống câu trả lời", Toast.LENGTH_SHORT).show()
        }else{
            onOkClick(binding.edtDialogAnswerContent.text.toString())
            dialog.dismiss()
        }
    }

    binding.tvDialogCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

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