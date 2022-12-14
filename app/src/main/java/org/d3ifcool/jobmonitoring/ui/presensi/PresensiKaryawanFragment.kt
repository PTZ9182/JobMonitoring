package org.d3ifcool.jobmonitoring.ui.presensi

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiKaryawanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class PresensiKaryawanFragment : Fragment() {

    private var _binding: FragmentPresensiKaryawanBinding? = null
    private val binding get() = _binding!!

    val storage = Firebase.storage
    lateinit var auth: FirebaseAuth
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPresensiKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        var idUser = pref.prefiduserpresensi
        var idPerusahaan = user!!.uid
        var tanggal = pref.preftanggalpresensi

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
            storage.getReference("images").child("Presensi").child(idPerusahaan!!).child(idUser!!).child(tanggal!!)
        storageReff.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imageSelfie.setImageBitmap(bitmap)
            nDialog.cancel()
        }.addOnFailureListener {
            nDialog.cancel()
        }

        binding.namaProfilPerusahaan.text = pref.prefnamauserpresensi
        binding.jam.text = pref.prefwaktupresensi
        binding.ket.text = pref.prefketeranganpresensi
        binding.tanggal.text = pref.preftanggalpresensi

    }
}