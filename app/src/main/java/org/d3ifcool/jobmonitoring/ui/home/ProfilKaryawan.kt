package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentProfilKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.Preference

class ProfilKaryawan : Fragment() {

    private var _binding: FragmentProfilKaryawanBinding? = null
    private val binding get() = _binding!!

    val storage: FirebaseStorage = Firebase.storage

    private lateinit var pref: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfilKaryawanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutProfilHomeKaryawan.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutProfilHomeKaryawan.isRefreshing = false
        }

        binding.hppkNama.text = pref.prefnamakaryawan
        binding.hppkAlamatprofil.text = pref.prefalamatkaryawan
        binding.hppkTextEmail.text = pref.prefemailkaryawan
        binding.hppkTextTanggal.text = pref.preftanggallahirkaryawan
        binding.hppkTextJk.text = pref.prefjeniskelaminkaryawan
        binding.hppkTextNohp.text = pref.prefnohpkaryawan
        binding.hppkTextAlamat.text = pref.prefalamatkaryawan

        val database = Firebase.database
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef =
            database.getReference("Divisi").child(idPerusahaan!!).orderByChild("id").equalTo(pref.prefiddivisikaryawan)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        binding.hppkTextDivisi.text = datas!!.divisi
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        val storageRef = storage.getReference("images").child("Karyawan").child(pref.prefidkaryawan!!).child("profil")
        storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.hppkImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
        }
    }
}