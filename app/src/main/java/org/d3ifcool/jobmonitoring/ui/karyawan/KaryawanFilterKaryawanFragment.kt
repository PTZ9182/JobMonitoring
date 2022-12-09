package org.d3ifcool.jobmonitoring.ui.karyawan

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
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
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanFilterKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class KaryawanFilterKaryawanFragment : Fragment() {

    private var _binding: FragmentKaryawanFilterKaryawanBinding? = null
    private val binding get() = _binding!!

    private lateinit var karyawanAdapter: KaryawanAdapter
    private lateinit var pref: Preference
    private val data = arrayListOf<KaryawanModel>()
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanFilterKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        getKaryawan()
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutKaryawanFilterKaryawan.setOnRefreshListener {
            binding.layoutKaryawanFilterKaryawan.isRefreshing = false
        }
        binding.kpkpButton.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_karyawanFilterKaryawanFragment_to_editKaryawanFragment)
        }
        binding.kpkpCollFillter.setOnClickListener {
            findNavController().navigate(R.id.action_karyawanFilterKaryawanFragment_to_karyawanFilterFragment)
        }
        karyawanAdapter =
            KaryawanAdapter(arrayListOf(), object : KaryawanAdapter.OnAdapterListener {
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
                                findNavController().navigate(R.id.action_karyawanFilterKaryawanFragment_to_editKaryawanFragment)
                                true
                            }
                            R.id.delete_Karyawan -> {
                                AlertDialog.Builder(context).apply {
                                    setMessage(R.string.pesan_hapus_karyawan)
                                    setPositiveButton("HAPUS") { _, _ ->
                                        val user = Firebase.auth.currentUser
                                        val name = user?.displayName
                                        val dbRef = database.getReference("Karyawan").child(name!!)
                                            .child(karyawan.id)
                                        val task = dbRef.removeValue()
                                        task.addOnSuccessListener {
                                            Toast.makeText(
                                                activity,
                                                "Karyawan Berhasil Dihapus",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            getKaryawan()
                                        }.addOnFailureListener { tast ->
                                            Toast.makeText(
                                                activity,
                                                "Gagal Menghapus Karyawan${tast.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
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

                override fun profil(karyawan: KaryawanModel, v: View) {
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
                    findNavController().navigate(R.id.action_karyawanFilterKaryawanFragment_to_profilKaryawan)
                }

            })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = karyawanAdapter
            setHasFixedSize(true)
        }

    }

    private fun getKaryawan() {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val filkar = pref.preffilkar
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val dbRef =
            database.getReference("Karyawan").child(name!!).orderByChild("divisi").equalTo(filkar)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
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

}