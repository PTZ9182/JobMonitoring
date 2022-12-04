package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentHomePerusahaanBinding
import org.d3ifcool.jobmonitoring.databinding.FragmentProfilKaryawanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class ProfilKaryawan : Fragment() {

    private var _binding: FragmentProfilKaryawanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfilKaryawanBinding.inflate(inflater, container, false)
        set()
        return binding.root

    }

    private fun set() {
        setFragmentResultListener("namaKaryawan") { requestKey, bundle ->
            val result = bundle.getString("namaKaryawan")
            binding.hpkNama.setText(result)
        }
        setFragmentResultListener("tanggallahir") { requestKey, bundle ->
            val result = bundle.getString("tanggallahir")
            binding.hpkTextTanggal.setText(result)
        }
        setFragmentResultListener("jenisKelamin") { requestKey, bundle ->
            val result = bundle.getString("jenisKelamin")
            binding.hpkTextJk.setText(result)
        }
        setFragmentResultListener("alamat") { requestKey, bundle ->
            val result = bundle.getString("alamat")
            binding.hpkAlamatprofil.setText(result)
            binding.hpkTextAlamat.setText(result)
        }
        setFragmentResultListener("nohandphone") { requestKey, bundle ->
            val result = bundle.getString("nohandphone")
            binding.hpkTextNohp.setText(result)
        }
        setFragmentResultListener("divisi") { requestKey, bundle ->
            val result = bundle.getString("divisi")
            binding.hpkTextDivisi.setText(result)
        }
        setFragmentResultListener("email") { requestKey, bundle ->
            val result = bundle.getString("email")
            binding.hpkTextEmail.setText(result)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutProfilKaryawan.setOnRefreshListener {
            binding.layoutProfilKaryawan.isRefreshing = false
        }
    }
}