package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanFilterBinding
import org.d3ifcool.jobmonitoring.model.Preference

class PekerjaanFilterFragment : Fragment(){

    private var _binding: FragmentPekerjaanFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanFilterBinding.inflate(inflater, container, false)
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

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutPekerjaanFilter.setOnRefreshListener {
            binding.layoutPekerjaanFilter.isRefreshing = false
        }
        binding.pfkDivisi.setText(pref.preffilkar)

        binding.pfkCollSelesai.setOnClickListener {
            pref.prefstatt = "1"
            findNavController().navigate(R.id.action_pekerjaanFilterFragment_to_pekerjaanFilterPekerjaanFragment)
        }
        binding.pfkCollBelumselesai.setOnClickListener {
            pref.prefstatt = "0"
            findNavController().navigate(R.id.action_pekerjaanFilterFragment_to_pekerjaanFilterPekerjaanFragment)
        }
        binding.pfkCollSemuastatus.setOnClickListener {
            pref.prefstatt = "semua"
            findNavController().navigate(R.id.action_pekerjaanFilterFragment_to_pekerjaanFilterPekerjaanFragment)
        }

    }


}
