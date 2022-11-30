package org.d3ifcool.jobmonitoring.ui.loginRegister

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentLoginKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class LoginKaryawanFragment : Fragment() {

    private var _binding: FragmentLoginKaryawanBinding? = null
    private val binding get() = _binding!!

    val database:DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var pref:Preference
    lateinit var nDialog: ProgressDialog

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            if (binding.lkIsiformPerusahaan.text.toString().isEmpty()){
                binding.lkIsiformPerusahaan.setError("Perusahaan tidak boleh kosong")
                binding.lkIsiformPerusahaan.requestFocus()
            } else if (binding.lkIsiformEmail.text.toString().isEmpty()){
                binding.lkIsiformEmail.setError("Email tidak boleh kosong")
                binding.lkIsiformEmail.requestFocus()
            }else if (binding.lkIsiformPassword.text.toString().isEmpty()){
                binding.lkIsiformPassword.setError("Password tidak boleh kosong")
                binding.lkIsiformPassword.requestFocus()
            } else {
                nDialog.show()
                val dbRef : Query = database.child("Karyawan").child(
                    binding.lkIsiformPerusahaan.text.toString()).orderByChild("email").equalTo(binding.lkIsiformEmail.text.toString())
                dbRef.addListenerForSingleValueEvent((object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for (datas in snapshot.children){
                                val karyawan = datas.getValue<KaryawanModel>()
                                if(karyawan != null){
                                    if(karyawan.password.equals(binding.lkIsiformPassword.text.toString())){
                                        nDialog.cancel()
                                        val email = datas.key
                                        setFragmentResult(
                                            "key",
                                            bundleOf("key" to email)
                                        )
                                        val company = binding.lkIsiformPerusahaan.text.toString()
                                        setFragmentResult(
                                            "perusahaan",
                                            bundleOf("perusahaan" to company)
                                        )
                                        pref.prefstatus = true
                                        pref.prefkey = datas.key
                                        pref.prefperusahaan = binding.lkIsiformPerusahaan.text.toString()
                                        reload()
                                    } else {
                                        nDialog.cancel()
                                        Toast.makeText(context, "Password Salah", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            nDialog.cancel()
                            Toast.makeText(context, "Email atau Perusahaan Belum Terdaftar", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }))
            }
        }
    }
}