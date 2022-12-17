package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class TambahPekerjaanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentTambahPekerjaanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Pekerjaan")
    private lateinit var pref: Preference
    private var listKaryawan = ArrayList<String>()
    private var listidKaryawan = ArrayList<String>()
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTambahPekerjaanBinding.inflate(inflater, container, false)
        listKaryawan()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        binding.layoutTambahPekerjaan.setOnRefreshListener {
            binding.layoutTambahPekerjaan.isRefreshing = false
        }
        binding.tpButtonTambah.setOnClickListener {
            if (binding.tpFormNamaPekerjaan.text.isEmpty()){
                binding.tpFormNamaPekerjaan.error = "Nama pekerjaan tidak boleh kosong"
                binding.tpFormNamaPekerjaan.requestFocus()
            } else if (binding.tpFormDescPekerjaan.text.isEmpty()){
                binding.tpFormDescPekerjaan.error = "Deskripsi tidak boleh kosong"
                binding.tpFormDescPekerjaan.requestFocus()
            } else if (binding.tpListKaryawan.selectedItem.equals("Pilih Karyawan")){
                Toast.makeText(context, "Karyawan Tidak tersedia", Toast.LENGTH_SHORT).show()
                binding.tpListKaryawan.requestFocus()
            } else {
                tambahPekerjaan()
            }
        }
    }

    private fun tambahPekerjaan(){
        nDialog.show()
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val idPekerjaan = dbRef.push().key!!
        var iduser = pref.prefiduserpekerjaan
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val divisi = pref.prefdivisipekerjaan
        val pekerjaan = PekerjaanModel(
            idPekerjaan,iduser!!, divisi!!,
            binding.tpFormNamaPekerjaan.text.toString(),
            binding.tpFormDescPekerjaan.text.toString(),
            binding.tpListKaryawan.selectedItem.toString())
        dbRef.child(idPerusahaan!!).child(idPekerjaan).setValue(pekerjaan).addOnCompleteListener{
            nDialog.cancel()
            Toast.makeText(activity,"Pekerjaan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener{ tast ->
            nDialog.cancel()
            Toast.makeText(activity,"Gagal menambahkan Pekerjaan${tast.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listKaryawan(){
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val divisi = pref.prefdivisipekerjaan
        val dbRef = database.getReference("Karyawan").child(idPerusahaan!!).orderByChild("divisi").equalTo(divisi)
        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listKaryawan.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        listKaryawan.add(datas!!.namaKaryawan)
                        listidKaryawan.add(datas!!.id)
                    }
                    binding.tpListKaryawan.onItemSelectedListener = this@TambahPekerjaanFragment
                    val adapter = context?.let {
                        ArrayAdapter(
                            it,
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listKaryawan)
                    }
                    binding.tpListKaryawan.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        p0?.getItemAtPosition(p2)
        pref.prefiduserpekerjaan = listidKaryawan[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
