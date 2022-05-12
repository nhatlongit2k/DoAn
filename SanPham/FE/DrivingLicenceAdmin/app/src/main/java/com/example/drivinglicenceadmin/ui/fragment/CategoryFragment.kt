package com.example.drivinglicenceadmin.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivinglicenceadmin.R
import com.example.drivinglicenceadmin.databinding.FragmentCategoryBinding
import com.example.drivinglicenceadmin.model.ResultRegisterAccount
import com.example.drivinglicenceadmin.model.Type
import com.example.drivinglicenceadmin.showCreateDialogInputType
import com.example.drivinglicenceadmin.showUpdateDialogInputType
import com.example.drivinglicenceadmin.ui.adapter.AdapterTypeRecycler
import com.example.drivinglicenceadmin.viewmodel.TypeViewModel
import com.example.mvvm_retrofit.Status
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CategoryFragment : Fragment() {

    lateinit var binding: FragmentCategoryBinding

    private val typeViewModel: TypeViewModel by lazy {
        ViewModelProvider(
            this,
            TypeViewModel.TypeViewModelFactory(requireActivity().application)
        ).get(
            TypeViewModel::class.java
        )
    }

    var typeList: ArrayList<Type> = ArrayList()

    private val adapterTypeRecycler: AdapterTypeRecycler by lazy {
        AdapterTypeRecycler(requireContext(), typeList, onItemClick, onDeleteClick, onEditClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        binding.rvTypeLicence.setHasFixedSize(true)
        binding.rvTypeLicence.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTypeLicence.adapter = adapterTypeRecycler

        binding.slTypeLicence.setOnRefreshListener {
            refreshData()
        }
        refreshData()

        binding.btnAddNew.setOnClickListener {
            context?.showCreateDialogInputType(onDialogAddOkClick)
        }

        return binding.root
    }

    private fun refreshData() {
        typeViewModel.getTypesFromApi().observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.slTypeLicence.isRefreshing = false
                        resource.data?.let { types ->
                            typeList.clear()
                            typeList.addAll(types)
                            adapterTypeRecycler.setTypes(typeList)
                        }

                    }
                    Status.ERROR -> {
                        Log.d("TAG", "error: ${it.message}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.slTypeLicence.isRefreshing = false
                    }
                    Status.LOADING -> {
                        binding.slTypeLicence.isRefreshing = true
                    }
                }
            }
        }
    }

    private val onItemClick: (Type) -> Unit = {

    }

    private val onDeleteClick: (Type) -> Unit = {
        showDialogDelete(it)
    }

    private val onEditClick: (Type) -> Unit = {
        context?.showUpdateDialogInputType(it, onDialogUpdateOkClick)
    }

    fun showDialogDelete(type: Type){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Hệ thống!")
        builder.setMessage("Bạn có chắc muốn xóa loại đề này?")
        builder.setIcon(R.drawable.ic_baseline_delete_24)

        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, which ->
            deleteType(type)
            dialogInterface.dismiss()
        })

        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, which ->
            dialogInterface.dismiss()
        })

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun deleteType(type: Type){
        Log.d("TAG", "delete click: ")
        val id = type.id
        if (id != null) {
            typeViewModel.deleteTypeLicenceOffApi(id).observe(viewLifecycleOwner) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { result ->
                                Log.d("TAG", "deleteType: ${result.messange}")
                                Toast.makeText(requireContext(), "${result.messange}", Toast.LENGTH_SHORT).show()
                                refreshData()
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

    private val onDialogAddOkClick: (Type) -> Unit = {
        typeViewModel.createTypeLicenceToApi(it).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { types ->
                            Log.d("TAG", "Thêm thành công:")
                            refreshData()
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

    private val onDialogUpdateOkClick: (Type) -> Unit = {
        val id = it.id
        Log.d("TAG", "id : $id")
        if (id != null) {
            typeViewModel.updateTypeLicenceToApi(id,it).observe(viewLifecycleOwner) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { types ->
                                Log.d("TAG", "Sửa thành công:")
                                refreshData()
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
}