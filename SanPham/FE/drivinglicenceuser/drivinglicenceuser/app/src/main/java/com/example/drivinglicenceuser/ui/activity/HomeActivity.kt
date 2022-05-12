package com.example.drivinglicenceuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.drivinglicenceadmin.*
import com.example.drivinglicenceadmin.model.Answer
import com.example.drivinglicenceadmin.model.Exam
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.viewmodel.TypeViewModel
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.ActivityHomeBinding
import com.example.mvvm_retrofit.Status
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    var token: String =""
    lateinit var account: User
    lateinit var accountMenu: PopupMenu
    var typeList: ArrayList<Type> = ArrayList()
    companion object{
        var recreate = false
    }

    private val typeViewModel: TypeViewModel by lazy {
        ViewModelProvider(
            this,
            TypeViewModel.TypeViewModelFactory(application)
        ).get(
            TypeViewModel::class.java
        )
    }

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

        token = intent.getStringExtra(TOKEN).toString()
        account = intent.getSerializableExtra(ACCOUNT) as User

//        getAccount()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(account.imageUrl.equals("")){
            Picasso.get().load("https://drivinglicence.s3.us-west-2.amazonaws.com/avatar-default.png").into(binding.imgTbAvatar)
        }else{
            Picasso.get().load(account.imageUrl).into(binding.imgTbAvatar)
        }

        setUpAccountMenu()
        binding.imgTbAvatar.setOnClickListener {
            accountMenu.show()
        }

        getTypeListToChose()
        binding.llDoExam.setOnClickListener {
            this.showSlectTypeDialog(typeList, onDialogItemClick)
        }

        binding.llLearn.setOnClickListener {
            val type = Type(15, "Khác", "Dành cho các đề ôn tập")
            val intent: Intent = Intent(this, SelectExamActivity::class.java)
            intent.putExtra(TOKEN, token)
            intent.putExtra(KEY_PASS_TYPE, type)
            intent.putExtra(ACCOUNT, account)
            startActivity(intent)
        }

        binding.llHowToLean.setOnClickListener {
            val intent: Intent = Intent(this, HowToUseActivity::class.java)
            startActivity(intent)
        }

        actionViewFliper()

        if(recreate){
            getAccount()
            recreate = false
        }
    }

    private fun actionViewFliper() {
//        val advertisements: ArrayList<String> = ArrayList()
//        advertisements.add("https://drivinglicence.s3.us-west-2.amazonaws.com/Featured_Image_Effective_Tips_Writing_Exams.jpg")
//        advertisements.add("https://drivinglicence.s3.us-west-2.amazonaws.com/khai-niem-bien-bao-giao-thong-duong-bo.jpg")
//        advertisements.add("https://drivinglicence.s3.us-west-2.amazonaws.com/Quy-trinh-thi-A1.jpeg")
//        for(i in 0 until advertisements.size){
//            val imageView: ImageView = ImageView(applicationContext)
//            Picasso.get().load(advertisements[i].toString()).into(imageView)
//            imageView.scaleType = ImageView.ScaleType.FIT_XY
//            binding.vfSlide.addView(imageView)
//        }
//        binding.vfSlide.flipInterval = 5000
//        binding.vfSlide.isAutoStart = true
//        val anim_slide_in: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_right)
//        val anim_slide_out: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_right)
//        binding.vfSlide.setInAnimation(anim_slide_in)
//        binding.vfSlide.setOutAnimation(anim_slide_out)

        var slideModels: ArrayList<SlideModel> = ArrayList()
        slideModels.add(SlideModel("https://drivinglicence.s3.us-west-2.amazonaws.com/Featured_Image_Effective_Tips_Writing_Exams.jpg", "Đề thi thử giống thật"))
        slideModels.add(SlideModel("https://drivinglicence.s3.us-west-2.amazonaws.com/khai-niem-bien-bao-giao-thong-duong-bo.jpg", "Các câu hỏi biển báo"))
        slideModels.add(SlideModel("https://drivinglicence.s3.us-west-2.amazonaws.com/Quy-trinh-thi-A1.jpeg", "Nâng cao khả năng thi"))
        binding.vfSlide.setImageList(slideModels, ScaleTypes.FIT)
    }

    private fun getTypeListToChose() {
        typeViewModel.getTypesFromApi().observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { types ->
                            typeList.clear()
                            typeList.addAll(types)
                            filterTypeList()
                        }
                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }

    private fun filterTypeList() {
        for(i in 0 until typeList.size){
            if(typeList[i].id == 15.toLong()){
                typeList.removeAt(i)
            }
        }
    }

    private val onDialogItemClick: (Type) -> Unit = {
        val intent: Intent = Intent(this, SelectExamActivity::class.java)
        intent.putExtra(TOKEN, token)
        intent.putExtra(KEY_PASS_TYPE, it)
        intent.putExtra(ACCOUNT, account)
        startActivity(intent)
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
//                    startActivity(intent)
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