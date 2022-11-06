package org.d3ifcool.jobmonitoring.ui.karyawan


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
import org.d3ifcool.jobmonitoring.data.KaryawanModel
import org.d3ifcool.jobmonitoring.data.ListDivisiModel
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahKaryawanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahKaryawanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val api by lazy { ApiRetrofit().endpoint }
    private var _binding: FragmentTambahKaryawanBinding? = null
    private val binding get() = _binding!!

    private var listIdDivisi = ArrayList<Int>()
    private var listDivisi = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTambahKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tambahKaryawan()
        listDivisi()
        binding.layoutTambahKaryawan.setOnRefreshListener {
            binding.layoutTambahKaryawan.isRefreshing = false
        }
    }

    private fun tambahKaryawan() {
        binding.buttonSimpanDataForm.setOnClickListener {

            if (binding.textNamaKaryawanDalamForm.text.isNotEmpty() &&
                    binding.tanggallahirDalamForm.text.isNotEmpty() &&
                    binding.textAlamatKaryawanDalamForm.text.isNotEmpty() &&
                    binding.textNoHandphoneKaryawanDalamForm.text.isNotEmpty() &&
                    binding.textEmailKaryawanDalamForm.text.isNotEmpty() &&
                    binding.textPasswordKaryawanDalamForm.text.isNotEmpty()) {
                api.createKaryawan(
                    binding.textNamaKaryawanDalamForm.text.toString(),
                    binding.tanggallahirDalamForm.text.toString(),
                    binding.spJeniskelamin.getSelectedItem().toString(),
                    binding.textAlamatKaryawanDalamForm.text.toString(),
                    binding.textNoHandphoneKaryawanDalamForm.text.toString(),
                    binding.spPilihdivisi.getSelectedItem().toString(),
                    binding.textEmailKaryawanDalamForm.text.toString(),
                    binding.textPasswordKaryawanDalamForm.text.toString(),"telkom")
                    .enqueue(object : Callback<KaryawanModel> {
                        override fun onResponse(
                            call: Call<KaryawanModel>,
                            response: Response<KaryawanModel>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    activity, "Karyawan Berhasil Dibuat",
                                    Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_tambahKaryawanFragment_to_karyawanFragment)
                            } else
                                Toast.makeText(
                                    activity, "Gagal",
                                    Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<KaryawanModel>, t: Throwable) {

                        }
                    })
            } else {
                Toast.makeText(
                    activity, "Data Tidak Boleh Kosong",
                    Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun listDivisi(){
        api.listDivisi().enqueue(object : Callback<ListDivisiModel> {
            override fun onResponse(call: Call<ListDivisiModel>, response: Response<ListDivisiModel>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!.tabel_divisi

                    listData?.forEach{
                        listIdDivisi.add(it.id)
                        listDivisi.add(it.divisi)
                    }

                    binding.spPilihdivisi.onItemSelectedListener = this@TambahKaryawanFragment
                    val adapter = ArrayAdapter(requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listDivisi)
                    binding.spPilihdivisi.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ListDivisiModel>, t: Throwable) {
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