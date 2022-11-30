package org.d3ifcool.jobmonitoring.ui.divisi

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.*
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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.DivisiAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentDivisiBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel


class DivisiFragment : Fragment() {

    private var _binding: FragmentDivisiBinding? = null
    private val binding get() = _binding!!

    private lateinit var divisiAdapter: DivisiAdapter

    private val data = arrayListOf<DivisiModel>()
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDivisiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        getDivisi()
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutDivisiPerusahaan.setOnRefreshListener {
            binding.layoutDivisiPerusahaan.isRefreshing = false
        }

        binding.dpButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_divisiFragment_to_tambahDivisiFragment)
        }

        divisiAdapter = DivisiAdapter(arrayListOf(),object : DivisiAdapter.OnAdapterListener{
            override fun popupMenus(divisi: DivisiModel, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.divisi_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_divisi -> {
                            val td_divisi = divisi.divisi
                            setFragmentResult(
                                "divisi",
                                bundleOf("divisi" to td_divisi)
                            )

                            val id = divisi.id
                            setFragmentResult(
                                "id",
                                bundleOf("id" to id)
                            )

                            findNavController().navigate(R.id.action_divisiFragment_to_editDivisiFragment)
                            true
                        }
                        R.id.delete_divisi -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_divisi)
                                setPositiveButton("HAPUS") { _, _ ->
                                    val user = Firebase.auth.currentUser
                                    val name = user?.displayName
                                    val dbRef = database.getReference("Perusahaan").child(name!!).child("Divisi").child(divisi.id)
                                    val task = dbRef.removeValue()
                                    task.addOnSuccessListener{
                                        Toast.makeText(activity,"Divisi Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                                        getDivisi()
                                    }.addOnFailureListener{ tast ->
                                        Toast.makeText(activity,"Gagal Menghapus Divisi${tast.message}", Toast.LENGTH_SHORT).show()
                                        getDivisi()
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
            adapter = divisiAdapter
            setHasFixedSize(true)
        }

    }
    private fun getDivisi(){
        val user = Firebase.auth.currentUser
        val name = user?.displayName
        val dbRef = database.getReference("Perusahaan").child(name!!).child("Divisi")
        dbRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        data.add(datas!!)
                    }
                    divisiAdapter.setData(data)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}
