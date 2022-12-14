package org.d3ifcool.jobmonitoring.ui.presensi

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import org.d3ifcool.jobmonitoring.adapter.PresensiAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiBinding
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiFilterPresensiBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference
import org.d3ifcool.jobmonitoring.model.PresensiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PresensiFilterPresensiFragment : Fragment() {

    private var _binding: FragmentPresensiFilterPresensiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    private lateinit var pref: Preference
    private lateinit var presensiAdapter: PresensiAdapter
    private val data = arrayListOf<PresensiModel>()
    private lateinit var searchView: SearchView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPresensiFilterPresensiBinding.inflate(inflater, container, false)
        getpresensi("")
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutPresensiFilterPresensi.setOnRefreshListener {
            binding.layoutPresensiFilterPresensi.isRefreshing = false
        }
        binding.pkCollFillter.setOnClickListener {
            findNavController().popBackStack()
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter)
        binding.ppTanggal.text = date.toString()

        searchView = view.findViewById(R.id.pp_search)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextSubmit(query: String?): Boolean {
                getpresensi(query!!)
                return false
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextChange(newtext: String?): Boolean {
                getpresensi(newtext!!)
                return false
            }

        })

        presensiAdapter =
            PresensiAdapter(arrayListOf(), object : PresensiAdapter.OnAdapterListener {
                override fun presensidata(presensi: PresensiModel, v: View) {
                    pref.prefidpresensi = presensi.id
                    pref.prefiduserpresensi = presensi.idUser
                    pref.prefnamauserpresensi = presensi.nama
                    pref.prefdivisipresensi = presensi.divisi
                    pref.prefketeranganpresensi = presensi.keterangan
                    pref.prefwaktupresensi = presensi.waktu
                    pref.preftanggalpresensi = presensi.tanggal
                    findNavController().navigate(R.id.action_presensiFilterPresensiFragment_to_presensiKaryawanFragment)
                }
            })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = presensiAdapter
            setHasFixedSize(true)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getpresensi(text: String) {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val filter = pref.preffilterpresensi
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef = database.getReference("Presensi").child(idPerusahaan!!).orderByChild("nama")
            .startAt(text).endAt(text + "\uf8ff")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(PresensiModel::class.java)
                        if (datas!!.divisi == filter && datas.tanggal!! == date.toString())
                            data.add(datas!!)
                            pref.prefjpresesnsi = data.size

                    }
                    presensiAdapter.setData(data)
                    binding.emptyView.visibility = View.GONE
                    binding.ppJumlah.text = pref.prefjpresesnsi.toString()
                } else {
                    presensiAdapter.setData(data)
                    pref.prefjpresesnsi = 0
                    binding.ppJumlah.text = pref.prefjpresesnsi.toString()
                    binding.emptyView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}