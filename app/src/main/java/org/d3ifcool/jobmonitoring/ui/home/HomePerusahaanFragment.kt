package org.d3ifcool.jobmonitoring.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentHomePerusahaanBinding

class HomePerusahaanFragment : Fragment() {

    private var _binding: FragmentHomePerusahaanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomePerusahaanBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homePerusahaan.setOnRefreshListener {
            binding.homePerusahaan.isRefreshing = false
        }

        binding.coliderMenuDivisi.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_divisiFragment)
        }
        binding.coliderMenuPekerjaan.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_pekerjaanFragment)
        }
    }
}