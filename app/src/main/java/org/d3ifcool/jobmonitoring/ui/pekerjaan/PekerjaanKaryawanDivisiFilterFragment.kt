package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanKaryawanDivisiFilterFragmentBinding
import org.d3ifcool.jobmonitoring.model.Preference

class PekerjaanKaryawanDivisiFilterFragment : Fragment(){

    private var _binding: FragmentPekerjaanKaryawanDivisiFilterFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPekerjaanKaryawanDivisiFilterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutPekerjaanFilter.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutPekerjaanFilter.isRefreshing = false
        }


        binding.pfkCollSelesai.setOnClickListener {
            findNavController().navigate(R.id.action_pekerjaanKaryawanDivisiFilterFragment_to_pekerjaanKaryawanDivisiFragment)
        }

        binding.pfkCollSemuastatus.setOnClickListener {
            findNavController().navigate(R.id.action_pekerjaanKaryawanDivisiFilterFragment_to_pekerjaanFilterDivisiFragment)
        }

    }
}