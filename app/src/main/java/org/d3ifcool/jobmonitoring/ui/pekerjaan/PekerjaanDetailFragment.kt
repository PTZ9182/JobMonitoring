package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanDetailBinding
import org.d3ifcool.jobmonitoring.model.Preference

class PekerjaanDetailFragment : Fragment() {

    private var _binding: FragmentPekerjaanDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.ddpIsidivisi.text = pref.prefdivisipekerjaan
        binding.ddpIsinama.text = pref.prefkaryawanpekerjaan
        binding.ddpIsinamapekerjaan.text = pref.prefnamapekerjaan
        binding.ddpIsidesc.text = pref.prefdeskripsipekerjaan
        binding.ddpTextprogress.text = pref.prefprogresspekerjaan.toString()
        val status = pref.prefstatuspekerjaan
        if (status == "1"){
            binding.ddpIsistatus.text = "Selesai"
        } else {
            binding.ddpIsistatusbelumseselai.text = "Belum Selesai"
        }

        binding.layoutPekerjaanDetail.setOnRefreshListener {
            binding.layoutPekerjaanDetail.isRefreshing = false
        }

    }
}