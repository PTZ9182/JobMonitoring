package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.data.ListKaryawanPekerjaanModel
import org.d3ifcool.jobmonitoring.data.PekerjaanModel
import org.d3ifcool.jobmonitoring.databinding.FragmentEditPekerjaanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPekerjaanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val api by lazy { ApiRetrofit().endpoint }
    private var _binding: FragmentEditPekerjaanBinding? = null
    private val binding get() = _binding!!

    private var listIdKaryawana = ArrayList<Int>()
    private var listKaryawana = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditPekerjaanBinding.inflate(inflater, container, false)
        setFL()
        return binding.root
    }

    private fun setFL() {
        setFragmentResultListener("nama_pekerjaan") { requestKey, bundle ->
            val result = bundle.getString("nama_pekerjaan")
            binding.textNamaDalamFormPekerjaan.setText(result)
        }
        setFragmentResultListener("deskripsi") { requestKey, bundle ->
            val result = bundle.getString("deskripsi")
            binding.textDeskripsiDalamFormPekerjaan.setText(result)
        }
        setFragmentResultListener("karyawan") { requestKey, bundle ->
            val result = bundle.getString("karyawan")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editPekerjaan()
        listKaryawan()

        binding.layoutEditPekerjaan.setOnRefreshListener {
            binding.layoutEditPekerjaan.isRefreshing = false
        }

    }

    private fun editPekerjaan() {
        setFragmentResultListener("id") { requestKey, bundle ->
            val result = bundle.getInt("id")
            binding.buttonEditPekerjaan.setOnClickListener {
                api.updatePekerjaan(result,
                    binding.textNamaDalamFormPekerjaan.text.toString(),
                    binding.textDeskripsiDalamFormPekerjaan.text.toString(),binding.listKaryawan.getSelectedItem().toString())
                    .enqueue(object : Callback<PekerjaanModel> {
                        override fun onResponse(
                            call: Call<PekerjaanModel>,
                            response: Response<PekerjaanModel>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "Perubahan Berhasil",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_editPekerjaanFragment_to_pekerjaanFragment)
                            } else
                                Toast.makeText(
                                    context, "Perubahan Gagal",
                                    Toast.LENGTH_LONG
                                ).show()
                        }

                        override fun onFailure(call: Call<PekerjaanModel>, t: Throwable) {
                            Toast.makeText(
                                context, "Perubahan Gagal",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
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

                    binding.listKaryawan.onItemSelectedListener = this@EditPekerjaanFragment
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
