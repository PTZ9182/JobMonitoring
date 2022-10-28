package org.d3ifcool.jobmonitoring.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentDivisiBinding

class DivisiFragment : Fragment() {

    private var _binding: FragmentDivisiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDivisiBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonTambahDivisi.setOnClickListener {
            it.findNavController().navigate(R.id.action_divisiFragment_to_tambahDivisiFragment)
        }


    }
}