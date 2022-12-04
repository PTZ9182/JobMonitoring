package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentHomePerusahaanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class HomePerusahaanFragment : Fragment() {

    private var _binding: FragmentHomePerusahaanBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference

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
        val context : Context
        context = requireActivity()
        pref = Preference(context)

        val user = Firebase.auth.currentUser

        if (user != null) {
            binding.namaProfilPerusahaan.text = user.displayName
        } else {
            binding.namaProfilPerusahaan.text = "Gagal Login"
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_homePerusahaanFragment_to_praLoginFragment)
        }

        binding.homePerusahaan.setOnRefreshListener {
            binding.homePerusahaan.isRefreshing = false
        }

        binding.coliderMenuDivisi.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_divisiFragment)
        }
        binding.coliderMenuPekerjaan.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_pekerjaanFragment)
        }

        binding.coliderMenuKaryawan.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_karyawanFragment)
        }
        binding.coliderMenuPresensi.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_presensiFragment)
        }
        binding.coliderMenuPengaturan.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_pengaturanFragment)
        }
    }
}