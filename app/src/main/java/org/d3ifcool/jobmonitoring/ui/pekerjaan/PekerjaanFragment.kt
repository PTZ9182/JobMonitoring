package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
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
import org.d3ifcool.jobmonitoring.model.Preference

class PekerjaanFragment : Fragment() {

    private var _binding: FragmentPekerjaanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    private lateinit var pref: Preference
    private lateinit var pekerjaanAdapter: PekerjaanAdapter
    private val data = arrayListOf<PekerjaanModel>()
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanBinding.inflate(inflater, container, false)
        getPekerjaan("")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutPekerjaanPerusahaan.setOnRefreshListener {
            binding.layoutPekerjaanPerusahaan.isRefreshing = false
        }

        binding.pkButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_pekerjaanFragment_to_tambahPekerjaanDivisiFragment)
        }

        binding.pkCollFillter.setOnClickListener {
            findNavController().navigate(R.id.action_pekerjaanFragment_to_pekerjaanFilterDivisiFragment)
        }

        searchView = view.findViewById(R.id.pk_search)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getPekerjaan(query!!)
                return false
            }

            override fun onQueryTextChange(newtext: String?): Boolean {
                getPekerjaan(newtext!!)
                return false
            }

        })

        pekerjaanAdapter = PekerjaanAdapter(arrayListOf(),object : PekerjaanAdapter.OnAdapterListener{
            override fun popupMenus(pekerjaan: PekerjaanModel, v: View) {
                val contextt : Context
                contextt = requireActivity()
                pref = Preference(contextt)
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.pekerjaan_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_pekerjaan -> {
                            pref.prefidpekerjaan = pekerjaan.id
                            pref.prefdivisipekerjaan = pekerjaan.divisi
                            pref.prefnamapekerjaan = pekerjaan.nama_pekerjaan
                            pref.prefdeskripsipekerjaan = pekerjaan.deskripsi
                            pref.prefkaryawanpekerjaan = pekerjaan.karyawan
                            pref.prefstatuspekerjaan = pekerjaan.status
                            pref.prefprogresspekerjaan = pekerjaan.progress.toString()
                            findNavController().navigate(R.id.action_pekerjaanFragment_to_editPekerjaanFragment)
                            true
                        }
                        R.id.delete_pekerjaan -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_pekerjaan)
                                setPositiveButton("HAPUS") { _, _ ->
                                    val user = Firebase.auth.currentUser
                                    val idPerusahaan = user?.uid
                                    val dbRef = database.getReference("Pekerjaan").child(idPerusahaan!!).child(pekerjaan.id)
                                    val task = dbRef.removeValue()
                                    task.addOnSuccessListener{
                                        Toast.makeText(activity,"Pekerjaan Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener{ tast ->
                                        Toast.makeText(activity,"Gagal Menghapus Pekerjaan${tast.message}", Toast.LENGTH_SHORT).show()
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

            override fun detail(pekerjaan: PekerjaanModel, v: View) {
                pref.prefidpekerjaan = pekerjaan.id
                pref.prefdivisipekerjaan = pekerjaan.divisi
                pref.prefnamapekerjaan = pekerjaan.nama_pekerjaan
                pref.prefdeskripsipekerjaan = pekerjaan.deskripsi
                pref.prefkaryawanpekerjaan = pekerjaan.karyawan
                pref.prefstatuspekerjaan = pekerjaan.status
                pref.prefprogresspekerjaan = pekerjaan.progress.toString()
                findNavController().navigate(R.id.action_pekerjaanFragment_to_pekerjaanDetailFragment)
            }
        })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = pekerjaanAdapter
            setHasFixedSize(true)
        }

    }
    private fun getPekerjaan(text: String){
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef = database.getReference("Pekerjaan").child(idPerusahaan!!).orderByChild("nama_pekerjaan").startAt(text).endAt(text + "\uf8ff")
        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(PekerjaanModel::class.java)
                        data.add(datas!!)
                        pref.prefjpekerjaan = data.size
                    }
                    pekerjaanAdapter.setData(data)
                    binding.emptyView.visibility = View.GONE
                    if (data.size != 0){
                        binding.pkJumlah.text = pref.prefjpekerjaan.toString()
                    } else {
                        pref.prefjpekerjaan = 0
                        binding.pkJumlah.text = pref.prefjpekerjaan.toString()
                        binding.emptyView.visibility = View.VISIBLE
                    }
                } else {
                    pref.prefjpekerjaan = 0
                    binding.pkJumlah.text = pref.prefjpekerjaan.toString()
                    pekerjaanAdapter.setData(data)
                    binding.emptyView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}