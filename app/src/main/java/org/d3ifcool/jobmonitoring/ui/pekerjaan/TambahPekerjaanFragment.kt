package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PekerjaanModel

class TambahPekerjaanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentTambahPekerjaanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")

    private var listIdKaryawan = ArrayList<String>()
    private var listKaryawan = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTambahPekerjaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        listKaryawan()
        super.onStart()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.layoutTambahPekerjaan.setOnRefreshListener {
            binding.layoutTambahPekerjaan.isRefreshing = false
        }
        binding.tpButtonTambah.setOnClickListener {
            if (binding.tpFormNamaPekerjaan.text.isEmpty()){
                binding.tpFormNamaPekerjaan.setError("Nama pekerjaan tidak boleh kosong")
                binding.tpFormNamaPekerjaan.requestFocus()
            } else if (binding.tpFormDescPekerjaan.text.isEmpty()){
                binding.tpFormDescPekerjaan.setError("Deskripsi tidak boleh kosong")
                binding.tpFormDescPekerjaan.requestFocus()
            } else {
                tambahPekerjaan()
            }
        }
    }

    private fun tambahPekerjaan(){
        val idPekerjaan = dbRef.push().key!!
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val pekerjaan = PekerjaanModel(
            idPekerjaan,
            binding.tpFormNamaPekerjaan.text.toString(),
            binding.tpFormDescPekerjaan.text.toString(),
            binding.tpListKaryawan.selectedItem.toString())
        dbRef.child(name!!).child("Pekerjaan").child(idPekerjaan).setValue(pekerjaan).addOnCompleteListener{
            Toast.makeText(activity,"Pekerjaan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_tambahPekerjaanFragment_to_pekerjaanFragment)
        }.addOnFailureListener{ tast ->
            Toast.makeText(activity,"Gagal menambahkan Pekerjaan${tast.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listKaryawan(){
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val dbRef = database.getReference("Karyawan").child(name!!)
        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        listKaryawan.add(datas!!.namaKaryawan)

                    }
                    binding.tpListKaryawan.onItemSelectedListener = this@TambahPekerjaanFragment
                    val adapter = ArrayAdapter(requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listKaryawan)
                    binding.tpListKaryawan.adapter = adapter
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
