package com.example.drivinglicenceuser.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.FragmentForgetBinding
import com.example.drivinglicenceuser.databinding.FragmentInputOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.*
import java.util.concurrent.TimeUnit

class InputOtpFragment : Fragment() {

    lateinit var binding: FragmentInputOtpBinding
    var phoneNumber = ""
    var login = ""
    var verification = ""
    var strOTP = ""

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInputOtpBinding.inflate(inflater, container, false)
        phoneNumber = arguments?.getString("phone_number").toString()
        login = arguments?.getString("login").toString()
        sendOTPToPhoneNumber()
        binding.tvSendToNumber.setText("Mã OTP đã gửi đến $phoneNumber")
        binding.btFragmentinputVerify.setOnClickListener {
            Log.d("TAG", "onCreateView: $verification")
            val credential = PhoneAuthProvider.getCredential(verification, strOTP)
            signInWithPhoneAuthCredential(credential)
        }

        binding.tvResend.setOnClickListener {
            sendOTPToPhoneNumber()
        }

        setUpOTPInput()
        return binding.root
    }

    private fun sendOTPToPhoneNumber() {
        Log.d("TAG", "sendOTPToPhoneNumber: ")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("TAG", "onVerificationCompleted: ")
                    signInWithPhoneAuthCredential(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(requireContext(), "verification failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, p1)
                    verification = verificationId
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
                    goToFragmentResetPassword()
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

//    private fun signInWithPhoneAuthCredentialWhenSendOTP(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "signInWithCredential:success")
//                    goToFragmentResetPassword()
////                    val user = task.result?.user
//
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    Log.w("TAG", "signInWithCredential:failure", task.exception)
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                        Toast.makeText(requireContext(), "The verification code entered was invalid", Toast.LENGTH_SHORT).show()
//                    }
//                    // Update UI
//                }
//            }
//    }

    private fun goToFragmentResetPassword() {
        Log.d("TAG", "goToFragmentResetPassword: ")
        val bundle = bundleOf("phone_number" to phoneNumber, "login" to login)
        findNavController().navigate(R.id.action_inputOtpFragment_to_resetPasswordFragment, bundle)
    }

    private fun setUpOTPInput(){
        binding.edtInput1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){
                    binding.edtInput2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.edtInput2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){
                    binding.edtInput3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.edtInput3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){
                    binding.edtInput4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.edtInput4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){
                    binding.edtInput5.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.edtInput5.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.toString().trim().isEmpty()){
                    binding.edtInput6.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
}