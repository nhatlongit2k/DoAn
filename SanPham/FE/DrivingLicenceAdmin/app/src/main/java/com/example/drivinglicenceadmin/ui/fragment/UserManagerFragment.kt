package com.example.drivinglicenceadmin.ui.fragment

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.ACCOUNT
import com.example.drivinglicenceadmin.TOKEN
import com.example.drivinglicenceadmin.databinding.FragmentUserManagerBinding
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceadmin.model.User
import com.example.drivinglicenceadmin.ui.activity.HomeActivity
import com.example.drivinglicenceadmin.ui.activity.NewAccountActivity
import com.example.drivinglicenceadmin.ui.activity.UserProfileActivity
import com.example.drivinglicenceadmin.ui.adapter.AdapterTypeRecycler
import com.example.drivinglicenceadmin.ui.adapter.AdapterUserRecycler
import com.example.drivinglicenceadmin.ui.adapter.SpinnerType
import com.example.drivinglicenceadmin.viewmodel.TypeViewModel
import com.example.drivinglicenceadmin.viewmodel.UserViewModel
import com.example.mvvm_retrofit.Status

class UserManagerFragment : Fragment() {

    lateinit var binding: FragmentUserManagerBinding
    lateinit var homeActivity: HomeActivity
    var userAutheSpinner: ArrayList<SpinnerType> = ArrayList<SpinnerType>()

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModel.UserViewModelFactory(requireActivity().application)
        ).get(
            UserViewModel::class.java
        )
    }

    var userList: ArrayList<User> = ArrayList()

    private val adapterUserRecycler: AdapterUserRecycler by lazy {
        AdapterUserRecycler(requireContext(), homeActivity.account, userList, onItemClick, onSwichClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserManagerBinding.inflate(inflater, container, false)
        homeActivity = activity as HomeActivity // Lay du lieu tu activity cha

        binding.rvUserLicence.setHasFixedSize(true)
        binding.rvUserLicence.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserLicence.adapter = adapterUserRecycler

        userAutheSpinner.clear()
        userAutheSpinner.add(SpinnerType(0, "Tất cả"))
        userAutheSpinner.add(SpinnerType(1, "User"))
        userAutheSpinner.add(SpinnerType(2, "Admin"))

        var spinnerAdapter: ArrayAdapter<SpinnerType> =
        ArrayAdapter<SpinnerType>(
            (activity as HomeActivity).applicationContext,
            com.example.drivinglicenceadmin.R.layout.style_spinner,
            userAutheSpinner
        )
        binding.spinnerUserAuthen.adapter = spinnerAdapter
        refreshData()

        binding.spinnerUserAuthen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(id == 0.toLong()){
//                    refreshData()
                    searchUser(binding.edtFragmentloginSearchUsername.text.toString())
                }
                if(id == 1.toLong()){
//                    getUsersWithRole("ROLE_USER", userList)
                    searchUser(binding.edtFragmentloginSearchUsername.text.toString())
                }
                if (id == 2.toLong()){
//                    getUsersWithRole("ROLE_ADMIN", userList)
                    searchUser(binding.edtFragmentloginSearchUsername.text.toString())
                }
            }

        }
        binding.slUserLicence.setOnRefreshListener {
            refreshData()
        }

        binding.btnAddNewAccount.setOnClickListener {
            val intent = Intent(activity, NewAccountActivity::class.java)
            intent.putExtra(TOKEN, homeActivity.token)
            intent.putExtra(ACCOUNT, homeActivity.account)
            startActivityForResult(intent, 1)
        }

        binding.imgUserFragmentSearch.setOnClickListener {
            searchUser(binding.edtFragmentloginSearchUsername.text.toString())
            binding.imgCancelSearch.visibility = View.VISIBLE
        }

        binding.imgCancelSearch.setOnClickListener {
//            refreshData()
            Log.d("TAG", "userAutheSpinner[binding.spinnerUserAuthen.selectedItemPosition].id: ${userAutheSpinner[binding.spinnerUserAuthen.selectedItemPosition].id}")
            when(userAutheSpinner[binding.spinnerUserAuthen.selectedItemPosition].id){
                0.toLong() -> {
                    adapterUserRecycler.setUsers(userList)
                }
                1.toLong() -> {
                    getUsersWithRole("ROLE_USER", userList)
                }
                2.toLong() -> {
                    getUsersWithRole("ROLE_ADMIN", userList)
                }
            }
            binding.imgCancelSearch.visibility = View.GONE
            binding.edtFragmentloginSearchUsername.text.clear()
        }

        return binding.root
    }

    private fun searchUser(strSearch: String) {
//        userViewModel.searchMultioption(binding.edtFragmentloginSearchUsername.text.toString()).observe(viewLifecycleOwner) {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        binding.slUserLicence.isRefreshing = false
//                        resource.data?.let { types ->
//                            userList.clear()
//                            userList.addAll(types)
//                            adapterUserRecycler.setUsers(userList)
//                            Log.d("TAG", "searchUser: ${userAutheSpinner[binding.spinnerUserAuthen.selectedItemPosition].id}")
////                            when(userAutheSpinner[binding.spinnerUserAuthen.selectedItemPosition].id){
////                                0.toLong() -> {
////                                    adapterUserRecycler.setUsers(userList)
////                                }
////                                1.toLong() -> {
//////                                    getUsersWithRole("ROLE_USER")
////                                    Toast.makeText(requireContext(), "1", Toast.LENGTH_SHORT).show()
////                                }
////                                2.toLong() -> {
//////                                    getUsersWithRole("ROLE_ADMIN")
////                                    Toast.makeText(requireContext(), "2", Toast.LENGTH_SHORT).show()
////                                }
////                            }
//                        }
//
//                    }
//                    Status.ERROR -> {
//                        Log.d("TAG", "error: ${it.message}")
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                        binding.slUserLicence.isRefreshing = false
//                    }
//                    Status.LOADING -> {
//                        binding.slUserLicence.isRefreshing = true
//                    }
//                }
//            }
//        }
        val userSearch: ArrayList<User> = ArrayList()
        for(i in 0 until userList.size){
            val name = "${userList[i].firstName} ${userList[i].lastName}"
            if(userList[i].login.contains(strSearch) || userList[i].email.contains(strSearch) || name.toLowerCase().contains(strSearch.toLowerCase())){
                userSearch.add(userList[i])
            }
        }
        when(userAutheSpinner[binding.spinnerUserAuthen.selectedItemPosition].id){
            0.toLong() -> {
                Log.d("TAG", "0: ")
                adapterUserRecycler.setUsers(userSearch)
            }
            1.toLong() -> {
                getUsersWithRole("ROLE_USER", userSearch)
                Toast.makeText(requireContext(), "1", Toast.LENGTH_SHORT).show()
            }
            2.toLong() -> {
                getUsersWithRole("ROLE_ADMIN", userSearch)
                Toast.makeText(requireContext(), "2", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUsersWithRole(role: String, userSearch: ArrayList<User>){
        val userWithRole: ArrayList<User> = ArrayList()
        var sizeAdapter: Int = userSearch.size
        for(i in 0 until sizeAdapter){
            if(userSearch[i].authorities.contains(role)){
                userWithRole.add(userSearch[i])
            }
        }
        adapterUserRecycler.setUsers(userWithRole)
    }

    private fun refreshData() {
        userViewModel.getAllUserFormApi("Bearer ${homeActivity.token}").observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.slUserLicence.isRefreshing = false
                        resource.data?.let { types ->
                            userList.clear()
                            userList.addAll(types)
                            adapterUserRecycler.setUsers(userList)
                        }

                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.slUserLicence.isRefreshing = false
                    }
                    Status.LOADING -> {
                        binding.slUserLicence.isRefreshing = true
                    }
                }
            }
        }
    }

    private val onItemClick: (User) -> Unit = {
        val intent = Intent(activity, UserProfileActivity::class.java)
        intent.putExtra(TOKEN, homeActivity.token)
        intent.putExtra(ACCOUNT, it)
        intent.putExtra("user", "user")
        startActivity(intent)
    }

    private val onSwichClick: (User) -> Unit = {
        userViewModel.updateUser("Bearer ${homeActivity.token}", it).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { types ->
//                            refreshData()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivityResult: $requestCode")
        refreshData()
    }
}