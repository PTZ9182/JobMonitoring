package org.d3ifcool.jobmonitoring.ui.divisi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.data.SubmitDivisiModel
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahDivisiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahDivisiFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private var _binding: FragmentTambahDivisiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentTambahDivisiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tambahDivisi()
    }

    private fun tambahDivisi() {
        binding.buttonTambahDivisiForm.setOnClickListener {
            if (binding.textNamaDivisiDalamForm.text.isNotEmpty()) {
                api.create(binding.textNamaDivisiDalamForm.text.toString())
                    .enqueue(object : Callback<SubmitDivisiModel> {
                        override fun onResponse(
                            call: Call<SubmitDivisiModel>,
                            response: Response<SubmitDivisiModel>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    activity, "Divisi Berhasil Dibuat",
                                    Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_tambahDivisiFragment_to_divisiFragment)
                            } else
                                Toast.makeText(
                                    activity, "Gagal",
                                    Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<SubmitDivisiModel>, t: Throwable) {

                        }
                    })
            } else {
                Toast.makeText(
                    activity, "Nama Divisi Tidak Boleh Kosong",
                    Toast.LENGTH_LONG).show()
            }

        }
    }
}