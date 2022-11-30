package org.d3ifcool.jobmonitoring.ui.karyawan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
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
import org.d3ifcool.jobmonitoring.databinding.FragmentEditKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel

class EditKaryawanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentEditKaryawanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Karyawan")

    private var listIdDivisi = ArrayList<String>()
    private var listDivisi = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditKaryawanBinding.inflate(inflater, container, false)
        set()
        listDivisi()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    private fun set() {
        setFragmentResultListener("namaKaryawan") { requestKey, bundle ->
            val result = bundle.getString("namaKaryawan")
            binding.ekFormNama.setText(result)
        }
        setFragmentResultListener("tanggallahir") { requestKey, bundle ->
            val result = bundle.getString("tanggallahir")
            binding.ekFormTanggallahir.setText(result)
        }
        setFragmentResultListener("jenisKelamin") { requestKey, bundle ->
            val result = bundle.getString("jenisKelamin")
        }
        setFragmentResultListener("alamat") { requestKey, bundle ->
            val result = bundle.getString("alamat")
            binding.ekFormAlamat.setText(result)
        }
        setFragmentResultListener("nohandphone") { requestKey, bundle ->
            val result = bundle.getString("nohandphone")
            binding.ekFormNohp.setText(result)
        }
        setFragmentResultListener("divisi") { requestKey, bundle ->
            val result = bundle.getString("divisi")
        }
        setFragmentResultListener("email") { requestKey, bundle ->
            val result = bundle.getString("email")
            binding.ekFormEmail.setText(result)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutEditKaryawan.setOnRefreshListener {
            binding.layoutEditKaryawan.isRefreshing = false
        }

        binding.ekGantipassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_editKaryawanFragment_to_gantiPasswordKaryawanFragment)
        }
        binding.buttonSimpanDataForm.setOnClickListener {
            if (binding.ekFormNama.text.isEmpty()){
                binding.ekFormNama.setError("Nama tidak boleh kosong")
                binding.ekFormNama.requestFocus()
            } else if (binding.ekFormTanggallahir.text.isEmpty()){
                binding.ekFormTanggallahir.setError("Tanggal lahir tidak boleh kosong")
                binding.ekFormTanggallahir.requestFocus()
            } else if (binding.ekFormJeniskelamin.selectedItem.equals("Jenis Kelamin")){
                Toast.makeText(context, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                binding.ekFormJeniskelamin.requestFocus()
            } else if (binding.ekFormAlamat.text.isEmpty()){
                binding.ekFormAlamat.setError("Alamat tidak boleh kosong")
                binding.ekFormAlamat.requestFocus()
            } else if (binding.ekFormNohp.text.isEmpty()){
                binding.ekFormNohp.setError("No.Handphone tidak boleh kosong")
                binding.ekFormNohp.requestFocus()
            } else if (binding.ekFormPilihdivisi.selectedItem.equals("Pilih Divisi")){
                Toast.makeText(context, "Pilih divisi", Toast.LENGTH_SHORT).show()
                binding.ekFormPilihdivisi.requestFocus()
            } else if (binding.ekFormEmail.text.isEmpty()){
                binding.ekFormEmail.setError("Email tidak boleh kosong")
                binding.ekFormEmail.requestFocus()
            } else {
                editKaryawan()
            }
        }
    }

    private fun editKaryawan() {
        setFragmentResultListener("id") { requestKey, bundle ->
            val result = bundle.getString("id")
            setFragmentResultListener("password") { requestKey, bundle ->
                val result2 = bundle.getString("password")
            val user = Firebase.auth.currentUser
            val name = user?.displayName
            val karyawan = KaryawanModel(
                result!!,
                binding.ekFormNama.text.toString(),
                binding.ekFormTanggallahir.text.toString(),
                binding.ekFormJeniskelamin.selectedItem.toString(),
                binding.ekFormAlamat.text.toString(),
                binding.ekFormNohp.text.toString(),
                binding.ekFormPilihdivisi.selectedItem.toString(),
                binding.ekFormEmail.text.toString(),result2!!)
            dbRef.child(name!!).child(result).setValue(karyawan)
                .addOnCompleteListener {
                    Toast.makeText(activity, "Data Karyawan Berhasil Diubah", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_editKaryawanFragment_to_karyawanFragment)
                }.addOnFailureListener { tast ->
                Toast.makeText(
                    activity,
                    "Gagal Mengubah Data Karyawan${tast.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        }
    }

    private fun listDivisi(){
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val dbRef = database.getReference("Perusahaan").child(name!!).child("Divisi")
        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(DivisiModel::class.java)
                            listDivisi.add(datas!!.divisi)

                    }
                    binding.ekFormPilihdivisi.onItemSelectedListener = this@EditKaryawanFragment
                    val adapter = ArrayAdapter(requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listDivisi)
                    binding.ekFormPilihdivisi.adapter = adapter
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
