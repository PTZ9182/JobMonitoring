package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.app.ProgressDialog
import android.content.Context
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
import org.d3ifcool.jobmonitoring.databinding.FragmentEditPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class EditPekerjaanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentEditPekerjaanBinding? = null
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

        _binding = FragmentEditPekerjaanBinding.inflate(inflater, container, false)
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

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.epIsiformNamaPekerjaan.setText(pref.prefnamapekerjaan)
        binding.epIsiformDescPekerjaan.setText(pref.prefdeskripsipekerjaan)

        binding.layoutEditPekerjaan.setOnRefreshListener {
            binding.layoutEditPekerjaan.isRefreshing = false
        }

        binding.epButtonSimpan.setOnClickListener {
            if (binding.epIsiformNamaPekerjaan.text.isEmpty()) {
                binding.epIsiformNamaPekerjaan.error = "Nama pekerjaan tidak boleh kosong"
                binding.epIsiformNamaPekerjaan.requestFocus()
            } else if (binding.epIsiformDescPekerjaan.text.isEmpty()) {
                binding.epIsiformDescPekerjaan.error = "Deskripsi tidak boleh kosong"
                binding.epIsiformDescPekerjaan.requestFocus()
            } else {
                editPekerjaan()
            }
        }
    }

    private fun editPekerjaan() {
        nDialog.show()
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val idPekerjaan = pref.prefidpekerjaan
        var idkaryawan = pref.prefidkaryawanpekerjaan
        val iddivisi = pref.prefiddivisipekerjaan
        val progress = pref.prefprogresspekerjaan
        val status = pref.prefstatuspekerjaan
        val pekerjaan = PekerjaanModel(
            idPekerjaan!!,idkaryawan!!,
            iddivisi!!,
            binding.epIsiformNamaPekerjaan.text.toString(),
            binding.epIsiformDescPekerjaan.text.toString(),
            progress!!.toInt(),
            status!!)
        dbRef.child(idPerusahaan!!).child(idPekerjaan).setValue(pekerjaan)
            .addOnCompleteListener {
                nDialog.cancel()
                Toast.makeText(
                    activity,
                    "Data Karyawan Pekerjaan Diubah",
                    Toast.LENGTH_SHORT
                )
                    .show()
                findNavController().popBackStack()
            }.addOnFailureListener { tast ->
                nDialog.cancel()
                Toast.makeText(
                    activity,
                    "Gagal Mengubah Data Pekerjaan${tast.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }

    private fun listKaryawan() {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val iddivisi = pref.prefiddivisipekerjaan
        val dbRef =
            database.getReference("Karyawan").child(idPerusahaan!!).orderByChild("iddivisi").equalTo(iddivisi)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listKaryawan.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        listKaryawan.add(datas!!.namaKaryawan)
                        listidKaryawan.add(datas!!.id)

                    }
                    binding.epListKaryawan.onItemSelectedListener = this@EditPekerjaanFragment
                    val adapter = context?.let {
                        ArrayAdapter(
                            it,
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                            listKaryawan
                        )
                    }
                    binding.epListKaryawan.adapter = adapter
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
        pref.prefidkaryawanpekerjaan = listidKaryawan[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
