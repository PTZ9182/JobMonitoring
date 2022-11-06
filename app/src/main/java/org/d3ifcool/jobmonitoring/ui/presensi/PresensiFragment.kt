package org.d3ifcool.jobmonitoring.ui.presensi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.AdapterPresensi
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiBinding
import org.d3ifcool.jobmonitoring.model.PresensiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PresensiFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var adapterPresensi: AdapterPresensi

    private var _binding: FragmentPresensiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPresensiBinding.inflate(inflater, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        adapterPresensi = AdapterPresensi(arrayListOf())
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = adapterPresensi
            setHasFixedSize(true)

        }
        binding.layoutPresensi.setOnRefreshListener {
            getNode()
            binding.layoutPresensi.isRefreshing = false
        }
        binding.buttonTambahPresensi.setOnClickListener {
            it.findNavController().navigate(R.id.action_presensiFragment_to_tambahPresensiFragment)
        }
        binding.jumlahPresensi.setOnClickListener{
            it.findNavController().navigate(R.id.action_presensiFragment_to_presensiKaryawanFragment)
        }
    }

    private fun getNode() {
        api.dataPresensi().enqueue(object : Callback<PresensiModel> {
            override fun onResponse(call: Call<PresensiModel>, response: Response<PresensiModel>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!.tabel_presensi

                    adapterPresensi.setData(listData)
                    binding.jumlahPresensi.text = listData.size.toString()
                }
            }

            override fun onFailure(call: Call<PresensiModel>, t: Throwable) {
                Toast.makeText(
                    activity, "Gagal Memuat",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}