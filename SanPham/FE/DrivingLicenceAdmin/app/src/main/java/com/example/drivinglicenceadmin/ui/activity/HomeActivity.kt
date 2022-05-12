package com.example.drivinglicenceadmin.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.databinding.ActivityHomeBinding
import com.example.drivinglicenceadmin.model.Question
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.mvvm_retrofit.Status
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {

    var token: String =""
    lateinit var account: User
    lateinit var accountMenu: PopupMenu
    companion object{
        var recreate = false
    }

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(application)
        ).get(
            UserViewModel::class.java
        )
    }

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = intent.getStringExtra(TOKEN).toString()
        account = intent.getSerializableExtra(ACCOUNT) as User
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragment_container)
        binding.bottomNavigationView.setupWithNavController(navController)
        setUpAccountMenu()
        if(account.imageUrl.equals("")){
            Picasso.get().load("https://drivinglicence.s3.us-west-2.amazonaws.com/avatar-default.png").into(binding.imgTbAvatar)
        }else{
            Picasso.get().load(account.imageUrl).into(binding.imgTbAvatar)
        }
        binding.imgTbAvatar.setOnClickListener {
            accountMenu.show()
        }

        if(recreate){
            getAccount()
            recreate = false
        }
    }

    private fun setUpAccountMenu(){
        accountMenu = PopupMenu(this, binding.imgTbAvatar)
        accountMenu.menu.add(Menu.NONE, 0, 0, "Thông tin cá nhân")
        accountMenu.menu.add(Menu.NONE, 1, 1, "Đổi mật khẩu")
        accountMenu.menu.add(Menu.NONE, 2, 2, "Đăng xuất")

        accountMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            when(id){
                0 -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(TOKEN, token)
                    intent.putExtra(ACCOUNT, account)
                    intent.putExtra("user", "this")
                    startActivityForResult(intent, 100)
                }
                1 -> {
                    this.showChangePasswordDialog { oldPassword, newPassword ->
                        userViewModel.changeAccountPassword("Bearer ${token}", oldPassword, newPassword).observe(this) {
                            it?.let { resource ->
                                when (resource.status) {
                                    Status.SUCCESS -> {
                                        resource.data?.let { resut ->
                                            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
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
                2 -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            false
        }
    }

//    private fun getAccount() {
//        userViewModel.getAccount("Bearer ${token}").observe(this) {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        resource.data?.let { resut ->
//                            acoount = resut
//                            if(!acoount.imageUrl.equals("")){
//                                Picasso.get().load(acoount.imageUrl).into(binding.tbHome.imgAvatar)
//                            }
//                        }
//
//                    }
//                    Status.ERROR -> {
//                        Log.d("TAG", "error: ${it.message}")
//                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    Status.LOADING -> {
//                    }
//                }
//            }
//        }
//    }

    private fun getAccount() {
        userViewModel.getAccount("Bearer ${token}").observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { resut ->
                            account = resut
                            if(account.imageUrl.equals("")){
                                Picasso.get().load("https://drivinglicence.s3.us-west-2.amazonaws.com/avatar-default.png").into(binding.imgTbAvatar)
                            }else{
                                Picasso.get().load(account.imageUrl).into(binding.imgTbAvatar)
                            }
                        }

                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(this, "Có lỗi sảy ra khi kết nối đến sever!", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate = true
        recreate()
    }
}