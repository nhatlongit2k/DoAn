package com.example.drivinglicenceadmin.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.drivinglicenceadmin.ACCOUNT
import com.example.drivinglicenceadmin.KEY_REQUEST_CAMERA
import com.example.drivinglicenceadmin.TOKEN
import com.example.drivinglicenceadmin.databinding.ActivityNewAccountBinding
import com.example.drivinglicenceadmin.model.ResultRegisterAccount
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.ui.amazon_client.AmazonClient
import com.example.drivinglicenceadmin.ui.amazon_client.RealPathUtil
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.mvvm_retrofit.Status
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*


class NewAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewAccountBinding

    var token: String =""
    lateinit var account: User
    var amazonClient: AmazonClient = AmazonClient()
    var image = "https://drivinglicence.s3.us-west-2.amazonaws.com/avatar-default.png"

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(application)
        ).get(
            UserViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(TOKEN).toString()
        account = intent.getSerializableExtra(ACCOUNT) as User

        binding.imgNewAccountActivityAvatar.setOnClickListener {
            onClickRequestPermission()
        }

        binding.btNewAccountActivityUp.setOnClickListener {
            registerUserAdmin()
        }
    }

    private fun onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery()
            return
        }
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery()
        } else {
            val permission = arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, KEY_REQUEST_CAMERA)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, KEY_REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KEY_REQUEST_CAMERA && resultCode == RESULT_OK){
//            binding.imgQuestionInput.visibility = View.VISIBLE
//            binding.imgQuestionInput.setImageURI(data?.data)
            binding.imgNewAccountActivityAvatar.setImageURI(data?.data)
            val uri: Uri? = data?.data

//            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//            val uploadFile = amazonClient.uploadFile(test)
//            Log.d("TAG", "onActivityResult: $uploadFile")

            val realPathUtil: RealPathUtil = RealPathUtil()

            var realPath: String? = realPathUtil.getRealPath(this, uri!!)
            Log.d("TAG", "onActivityResult: $realPath")
            val file: File = File(realPath)

            GlobalScope.launch {
                val uploadFile = amazonClient.uploadFile(file)
                Log.d("TAG", "image: $uploadFile")
                image = uploadFile.toString()
            }
        }
    }

    private fun registerUserAdmin() {
        if(checkBeforeRegister()){
            val userName = binding.edtNewAccountActivityUsername.text.toString()
            val password = binding.edtNewAccountActivityPassword.text.toString()
            val firstName = binding.edtNewAccountActivityFirstName.text.toString()
            val lastName = binding.edtNewAccountActivityLastName.text.toString()
            val email = binding.edtNewAccountActivityEmail.text.toString()
            val createBy = account.login
            val phonNumber = "+84${binding.edtNewAccountActivityPhoneNumber.text.toString()}"
            userViewModel.registerAccountAdmin(userName, password, firstName, lastName, email, createBy, phonNumber, image).observe(this) {
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

                                Toast.makeText(this, "${resultRegisterAccount.title}", Toast.LENGTH_SHORT).show()

                                finish()
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

    private fun checkBeforeRegister(): Boolean {
        if(binding.edtNewAccountActivityFirstName.text.toString().equals("") || binding.edtNewAccountActivityLastName.text.toString().equals("")){
            Toast.makeText(this, "Không được để họ tên trống", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.edtNewAccountActivityUsername.text.toString().equals("")){
            Toast.makeText(this, "Không được để tên đăng nhập trống", Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.edtNewAccountActivityPassword.text.toString().equals("")){
            Toast.makeText(this, "Không được để tên đăng nhập trống", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!binding.edtNewAccountActivityConfrimPassword.text.toString().equals(binding.edtNewAccountActivityPassword.text.toString())){
            Toast.makeText(this, "Xác nhận mật khẩu không trùng với mật khẩu", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}