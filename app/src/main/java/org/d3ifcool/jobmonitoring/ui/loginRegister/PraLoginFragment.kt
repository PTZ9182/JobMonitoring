package org.d3ifcool.jobmonitoring.ui.loginRegister

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentPraloginBinding
import org.d3ifcool.jobmonitoring.model.Preference

class PraLoginFragment : Fragment() {

    private var _binding: FragmentPraloginBinding? = null
    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth
    private lateinit var pref: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPraloginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reloadPerusahaan();
            Log.i("perusahaan : ", currentUser.toString());
        } else if (pref.prefstatus) {
            reloadKaryawan()
            Log.i("karyawan", "Login");
        } else {
            Log.i("karyawan", "Gagal Login");

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.praLoginPragment.setOnRefreshListener {
            binding.praLoginPragment.isRefreshing = false
        }

        binding.dpButtonPerusahaan.setOnClickListener {
            it.findNavController().navigate(R.id.action_praLoginFragment_to_loginPerusahaanFragment)
        }
        binding.dpButtonKaryawan.setOnClickListener {
            it.findNavController().navigate(R.id.action_praLoginFragment_to_loginKaryawanFragment)
        }
        binding.plRegis.setOnClickListener {
            it.findNavController().navigate(R.id.action_praLoginFragment_to_registerFragment)
        }
    }

    private fun reloadKaryawan(){
        findNavController().navigate(R.id.action_praLoginFragment_to_homeKaryawanFragment)
    }

    private fun reloadPerusahaan(){
        findNavController().navigate(R.id.action_praLoginFragment_to_homePerusahaanFragment)
    }
}