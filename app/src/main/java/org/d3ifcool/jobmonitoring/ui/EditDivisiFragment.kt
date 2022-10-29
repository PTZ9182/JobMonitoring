package org.d3ifcool.jobmonitoring.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.d3ifcool.jobmonitoring.Api.ApiRetrofit
import org.d3ifcool.jobmonitoring.databinding.FragmentEditDivisiBinding

class EditDivisiFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private val divisi by lazy { getActivity()?.getIntent()?.getStringExtra("divisi")  }
    private var _binding: FragmentEditDivisiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditDivisiBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textNamaDivisiDalamForm.setText(divisi)
    }
}