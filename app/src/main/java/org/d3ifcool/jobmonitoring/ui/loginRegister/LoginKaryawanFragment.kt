package org.d3ifcool.jobmonitoring.ui.loginRegister

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.KaryawanAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentLoginKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.d3ifcool.jobmonitoring.model.Preference
import kotlin.math.log

class LoginKaryawanFragment : Fragment() {

    private var _binding: FragmentLoginKaryawanBinding? = null
    private val binding get() = _binding!!

    val database:DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var pref:Preference
    lateinit var nDialog: ProgressDialog
    private val data = arrayListOf<PerusahaanModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        if (pref.prefstatus){
            reload()
        }
        super.onStart()
    }

    private fun reload(){
        findNavController().navigate(R.id.action_loginKaryawanFragment_to_homeKaryawanFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang Login")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        binding.loginKaryawan.setOnRefreshListener {
            binding.loginKaryawan.isRefreshing = false
        }
        binding.lkRegis.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginKaryawanFragment_to_registerFragment)
        }

        binding.lkButtonLogin.setOnClickListener {
            val email = binding.lkIsiformEmail.text.toString()
            if (binding.lkIsiformPerusahaan.text.toString().isEmpty()){
                binding.lkIsiformPerusahaan.error = "Perusahaan tidak boleh kosong"
                binding.lkIsiformPerusahaan.requestFocus()
            } else if (binding.lkIsiformEmail.text.toString().isEmpty()) {
                binding.lkIsiformEmail.error = "Email tidak boleh kosong"
                binding.lkIsiformEmail.requestFocus()
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.lkIsiformEmail.error = "Email Tidak Valid!!!!"
                binding.lkIsiformEmail.requestFocus()
            }else if (binding.lkIsiformPassword.text.toString().isEmpty()){
                binding.lkIsiformPassword.error = "Password tidak boleh kosong"
                binding.lkIsiformPassword.requestFocus()
            } else {
                val databasee = Firebase.database
                val nama = binding.lkIsiformPerusahaan.text.toString()
                val dbReff = databasee.getReference("Perusahaan").orderByChild("perusahaan").equalTo(nama)
                dbReff.addListenerForSingleValueEvent(object  : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for (datasnap in snapshot.children){
                                val datas = datasnap.getValue(PerusahaanModel::class.java)
                                if(datas!!.perusahaan == nama){
                                    data.add(datas)
                                    pref.prefidperusahaanuser = datas.id
                                }
                            }
                            login(pref.prefidperusahaanuser!!)
                        } else {
                            Toast.makeText(activity, "Perusahaan Belum terdaftar", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }

    private fun login(id:String){

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        val dbRef : Query = database
            .child("Karyawan")
            .child(id)
            .orderByChild("email")
            .equalTo(binding.lkIsiformEmail.text.toString())
        dbRef.addListenerForSingleValueEvent((object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (datas in snapshot.children){
                        val karyawan = datas.getValue<KaryawanModel>()
                        if(karyawan != null){
                            if(karyawan.password == binding.lkIsiformPassword.text.toString()){

                                pref.prefiduser = datas.key
                                pref.prefperusahaanuser = binding.lkIsiformPerusahaan.text.toString()
                                pref.prefstatus = true
                                nDialog.cancel()
                                reload()
                            } else {
                                nDialog.cancel()
                                Toast.makeText(context, "Password Salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    nDialog.cancel()
                    Toast.makeText(context, "Email Belum Terdaftar", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
            }
        }))
    }
}