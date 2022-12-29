package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentProfilPerusahaanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class ProfilePerusahaan : Fragment(){
    private var _binding: FragmentProfilPerusahaanBinding? = null
    private val binding get() = _binding!!

    val storage: FirebaseStorage = Firebase.storage

    private lateinit var pref: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfilPerusahaanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutProfilPerusahaan.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutProfilPerusahaan.isRefreshing = false
        }

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                binding.hppNama.text = profile.displayName
                binding.hppTextEmail.text = profile.email
                binding.hppTextNohp.text = pref.prefnohpperusahaan
                binding.hppTextAlamat.text = pref.prefalamatperusahaan
                binding.hppAlamatprofil.text = pref.prefalamatperusahaan
            }
        }
        val id = user!!.uid
        val storageRef = storage.getReference("images").child("perusahaan").child(id).child("profil")
        storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.hppImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
        }
    }
}