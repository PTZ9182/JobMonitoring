package org.d3ifcool.jobmonitoring.ui.karyawan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.data.KaryawanModel
import org.d3ifcool.jobmonitoring.data.ListDivisiModel
import org.d3ifcool.jobmonitoring.databinding.FragmentEditKaryawanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditKaryawanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val api by lazy { ApiRetrofit().endpoint }
    private var _binding: FragmentEditKaryawanBinding? = null
    private val binding get() = _binding!!

    private var listIdDivisi = ArrayList<Int>()
    private var listDivisi = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditKaryawanBinding.inflate(inflater, container, false)
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
        setFL()
        listDivisi()
        editKaryawan()
        binding.layoutEditKaryawan.setOnRefreshListener {
            binding.layoutEditKaryawan.isRefreshing = false
        }

        binding.gantiPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_editKaryawanFragment_to_gantiPasswordKaryawanFragment)
        }
    }

    private fun setFL() {
        setFragmentResultListener("nama_karyawan") { requestKey, bundle ->
            val result = bundle.getString("nama_karyawan")
            binding.textNamaKaryawanDalamForm.setText(result)

        }
        setFragmentResultListener("tanggal_lahir") { requestKey, bundle ->
            val result = bundle.getString("tanggal_lahir")
            binding.tanggallahirDalamForm.setText(result)
        }
        setFragmentResultListener("jenis_kelamin") { requestKey, bundle ->
            val result = bundle.getString("jenis_kelamin")
        }
        setFragmentResultListener("alamat") { requestKey, bundle ->
            val result = bundle.getString("alamat")
            binding.textAlamatKaryawanDalamForm.setText(result)
        }
        setFragmentResultListener("no_hp") { requestKey, bundle ->
            val result = bundle.getString("no_hp")
            binding.textNoHandphoneKaryawanDalamForm.setText(result)
        }
        setFragmentResultListener("divisi") { requestKey, bundle ->
            val result = bundle.getString("divisi")

        }
        setFragmentResultListener("email") { requestKey, bundle ->
            val result = bundle.getString("email")
            binding.textEmailKaryawanDalamForm.setText(result)

        }
        setFragmentResultListener("password") { requestKey, bundle ->
            val result = bundle.getString("password")
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

                    binding.spPilihdivisi.onItemSelectedListener = this@EditKaryawanFragment
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

    private fun editKaryawan() {
        setFragmentResultListener("id") { requestKey, bundle ->
            val result = bundle.getInt("id")
            binding.buttonSimpanDataForm.setOnClickListener {
                api.updateKaryawan(result,
                    binding.textNamaKaryawanDalamForm.text.toString(),
                    binding.tanggallahirDalamForm.text.toString(),
                    binding.spJeniskelamin.getSelectedItem().toString(),
                    binding.textAlamatKaryawanDalamForm.text.toString(),
                    binding.textNoHandphoneKaryawanDalamForm.text.toString(),
                    binding.spPilihdivisi.getSelectedItem().toString(),
                    binding.textEmailKaryawanDalamForm.text.toString())
                    .enqueue(object : Callback<KaryawanModel> {
                        override fun onResponse(
                            call: Call<KaryawanModel>,
                            response: Response<KaryawanModel>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "Perubahan Berhasil",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_editKaryawanFragment_to_karyawanFragment)
                            } else
                                Toast.makeText(
                                    context, "Perubahan Gagal",
                                    Toast.LENGTH_LONG
                                ).show()
                        }

                        override fun onFailure(call: Call<KaryawanModel>, t: Throwable) {
                            Toast.makeText(
                                context, "Perubahan Gagal",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }

    }
}