package com.example.drivinglicenceuser.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.drivinglicenceadmin.model.ResultRegisterAccount
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.FragmentLoginBinding
import com.example.drivinglicenceuser.databinding.FragmentSignUpBinding
import com.example.mvvm_retrofit.Status
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding

    var image = "https://drivinglicence.s3.us-west-2.amazonaws.com/avatar-default.png"

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
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.btNewAccountFragment.setOnClickListener {
            registerUser()
        }
        return binding.root
    }

    private fun registerUser() {
        if(checkBeforeRegister()){
            val userName = binding.edtNewAccountFragmentUsername.text.toString()
            val password = binding.edtNewAccountFragmentPassword.text.toString()
            val firstName = binding.edtNewAccountFragmentFirstName.text.toString()
            val lastName = binding.edtNewAccountFragmentLastName.text.toString()
            val email = binding.edtNewAccountFragmentEmail.text.toString()
            val createBy = "sign up"
            val phonNumber = "+84${binding.edtNewAccountFragmentPhoneNumber.text.toString()}"
            userViewModel.registerAccoun(userName, password, firstName, lastName, email, createBy, phonNumber, image).observe(viewLifecycleOwner) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { result ->
                                val i: InputStream = result.errorBody()!!.byteStream()
                                val r = BufferedReader(InputStreamReader(i))
                                val errorResult = StringBuilder()
                                var line: String?
                                try {
                                    while (r.readLine().also { line = it } != null) {
                                        errorResult.append(line).append('\n')
                                    }
                                    Log.d("API_RESPONSE_ERROR_BODY", errorResult.toString())
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                                val resultRegisterAccount: ResultRegisterAccount = Gson().fromJson(errorResult.toString(), ResultRegisterAccount::class.java)
                                Log.d("test: ", "${resultRegisterAccount.title}")
                                val successStr: String = "End of input at line 1 column 1 path \$"
                                if(resultRegisterAccount.title.equals(successStr)){
                                    Toast.makeText(requireContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(requireContext(), "${resultRegisterAccount.title}", Toast.LENGTH_SHORT).show()
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

    private fun checkBeforeRegister(): Boolean {
        if(binding.edtNewAccountFragmentFirstName.text.toString().equals("") || binding.edtNewAccountFragmentLastName.text.toString().equals("")){
            Toast.makeText(requireContext(), "Không được để họ tên trống", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.edtNewAccountFragmentUsername.text.toString().equals("")){
            Toast.makeText(requireContext(), "Không được để tên đăng nhập trống", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.edtNewAccountFragmentPassword.text.toString().equals("")){
            Toast.makeText(requireContext(), "Không được để tên đăng nhập trống", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!binding.edtNewAccountFragmentConfrimPassword.text.toString().equals(binding.edtNewAccountFragmentPassword.text.toString())){
            Toast.makeText(requireContext(), "Xác nhận mật khẩu không trùng với mật khẩu", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}