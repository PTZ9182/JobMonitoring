@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.presensi

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class PresensiKaryawanFragment : Fragment() {

    private var _binding: FragmentPresensiKaryawanBinding? = null
    private val binding get() = _binding!!

    val storage = Firebase.storage
    lateinit var auth: FirebaseAuth
    val database = Firebase.database
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPresensiKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)

        binding.layoutKaryawanFragment.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutKaryawanFragment.isRefreshing = false
        }

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idUser = pref.prefidkaryawanpresensi
        val idPerusahaan = user!!.uid
        val tanggal = pref.preftanggalpresensi

        nDialog.show()
        val storageRefff =
            storage.getReference("images").child("Karyawan").child(idUser!!).child("profil")
        storageRefff.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imgProfilPerusahaan.setImageBitmap(bitmap)
            nDialog.cancel()
        }.addOnFailureListener {
            nDialog.cancel()
        }
        nDialog.show()
        val storageReff =
            storage.getReference("images").child("Presensi").child(idPerusahaan).child(idUser).child(tanggal!!)
        storageReff.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imageSelfie.setImageBitmap(bitmap)
            nDialog.cancel()
        }.addOnFailureListener {
            nDialog.cancel()
        }

        val dbReff = database.getReference("Karyawan").child(idPerusahaan).orderByChild("id").equalTo(pref.prefidkaryawanpresensi)
        dbReff.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        binding.namaProfilPerusahaan.text = datas!!.namaKaryawan
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        binding.jam.text = pref.prefwaktumasukpresensi
        binding.jamkeluar.text = pref.prefwaktukeluarpresensi
        binding.ket.text = pref.prefketeranganpresensi
        binding.tanggal.text = pref.preftanggalpresensi

    }
}