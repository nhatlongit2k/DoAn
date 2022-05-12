package com.example.drivinglicenceadmin.ui.fragment

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
import com.example.drivinglicenceadmin.databinding.FragmentForgetBinding
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.drivinglicenceadmin.R
import com.example.mvvm_retrofit.Status
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class ForgetFragment : Fragment() {

    lateinit var binding: FragmentForgetBinding
    var login: String =""
    var phoneNumber: String = ""

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetBinding.inflate(inflater, container, false)
        binding.btFragmentforgetSendOtp.setOnClickListener {
            login = binding.edtUsernameFragmentForgot.text.toString()
            sendOTPClick()
        }
        return binding.root
    }

    private fun sendOTPClick() {
        userViewModel.getUserByLogin(login).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { resut ->
//                            Log.d("TAG", "sendOTPClick: ${resut.phoneNumber}")
                            phoneNumber = resut.phoneNumber
                            val bundle = bundleOf("phone_number" to phoneNumber, "login" to login)
                            findNavController().navigate(R.id.action_forgetFragment_to_inputOtpFragment, bundle)
                        }

                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(requireContext(), "Không có tài khoản này!!", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }


    private fun sendOTPToPhoneNumber() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(requireContext(), "verification failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, p1)
                }
            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
//                    goToFragmentResetPassword()
//                    val user = task.result?.user

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(requireContext(), "The verification code entered was invalid", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }
}