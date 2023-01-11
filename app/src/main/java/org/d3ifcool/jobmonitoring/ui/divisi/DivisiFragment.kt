package org.d3ifcool.jobmonitoring.ui.divisi

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import org.d3ifcool.jobmonitoring.model.Preference
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat.recreate
class DivisiFragment : Fragment() {

    private var _binding: FragmentDivisiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    private lateinit var pref: Preference
    private lateinit var divisiAdapter: DivisiAdapter
    private val data = arrayListOf<DivisiModel>()
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDivisiBinding.inflate(inflater, container, false)
        getDivisi("")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutDivisiPerusahaan.setOnRefreshListener {
            recreate(requireActivity())
            binding.layoutDivisiPerusahaan.isRefreshing = false
        }

        binding.dpButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_divisiFragment_to_tambahDivisiFragment)
        }

        searchView = view.findViewById(R.id.dp_search)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getDivisi(query!!)
                return false
            }

            override fun onQueryTextChange(newtext: String?): Boolean {
                getDivisi(newtext!!)
                return false
            }

        })

        divisiAdapter = DivisiAdapter(arrayListOf(), object : DivisiAdapter.OnAdapterListener {
            override fun popupMenus(divisi: DivisiModel, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.divisi_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_divisi -> {
                            pref.prefiddivisi = divisi.id
                            pref.prefdivisi = divisi.divisi
                            findNavController().navigate(R.id.action_divisiFragment_to_editDivisiFragment)
                            true
                        }
                        R.id.delete_divisi -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_divisi)
                                setPositiveButton("HAPUS") { _, _ ->
                                    val user = Firebase.auth.currentUser
                                    val idPerusahaan = user?.uid
                                    val dbRef =
                                        database.getReference("Divisi").child(idPerusahaan!!)
                                            .child(divisi.id)
                                    val task = dbRef.removeValue()
                                    task.addOnSuccessListener {
                                        Toast.makeText(
                                            activity,
                                            "Divisi Berhasil Dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener { tast ->
                                        Toast.makeText(
                                            activity,
                                            "Gagal Menghapus Divisi${tast.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
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

    private fun getDivisi(text: String) {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef = database.getReference("Divisi").child(idPerusahaan!!).orderByChild("divisi")
            .startAt(text).endAt(text + "\uf8ff")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        data.add(datas!!)
                        pref.prefjdivisi = data.size

                    }
                    divisiAdapter.setData(data)
                    binding.dpJumlah.text = pref.prefjdivisi.toString()
                    binding.emptyView.visibility = View.GONE
                } else {
                    pref.prefjdivisi = 0
                    binding.dpJumlah.text = pref.prefjdivisi.toString()
                    divisiAdapter.setData(data)
                    binding.emptyView.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}



