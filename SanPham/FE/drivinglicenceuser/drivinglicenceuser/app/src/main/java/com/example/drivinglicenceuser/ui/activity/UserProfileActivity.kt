package com.example.drivinglicenceuser.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.drivinglicenceadmin.ACCOUNT
import com.example.drivinglicenceadmin.KEY_REQUEST_CAMERA
import com.example.drivinglicenceadmin.TOKEN
import com.example.drivinglicenceadmin.model.ResultRegisterAccount
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.ui.amazon_client.AmazonClient
import com.example.drivinglicenceadmin.ui.amazon_client.RealPathUtil
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.ActivityDoExamBinding
import com.example.drivinglicenceuser.databinding.ActivityUserProfileBinding
import com.example.drivinglicenceuser.viewmodel.ResultTestViewModel
import com.example.mvvm_retrofit.Status
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*

class UserProfileActivity : AppCompatActivity() {

    var token: String = ""
    lateinit var account: User
    var amazonClient: AmazonClient = AmazonClient()
    private lateinit var binding: ActivityUserProfileBinding
    var image: String = ""

    var edit_avatar = false
    var edit_email = false
    var edit_phone_number = false
    var edit_name = false

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(application)
        ).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = intent.getStringExtra(TOKEN).toString()
        account = intent.getSerializableExtra(ACCOUNT) as User
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        image = account.imageUrl
        setContentView(binding.root)
        setUserData()

        binding.imgEditImage.setOnClickListener {
            if(!edit_avatar){
                onClickRequestPermission()
                Log.d("TAG", "vao edit img: ")
            }else{
                edit_avatar = false
                binding.imgEditImage.setImageResource(R.drawable.ic_baseline_edit_24)
                if(account.imageUrl.equals("")){
                    Picasso.get().load("https://drivinglicence.s3.us-west-2.amazonaws.com/avatar-default.png").into(binding.imgAvatar)
                }else{
                    Picasso.get().load(account.imageUrl).into(binding.imgAvatar)
                }
                if(checkedit()){
                    binding.btEdit.visibility = View.VISIBLE
                }else{
                    binding.btEdit.visibility = View.GONE
                }
            }
            if(checkedit()){
                binding.btEdit.visibility = View.VISIBLE
            }else{
                binding.btEdit.visibility = View.GONE
            }
        }

        binding.imgEditName.setOnClickListener {
            if(!edit_name){
                edit_name = true
                binding.imgEditName.setImageResource(R.drawable.ic_baseline_close_24)
                binding.llEditName.visibility = View.VISIBLE
            }else{
                edit_name = false
                binding.imgEditName.setImageResource(R.drawable.ic_baseline_edit_24)
                binding.llEditName.visibility = View.GONE
            }
            if(checkedit()){
                binding.btEdit.visibility = View.VISIBLE
            }else{
                binding.btEdit.visibility = View.GONE
            }
        }

        binding.imgEditEmail.setOnClickListener {
            if(!edit_email){
                edit_email = true
                binding.imgEditEmail.setImageResource(R.drawable.ic_baseline_close_24)
                binding.edtEmail.visibility = View.VISIBLE
            }else{
                edit_email = false
                binding.imgEditEmail.setImageResource(R.drawable.ic_baseline_edit_24)
                binding.edtEmail.visibility = View.GONE
            }
            if(checkedit()){
                binding.btEdit.visibility = View.VISIBLE
            }else{
                binding.btEdit.visibility = View.GONE
            }
        }

        binding.imgEditPhoneNumber.setOnClickListener {
            if(!edit_phone_number){
                edit_phone_number = true
                binding.imgEditPhoneNumber.setImageResource(R.drawable.ic_baseline_close_24)
                binding.llPhoneNumber.visibility = View.VISIBLE
            }else{
                edit_phone_number = false
                binding.imgEditPhoneNumber.setImageResource(R.drawable.ic_baseline_edit_24)
                binding.llPhoneNumber.visibility = View.GONE
            }
            if(checkedit()){
                binding.btEdit.visibility = View.VISIBLE
            }else{
                binding.btEdit.visibility = View.GONE
            }
        }

        binding.btEdit.setOnClickListener {
            if(edit_avatar){
                account.imageUrl = image
            }
            if(edit_email){
                account.email = binding.edtEmail.text.toString()
            }
            if(edit_phone_number){
                account.phoneNumber = "+84${binding.edtPhoneNumber.text.toString()}"
            }
            if(edit_name){
                account.firstName = binding.edtFirstname.text.toString()
                account.lastName = binding.edtLastName.text.toString()
            }

            updateAccount()
        }
    }

    private fun updateAccount() {
//        userViewModel.updateAccount(account).observe(this) {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        resource.data?.let { result ->
//                            val i: InputStream = result.errorBody()!!.byteStream()
//                            val r = BufferedReader(InputStreamReader(i))
//                            val errorResult = StringBuilder()
//                            var line: String?
//                            try {
//                                while (r.readLine().also { line = it } != null) {
//                                    errorResult.append(line).append('\n')
//                                }
//                                Log.d("API_RESPONSE_ERROR_BODY", errorResult.toString())
//                            } catch (e: IOException) {
//                                e.printStackTrace()
//                            }
//                            val resultRegisterAccount: ResultRegisterAccount = Gson().fromJson(errorResult.toString(), ResultRegisterAccount::class.java)
//                            Log.d("test: ", "${resultRegisterAccount.title}")
//                            val successStr: String = "End of input at line 1 column 1 path"
//                            if(resultRegisterAccount.title.contains(successStr)){
//                                Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show()
//                            }else{
//                                Toast.makeText(this, "${resultRegisterAccount.title}", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//
//                    }
//                    Status.ERROR -> {
//                        Log.d("TAG", "error: ${it.message}")
//                        Toast.makeText(this, "Có lỗi khi thực hiện!", Toast.LENGTH_SHORT).show()
//                    }
//                    Status.LOADING -> {
//                    }
//                }
//            }
//        }
        userViewModel.updateForUser(account).observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { types ->
                            Log.d("TAG", "updateAccount: ${account.imageUrl}")
                            Toast.makeText(this, "update thành công", Toast.LENGTH_SHORT).show()
                            finish()
//                            refreshData()
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

    private fun checkedit(): Boolean {
        if(edit_avatar){
            return true
        }
        if(edit_email){
            return true
        }
        if(edit_phone_number){
            return true
        }
        if(edit_name){
            return true
        }
        return false
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
            binding.imgAvatar.setImageURI(data?.data)
            val uri: Uri? = data?.data
            val realPathUtil: RealPathUtil = RealPathUtil()

            var realPath: String? = realPathUtil.getRealPath(this, uri!!)
            Log.d("TAG", "onActivityResult: $realPath")
            val file: File = File(realPath)

            GlobalScope.launch {
                val uploadFile = amazonClient.uploadFile(file)
                Log.d("TAG", "image: $uploadFile")
                image = uploadFile.toString()
                edit_avatar = true
                binding.imgEditImage.setImageResource(R.drawable.ic_baseline_close_24)
                withContext(Dispatchers.Main){
                    if(checkedit()){
                        Log.d("TAG", "day di")
                        binding.btEdit.visibility = View.VISIBLE
                    }else{
                        binding.btEdit.visibility = View.GONE
                    }
                }
//                if(checkedit()){
//                    Log.d("TAG", "day di")
//                    binding.btEdit.visibility = View.VISIBLE
//                }else{
//                    binding.btEdit.visibility = View.GONE
//                }
            }
        }
    }

    private fun setUserData() {
        if(account.imageUrl.equals("")){
            Picasso.get().load("https://drivinglicence.s3.us-west-2.amazonaws.com/avatar-default.png").into(binding.imgAvatar)
        }else{
            Picasso.get().load(account.imageUrl).into(binding.imgAvatar)
        }
        binding.tvName.setText("${account.firstName} ${account.lastName}")
        binding.tvEmail.setText(account.email)
        binding.tvUserName.setText(account.login)
        binding.tvPhoneNumber.setText(account.phoneNumber)
    }
}