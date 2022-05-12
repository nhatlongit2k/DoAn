package com.example.drivinglicenceadmin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.drivinglicenceadmin.ACCOUNT
import com.example.drivinglicenceadmin.R
import com.example.drivinglicenceadmin.TOKEN
import com.example.drivinglicenceadmin.databinding.FragmentLoginBinding
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Login
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.ui.activity.HomeActivity
import com.example.drivinglicenceadmin.ui.activity.LoginActivity
import com.example.drivinglicenceadmin.viewmodel.AnswerViewModel
import com.example.drivinglicenceadmin.viewmodel.LoginViewModel
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.mvvm_retrofit.Status
import com.squareup.picasso.Picasso

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

//    var answers: List<Answer> = ArrayList()

    private val loginViewModel: LoginViewModel by lazy{
        ViewModelProvider(this, LoginViewModel.LoginViewModelFactory(requireActivity().application)).get(LoginViewModel::class.java)
    }

    private val answerViewModel: AnswerViewModel by lazy{
        ViewModelProvider(this, AnswerViewModel.AnswerViewModelFactory(requireActivity().application)).get(AnswerViewModel::class.java)
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

        binding.tvFragmentloginForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetFragment)
        }
        return binding.root
    }

    private fun testAnswer() {
        answerViewModel.getAnswersFromApi().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS->{
                        resource.data?.let { answers->
                            Log.d("TAG", "testAnswer: ${answers.size}")
                        }
                    }
                    Status.ERROR ->{
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING->{
                    }
                }
            }
        })
    }

    private fun login() {
        binding.pgbLogin.visibility = View.VISIBLE
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
                        Toast.makeText(requireContext(), "Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                        binding.pgbLogin.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.pgbLogin.visibility = View.VISIBLE
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
                            if(acoount.authorities.contains("ROLE_ADMIN")){
                                val intent = Intent(activity, HomeActivity::class.java)
                                intent.putExtra(TOKEN, token)
                                intent.putExtra(ACCOUNT, acoount)
                                Log.d("TAG", "getAccount: ${acoount.imageUrl}")
                                binding.pgbLogin.visibility = View.GONE
                                startActivity(intent)
                                activity?.finish()
                            }else{
                                Toast.makeText(requireContext(), "Tài khoản này k có quyền truy cập!", Toast.LENGTH_SHORT).show()
                                binding.pgbLogin.visibility = View.GONE
                            }
                        }

                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(requireContext(), "Tên tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show()
                        binding.pgbLogin.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.pgbLogin.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}