package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.data.ListKaryawanPekerjaanModel
import org.d3ifcool.jobmonitoring.data.PekerjaanModel
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahPekerjaanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahPekerjaanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val api by lazy { ApiRetrofit().endpoint }
    private var _binding: FragmentTambahPekerjaanBinding?= null
    private val binding get() = _binding!!

    private var listIdKaryawana = ArrayList<Int>()
    private var listKaryawana = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTambahPekerjaanBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tambahPekerjaan()
        listKaryawan()

        binding.layoutTambahPekerjaan.setOnRefreshListener {
            binding.layoutTambahPekerjaan.isRefreshing = false
        }
    }

    private fun tambahPekerjaan() {
        binding.buttonTambahPekerjaan.setOnClickListener {

            var perusahaan =
                if (binding.textNamaDalamFormPekerjaan.text.isNotEmpty()) {
                    api.createPekerjaan(binding.textNamaDalamFormPekerjaan.text.toString(),
                        binding.textDeskripsiDalamFormPekerjaan.text.toString(),"telkom",binding.listKaryawan.getSelectedItem().toString())
                        .enqueue(object : Callback<PekerjaanModel> {
                            override fun onResponse(
                                call: Call<PekerjaanModel>,
                                response: Response<PekerjaanModel>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        activity, "Pekerjaan Berhasil Dibuat",
                                        Toast.LENGTH_LONG).show()
                                    findNavController().navigate(R.id.action_tambahPekerjaanFragment_to_pekerjaanFragment)
                                } else
                                    Toast.makeText(
                                        activity, "Gagal",
                                        Toast.LENGTH_LONG).show()
                            }

                            override fun onFailure(call: Call<PekerjaanModel>, t: Throwable) {

                            }
                        })
                } else {
                    Toast.makeText(
                        activity, "Nama Pekerjaan Tidak Boleh Kosong",
                        Toast.LENGTH_LONG).show()
                }

        }
    }

    private fun listKaryawan(){
        api.listKaryawan().enqueue(object : Callback<ListKaryawanPekerjaanModel> {
            override fun onResponse(call: Call<ListKaryawanPekerjaanModel>, response: Response<ListKaryawanPekerjaanModel>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!.list_karyawan_pekerjaan

                    listData?.forEach{
                        listIdKaryawana.add(it.id)
                        listKaryawana.add(it.nama_karyawan)
                    }

                    binding.listKaryawan.onItemSelectedListener = this@TambahPekerjaanFragment
                    val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,listKaryawana)
                    binding.listKaryawan.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ListKaryawanPekerjaanModel>, t: Throwable) {
                Toast.makeText(
                    activity, "Gagal Memuat",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemAtPosition(p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
