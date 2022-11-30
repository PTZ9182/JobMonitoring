package org.d3ifcool.jobmonitoring.ui.presensi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiBinding

class PresensiFragment : Fragment() {

    private var _binding: FragmentPresensiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPresensiBinding.inflate(inflater, container, false)
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

        binding.layoutPresensiPerusahaan.setOnRefreshListener {
            binding.layoutPresensiPerusahaan.isRefreshing = false
        }
        binding.ppButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_presensiFragment_to_tambahPresensiFragment)
        }
    }
}