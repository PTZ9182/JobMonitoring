package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.d3ifcool.jobmonitoring.adapter.PekerjaanAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel

class PekerjaanFragment : Fragment() {

    private var _binding: FragmentPekerjaanBinding? = null
    private val binding get() = _binding!!

    private lateinit var pekerjaanAdapter: PekerjaanAdapter

    private val data = arrayListOf<PekerjaanModel>()
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        getPekerjaan()
        super.onStart()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.layoutPekerjaanPerusahaan.setOnRefreshListener {
            binding.layoutPekerjaanPerusahaan.isRefreshing = false
        }

        binding.pkButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_pekerjaanFragment_to_tambahPekerjaanFragment)
        }

        pekerjaanAdapter = PekerjaanAdapter(arrayListOf(),object : PekerjaanAdapter.OnAdapterListener{
            override fun popupMenus(pekerjaan: PekerjaanModel, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.pekerjaan_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_pekerjaan -> {
                            val id = pekerjaan.id
                            setFragmentResult(
                                "id",
                                bundleOf("id" to id)
                            )
                            val nama_pekerjaan = pekerjaan.nama_pekerjaan
                            setFragmentResult(
                                "nama_pekerjaan",
                                bundleOf("nama_pekerjaan" to nama_pekerjaan)
                            )
                            val deskripsi = pekerjaan.deskripsi
                            setFragmentResult(
                                "deskripsi",
                                bundleOf("deskripsi" to deskripsi)
                            )
                            val karyawan = pekerjaan.karyawan
                            setFragmentResult(
                                "karyawan",
                                bundleOf("karyawan" to karyawan)
                            )
                            val status = pekerjaan.status
                            setFragmentResult(
                                "status",
                                bundleOf("status" to status)
                            )

                            findNavController().navigate(R.id.action_pekerjaanFragment_to_editPekerjaanFragment)
                            true
                        }
                        R.id.delete_pekerjaan -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_pekerjaan)
                                setPositiveButton("HAPUS") { _, _ ->
                                    val user = Firebase.auth.currentUser
                                    val name = user?.displayName
                                    val dbRef = database.getReference("Perusahaan").child(name!!).child("Pekerjaan").child(pekerjaan.id)
                                    val task = dbRef.removeValue()
                                    task.addOnSuccessListener{
                                        Toast.makeText(activity,"Pekerjaan Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                                        getPekerjaan()
                                    }.addOnFailureListener{ tast ->
                                        Toast.makeText(activity,"Gagal Menghapus Pekerjaan${tast.message}", Toast.LENGTH_SHORT).show()
                                        getPekerjaan()
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
            adapter = pekerjaanAdapter
            setHasFixedSize(true)
        }

    }
    private fun getPekerjaan(){
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val dbRef = database.getReference("Perusahaan").child(name!!).child("Pekerjaan")
        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(PekerjaanModel::class.java)
                        data.add(datas!!)
                    }
                    pekerjaanAdapter.setData(data)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}