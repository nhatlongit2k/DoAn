package com.example.drivinglicenceuser.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.drivinglicenceadmin.ACCOUNT
import com.example.drivinglicenceadmin.TOKEN
import com.example.drivinglicenceadmin.model.Login
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.viewmodel.LoginViewModel
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.FragmentLoginBinding
import com.example.drivinglicenceuser.ui.activity.HomeActivity
import com.example.mvvm_retrofit.Status

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    var token = ""
    lateinit var acoount: User

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(requireActivity().application)
        ).get(
            UserViewModel::class.java
        )
    }

    private val loginViewModel: LoginViewModel by lazy{
        ViewModelProvider(this, LoginViewModel.LoginViewModelFactory(requireActivity().application)).get(
            LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.btFragmentloginLogin.setOnClickListener {
            login()
//            testAnswer()
        }

        binding.tvFragmentloginSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.tvFragmentloginForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetFragment)
        }

        return binding.root
    }

    private fun login() {
        val login: Login = Login(binding.edtFragmentloginUsername.text.toString(), binding.edtFragmentloginPassword.text.toString())
        loginViewModel.getTokenFromApi(login).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { tokenkey ->
                            token = tokenkey.token
                            Log.d("TAG", "login: $token")

                            getAccount()

//                            val intent = Intent(activity, HomeActivity::class.java)
//                            intent.putExtra(TOKEN, token)
//                            startActivity(intent)
//                            activity?.finish()
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

    private fun getAccount() {
        userViewModel.getAccount("Bearer ${token}").observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { resut ->
                            acoount = resut
                            if(acoount.authorities.contains("ROLE_USER")){
                                val intent = Intent(activity, HomeActivity::class.java)
                                intent.putExtra(TOKEN, token)
                                intent.putExtra(ACCOUNT, acoount)
                                startActivity(intent)
                                activity?.finish()
                            }else{
                                Toast.makeText(requireContext(), "Tài khoản này k có quyền truy cập!", Toast.LENGTH_SHORT).show()
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