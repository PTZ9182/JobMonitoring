package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanDetailBinding

class PekerjaanDetailFragment : Fragment() {

    private var _binding: FragmentPekerjaanDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanDetailBinding.inflate(inflater, container, false)
        set()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    private fun set() {
        setFragmentResultListener("divisi") { requestKey, bundle ->
            val result = bundle.getString("divisi")
            binding.ddpIsidivisi.setText(result)
        }
        setFragmentResultListener("karyawan") { requestKey, bundle ->
            val result = bundle.getString("karyawan")
            binding.ddpIsinama.setText(result)
        }
        setFragmentResultListener("nama_pekerjaan") { requestKey, bundle ->
            val result = bundle.getString("nama_pekerjaan")
            binding.ddpIsinamapekerjaan.setText(result)
        }
        setFragmentResultListener("deskripsi") { requestKey, bundle ->
            val result = bundle.getString("deskripsi")
            binding.ddpIsidesc.setText(result)
        }
        setFragmentResultListener("status") { requestKey, bundle ->
            val result = bundle.getString("status")
            if (result == "1"){
                binding.ddpIsistatus.text = "Selesai"
            } else
                binding.ddpIsistatusbelumseselai.text = "Belum Selesai"
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutPekerjaanDetail.setOnRefreshListener {
            binding.layoutPekerjaanDetail.isRefreshing = false
        }

    }
}