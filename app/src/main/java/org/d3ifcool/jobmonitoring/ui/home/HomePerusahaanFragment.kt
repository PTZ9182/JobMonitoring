package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentHomePerusahaanBinding
import org.d3ifcool.jobmonitoring.model.Preference


class HomePerusahaanFragment : Fragment() {

    private var _binding: FragmentHomePerusahaanBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference
    val database = Firebase.database
    val storage = Firebase.storage

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

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                binding.namaProfilPerusahaan.text = profile.displayName
            }
        }
        val id = user!!.uid
        val storageRef = storage.getReference("images").child("perusahaan").child(id).child("profil")
        storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imgProfilPerusahaan.setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}