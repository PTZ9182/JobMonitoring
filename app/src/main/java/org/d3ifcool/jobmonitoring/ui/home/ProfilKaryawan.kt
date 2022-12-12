package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentProfilKaryawanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class ProfilKaryawan : Fragment() {

    private var _binding: FragmentProfilKaryawanBinding? = null
    private val binding get() = _binding!!

    val storage: FirebaseStorage = Firebase.storage

    private lateinit var pref: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfilKaryawanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.hpkNama.text = pref.prefnamakaryawan
        binding.hpkAlamatprofil.text = pref.prefalamatkaryawan

        binding.hpkTextDivisi.text = pref.prefdivisikaryawan
        binding.hpkTextEmail.text = pref.prefemailkaryawan
        binding.hpkTextTanggal.text = pref.preftanggallahirkaryawan
        binding.hpkTextJk.text = pref.prefjeniskelaminkaryawan
        binding.hpkTextNohp.text = pref.prefnohpkaryawan
        binding.hpkTextAlamat.text = pref.prefalamatkaryawan

        val storageRef = storage.getReference("images").child("Karyawan").child(pref.prefidkaryawan!!).child("profil")
        storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.hpkImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
        }
        binding.layoutProfilKaryawan.setOnRefreshListener {
            binding.layoutProfilKaryawan.isRefreshing = false
        }
    }
}