package org.d3ifcool.jobmonitoring.ui.home

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentHomeKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class HomeKaryawanFragment : Fragment() {

    private var _binding: FragmentHomeKaryawanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val storage = Firebase.storage
    private lateinit var pref: Preference
    private val data = arrayListOf<KaryawanModel>()
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeKaryawanBinding.inflate(inflater, container, false)
        getData()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context: Context
        context = requireActivity()
        pref = Preference(context)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        nDialog.show()
        val id = pref.prefiduser
        val storageRef = storage.getReference("images").child("Karyawan").child(id!!).child("profil")
        storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.hkImgProfilKaryawan.setImageBitmap(bitmap)
            nDialog.cancel()
        }.addOnFailureListener {
            nDialog.cancel()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)

        binding.homeKaryawan.setOnRefreshListener {
            binding.homeKaryawan.isRefreshing = false
        }

        binding.hkCollPengaturan.setOnClickListener {
            findNavController().navigate(R.id.action_homeKaryawanFragment_to_pengaturanKaryawanFragment)
        }
        binding.hkCollPekerjaan.setOnClickListener {
            findNavController().navigate(R.id.action_homeKaryawanFragment_to_karyawanPekerjaanFragment)
        }
        binding.hkCollPresensi.setOnClickListener{
            findNavController().navigate(R.id.action_homeKaryawanFragment_to_tambahPresensiKaryawanFragment)
        }
        binding.hkImgProfilKaryawan.setOnClickListener {
            findNavController().navigate(R.id.action_homeKaryawanFragment_to_profileHomeKaryawan)
        }
    }

    private fun getData() {
        val context: Context
        context = requireActivity()
        pref = Preference(context)
        val dbRef = database.getReference("Karyawan").child(pref.prefidperusahaanuser!!).orderByChild(pref.prefiduser!!)
        dbRef.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        if(datas!!.id == pref.prefiduser){
                            if(datas!!.id == pref.prefiduser) {
                                data.add(datas)
                                pref.prefnamauser = datas.namaKaryawan
                                pref.preftanggallahiruser = datas.tanggallahir
                                pref.prefjeniskelaminuser = datas.jenisKelamin
                                pref.prefalamatuser = datas.alamat
                                pref.prefnohpuser = datas.nohandphone
                                pref.prefdivisiuser = datas.divisi
                                pref.prefemailuser = datas.email
                                pref.prefpassworduser = datas.password
                            }
                        }
                    }
                    binding.hkNamaProfilKaryawan.text = pref.prefnamauser
                    binding.hkAlamatKaryawan.text = pref.prefalamatuser
                } else {
                    Toast.makeText(activity, "Data belum ada", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal", Toast.LENGTH_LONG).show()
            }

        })
    }
    fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}
