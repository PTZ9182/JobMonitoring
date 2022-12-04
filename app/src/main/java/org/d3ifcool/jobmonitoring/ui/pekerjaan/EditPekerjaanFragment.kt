package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentEditPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class EditPekerjaanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentEditPekerjaanBinding? = null
    private val binding get() = _binding!!

    private var listIdKaryawan = ArrayList<String>()
    private var listKaryawan = ArrayList<String>()
    private lateinit var pref: Preference
    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditPekerjaanBinding.inflate(inflater, container, false)
        set()
        listKaryawan()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    private fun set() {
        setFragmentResultListener("nama_pekerjaan") { requestKey, bundle ->
            val result = bundle.getString("nama_pekerjaan")
            binding.epIsiformNamaPekerjaan.setText(result)
        }
        setFragmentResultListener("deskripsi") { requestKey, bundle ->
            val result = bundle.getString("deskripsi")
            binding.epIsiformDescPekerjaan.setText(result)
        }
        setFragmentResultListener("karyawan") { requestKey, bundle ->
            val result = bundle.getString("karyawan")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutEditPekerjaan.setOnRefreshListener {
            binding.layoutEditPekerjaan.isRefreshing = false
        }

        binding.epButtonSimpan.setOnClickListener {
            if (binding.epIsiformNamaPekerjaan.text.isEmpty()) {
                binding.epIsiformNamaPekerjaan.setError("Nama pekerjaan tidak boleh kosong")
                binding.epIsiformNamaPekerjaan.requestFocus()
            } else if (binding.epIsiformDescPekerjaan.text.isEmpty()) {
                binding.epIsiformDescPekerjaan.setError("Deskripsi tidak boleh kosong")
                binding.epIsiformDescPekerjaan.requestFocus()
            } else {
                editPekerjaan()
            }
        }
    }

    private fun editPekerjaan() {
        setFragmentResultListener("id") { requestKey, bundle ->
            val result = bundle.getString("id")
            setFragmentResultListener("status") { requestKey, bundle ->
                val result2 = bundle.getString("status")
                val contextt: Context
                contextt = requireActivity()
                pref = Preference(contextt)
                val div = pref.prefpekdiv
                val user = Firebase.auth.currentUser
                val name = user?.displayName
                val pekerjaan = PekerjaanModel(
                    result!!,
                    div!!,
                    binding.epIsiformNamaPekerjaan.text.toString(),
                    binding.epIsiformDescPekerjaan.text.toString(),
                    binding.epListKaryawan.selectedItem.toString(),
                    result2!!
                )
                dbRef.child(name!!).child("Pekerjaan").child(result).setValue(pekerjaan)
                    .addOnCompleteListener {
                        Toast.makeText(
                            activity,
                            "Data Karyawan Pekerjaan Diubah",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        findNavController().navigate(R.id.action_editPekerjaanFragment_to_pekerjaanFragment)
                    }.addOnFailureListener { tast ->
                        Toast.makeText(
                            activity,
                            "Gagal Mengubah Data Pekerjaan${tast.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }
        }
    }

    private fun listKaryawan() {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val div = pref.prefpekdiv
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val dbRef =
            database.getReference("Karyawan").child(name!!).orderByChild("divisi").equalTo(div)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        listKaryawan.add(datas!!.namaKaryawan)

                    }
                    binding.epListKaryawan.onItemSelectedListener = this@EditPekerjaanFragment
                    val adapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        listKaryawan
                    )
                    binding.epListKaryawan.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.getItemAtPosition(p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
