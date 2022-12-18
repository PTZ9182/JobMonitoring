package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentProfilHomekaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.Preference

class ProfileHomeKaryawan : Fragment() {
    private var _binding: FragmentProfilHomekaryawanBinding? = null
    private val binding get() = _binding!!

    val storage: FirebaseStorage = Firebase.storage
    val database = Firebase.database
    private lateinit var pref: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfilHomekaryawanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.hppkNama.text = pref.prefnamauser
        binding.hppkAlamatprofil.text = pref.prefalamatuser
        binding.hppkTextEmail.text = pref.prefemailuser
        binding.hppkTextTanggal.text = pref.preftanggallahiruser
        binding.hppkTextJk.text = pref.prefjeniskelaminuser
        binding.hppkTextNohp.text = pref.prefnohpuser
        binding.hppkTextAlamat.text = pref.prefalamatuser

        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef =
            database.getReference("Divisi").child(idPerusahaan!!).orderByChild("id").equalTo(pref.prefiddivisiuser)
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
        val storageRef = storage.getReference("images").child("Karyawan").child(pref.prefiduser!!).child("profil")
        storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.hppkImg.setImageBitmap(bitmap)
        }.addOnFailureListener {
        }
        binding.layoutProfilHomeKaryawan.setOnRefreshListener {
            binding.layoutProfilHomeKaryawan.isRefreshing = false
        }
    }
}