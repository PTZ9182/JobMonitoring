package org.d3ifcool.jobmonitoring.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.d3ifcool.jobmonitoring.Api.ApiRetrofit
import org.d3ifcool.jobmonitoring.data.TambahDivisiModel
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahDivisiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahDivisiFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private var _binding: FragmentTambahDivisiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
            val text = binding.textNamaDivisiDalamForm.text.toString()
            if (binding.textNamaDivisiDalamForm.text.isNotEmpty()) {
                Log.e("TambahDivisiFragment", text)
                api.create(binding.textNamaDivisiDalamForm.text.toString())
                    .enqueue(object : Callback<TambahDivisiModel> {
                        override fun onResponse(
                            call: Call<TambahDivisiModel>,
                            response: Response<TambahDivisiModel>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    activity, "Divisi Berhasil Dibuat",
                                    Toast.LENGTH_LONG).show()
                            } else
                                Toast.makeText(
                                    activity, "Gagal",
                                    Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<TambahDivisiModel>, t: Throwable) {

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