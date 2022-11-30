package org.d3ifcool.jobmonitoring.ui.karyawan

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.KaryawanAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel

class KaryawanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentKaryawanBinding? = null
    private val binding get() = _binding!!

    private lateinit var karyawanAdapter: KaryawanAdapter

    private var listIdDivisi = ArrayList<String>()
    private var listDivisi = ArrayList<String>()

    private val data = arrayListOf<KaryawanModel>()
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanBinding.inflate(inflater, container, false)
        getKaryawan()
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listDivisi()

        binding.layoutKaryawanPerusahaan.setOnRefreshListener {
            binding.layoutKaryawanPerusahaan.isRefreshing = false
        }
        binding.kpButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_karyawanFragment_to_tambahKaryawanFragment)
        }
        karyawanAdapter = KaryawanAdapter(arrayListOf(),object : KaryawanAdapter.OnAdapterListener{
            override fun popupMenus(karyawan: KaryawanModel, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.karyawan_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_Karyawan -> {
                            val id = karyawan.id
                            setFragmentResult(
                                "id",
                                bundleOf("id" to id)
                            )
                            val namaKaryawan = karyawan.namaKaryawan
                            setFragmentResult(
                                "namaKaryawan",
                                bundleOf("namaKaryawan" to namaKaryawan)
                            )
                            val tanggallahir = karyawan.tanggallahir
                            setFragmentResult(
                                "tanggallahir",
                                bundleOf("tanggallahir" to tanggallahir)
                            )
                            val jenisKelamin = karyawan.jenisKelamin
                            setFragmentResult(
                                "jenisKelamin",
                                bundleOf("jenisKelamin" to jenisKelamin)
                            )
                            val alamat = karyawan.alamat
                            setFragmentResult(
                                "alamat",
                                bundleOf("alamat" to alamat)
                            )
                            val nohandphone = karyawan.nohandphone
                            setFragmentResult(
                                "nohandphone",
                                bundleOf("nohandphone" to nohandphone)
                            )
                            val divisi = karyawan.divisi
                            setFragmentResult(
                                "divisi",
                                bundleOf("divisi" to divisi)
                            )
                            val email = karyawan.email
                            setFragmentResult(
                                "email",
                                bundleOf("email" to email)
                            )
                            val password = karyawan.password
                            setFragmentResult(
                                "password",
                                bundleOf("password" to password)
                            )
                            findNavController().navigate(R.id.action_karyawanFragment_to_editKaryawanFragment)
                            true
                        }
                        R.id.delete_Karyawan -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_karyawan)
                                setPositiveButton("HAPUS") { _, _ ->
                                    val user = Firebase.auth.currentUser
                                    val name = user?.displayName
                                    val dbRef = database.getReference("Karyawan").child(name!!).child(karyawan.id)
                                    val task = dbRef.removeValue()
                                    task.addOnSuccessListener{
                                        Toast.makeText(activity,"Karyawan Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                                        getKaryawan()
                                    }.addOnFailureListener{ tast ->
                                        Toast.makeText(activity,"Gagal Menghapus Karyawan${tast.message}", Toast.LENGTH_SHORT).show()
                                        getKaryawan()
                                    }

                                }
                                setNegativeButton("Batal") { dialog, _ ->
                                    dialog.cancel()
                                }
                                show()
                            }
                            true
                        }
                        else -> true
                    }
                }
                popupMenus.show()
            }

        })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = karyawanAdapter
            setHasFixedSize(true)
        }

    }
    private fun getKaryawan(){
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val dbRef = database.getReference("Karyawan").child(name!!)
        dbRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        data.add(datas!!)
                    }
                    karyawanAdapter.setData(data)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
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
                    binding.kpPilihDivisi.onItemSelectedListener = this@KaryawanFragment
                    val adapter = ArrayAdapter(requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listDivisi)
                    binding.kpPilihDivisi.adapter = adapter
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