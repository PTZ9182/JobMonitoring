package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanDetailBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class PekerjaanDetailFragment : Fragment() {

    private var _binding: FragmentPekerjaanDetailBinding? = null
    private val binding get() = _binding!!
    val database = Firebase.database
    private lateinit var pref: Preference
    val storage = Firebase.storage
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val iddivisi = pref.prefiddivisipekerjaan
        val dbRef =
            database.getReference("Divisi").child(idPerusahaan!!).orderByChild("id").equalTo(iddivisi)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        binding.ddpIsidivisi.text = datas!!.divisi
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        val idkaryawan = pref.prefidkaryawanpekerjaan
        val dbReff = database.getReference("Karyawan").child(idPerusahaan!!).orderByChild("id").equalTo(idkaryawan)
        dbReff.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        binding.ddpIsinama.text = datas!!.namaKaryawan
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        binding.ddpIsinamapekerjaan.text = pref.prefnamapekerjaan
        binding.ddpIsidesc.text = pref.prefdeskripsipekerjaan
        binding.ddpTextprogress.text = pref.prefprogresspekerjaan.toString()
        val status = pref.prefstatuspekerjaan
        if (status == "1"){
            binding.ddpIsistatus.text = "Selesai"
        } else {
            binding.ddpIsistatusbelumseselai.text = "Belum Selesai"
        }

        val idPekerjaan = pref.prefidpekerjaan
        val idKaryawan = pref.prefidkaryawanpekerjaan
        nDialog.show()
        val storageRefff =
            storage.getReference("images").child("Pekerjaan").child(idPerusahaan).child(idKaryawan!!).child(idPekerjaan!!).child("bukti")
        storageRefff.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imageSelfie.setImageBitmap(bitmap)
            nDialog.cancel()
        }.addOnFailureListener {
            nDialog.cancel()
        }

        binding.layoutPekerjaanDetail.setOnRefreshListener {
            binding.layoutPekerjaanDetail.isRefreshing = false
        }

    }
}