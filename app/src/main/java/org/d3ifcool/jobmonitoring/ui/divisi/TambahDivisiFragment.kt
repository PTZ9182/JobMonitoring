package org.d3ifcool.jobmonitoring.ui.divisi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahDivisiBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel

class TambahDivisiFragment : Fragment() {

    private var _binding: FragmentTambahDivisiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentTambahDivisiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutTambahDivisi.setOnRefreshListener {
            binding.layoutTambahDivisi.isRefreshing = false
        }

        binding.tdButtonTambah.setOnClickListener {
            if(binding.tdFormNamaDivisi.text.isEmpty()){
                binding.tdFormNamaDivisi.setError("Nama divisi tidak boleh kosong")
                binding.tdFormNamaDivisi.requestFocus()
            } else {
                tambahDivisi()
            }
        }
    }

    private fun tambahDivisi(){
        val idDivisi = dbRef.push().key!!
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val divisi = DivisiModel(idDivisi,binding.tdFormNamaDivisi.text.toString())
        if (name != null) {
            dbRef.child(name).child("Divisi").child(idDivisi).setValue(divisi).addOnCompleteListener{
                Toast.makeText(activity,"Divisi Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_tambahDivisiFragment_to_divisiFragment)
            }.addOnFailureListener{ tast ->
                Toast.makeText(activity,"Gagal menambahkan Divisi${tast.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}