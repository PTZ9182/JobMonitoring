package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentHomeKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class HomeKaryawanFragment : Fragment() {

    private var _binding: FragmentHomeKaryawanBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeKaryawanBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onStart() {
        getData()
        super.onStart()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hkCollPengaturan.setOnClickListener {
            pref.prefClear()
            findNavController().navigate(R.id.action_homeKaryawanFragment_to_praLoginFragment)
        }
        binding.hkCollPekerjaan.setOnClickListener {
            findNavController().navigate(R.id.action_homeKaryawanFragment_to_karyawanPekerjaanFragment)
        }
    }


    private fun getData() {
        val context: Context
        context = requireActivity()
        pref = Preference(context)
        val dbRef = database.getReference("Karyawan").child(pref.prefperusahaan!!).child(pref.prefkey!!)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("firebase", "onDataChange: $snapshot")
                if (snapshot.exists()) {
                    binding.hkNamaProfilKaryawan.text = snapshot.child("namaKaryawan").value as CharSequence?
                    binding.hkAlamatKaryawan.text = snapshot.child("alamat").value as CharSequence?
                    pref.prefnamekar = (snapshot.child("namaKaryawan").value as CharSequence?).toString()
                } else {
                    Log.i("firebase", "Tidak ada data")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("firebase", "ERORR BAH")
            }

        })
    }
}
