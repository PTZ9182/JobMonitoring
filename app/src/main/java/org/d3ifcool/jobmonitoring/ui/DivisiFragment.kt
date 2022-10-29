package org.d3ifcool.jobmonitoring.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.Api.ApiRetrofit
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.AdapterDivisi
import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.databinding.FragmentDivisiBinding
import org.d3ifcool.jobmonitoring.databinding.FragmentEditDivisiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DivisiFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var divisiAdapter: AdapterDivisi
    private var _binding: FragmentDivisiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDivisiBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        getNode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle ?) {
        super.onViewCreated(view, savedInstanceState)
        divisiAdapter = AdapterDivisi(arrayListOf(),requireContext())
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = divisiAdapter
            setHasFixedSize(true)
        }

        binding.buttonTambahDivisi.setOnClickListener {
            it.findNavController().navigate(R.id.action_divisiFragment_to_tambahDivisiFragment)
        }
    }

    private fun getNode() {
        api.data().enqueue(object : Callback<DivisiModel> {
            override fun onResponse(call: Call<DivisiModel>, response: Response<DivisiModel>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!.tabel_divisi
                    divisiAdapter.setData(listData)
                    //listData.forEach{" Divisi ${it.divisi}"}
                }
            }

            override fun onFailure(call: Call<DivisiModel>, t: Throwable) {
                Log.e("MainActivity", t.toString())
            }
        })
    }
}