package com.example.drivinglicenceuser.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.FragmentInputOtpBinding
import com.example.drivinglicenceuser.databinding.FragmentResetPasswordBinding
import com.example.mvvm_retrofit.Status

class ResetPasswordFragment : Fragment() {

    lateinit var binding: FragmentResetPasswordBinding
    var phoneNumber = ""
    var login = ""

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(requireActivity().application)
        ).get(
            UserViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        phoneNumber = arguments?.getString("phone_number").toString()
        login = arguments?.getString("login").toString()

        binding.btFragmentResetConfirm.setOnClickListener {
            if(checkingBeforeReset()){
                resetPassword()
            }
        }
        return binding.root
    }

    private fun resetPassword() {
        val newPassword = binding.edtNewPasswordFragmentReset.text.toString()
        userViewModel.resetPasswordForUser(newPassword,login).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { resut ->
                            Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                        }

                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(requireContext(), "Có lỗi khi thực hiện reset password", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    private fun checkingBeforeReset(): Boolean {
        if(binding.edtNewPasswordFragmentReset.text.toString().equals("")){
            Toast.makeText(requireContext(), "Hãy nhập password!", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!binding.edtNewPasswordFragmentReset.text.toString().equals(binding.edtConfirmNewPasswordFragmentReset.text.toString())){
            Toast.makeText(requireContext(), "Xác nhận mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}