package org.d3ifcool.jobmonitoring.ui.presensi

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.recreate
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
import org.d3ifcool.jobmonitoring.adapter.PresensiAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiBinding
import org.d3ifcool.jobmonitoring.model.Preference
import org.d3ifcool.jobmonitoring.model.PresensiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PresensiFragment : Fragment() {

    private var _binding: FragmentPresensiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    private lateinit var pref: Preference
    private lateinit var presensiAdapter: PresensiAdapter
    private val data = arrayListOf<PresensiModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPresensiBinding.inflate(inflater, container, false)
        getpresensi()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        pref.prefrekaptitlepresensi = "presensi"

        binding.ppButton.setOnClickListener {
            findNavController().navigate(R.id.action_presensiFragment_to_tambahPresensiFragment)
        }

        binding.layoutPresensiPerusahaan.setOnRefreshListener {
                activity?.let { recreate(it) }
            binding.layoutPresensiPerusahaan.isRefreshing = false
        }
        binding.pkCollFillter.setOnClickListener {
            it.findNavController().navigate(R.id.action_presensiFragment_to_presensiFilterFragment)
        }
        binding.ppRekap.setOnClickListener {
            findNavController().navigate(R.id.action_presensiFragment_to_presensiRekapTanggal)
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter)
        binding.ppTanggal.text = date.toString()
        pref.prefrekaptanggalpresensi = date.toString()

        presensiAdapter =
            PresensiAdapter(arrayListOf(), object : PresensiAdapter.OnAdapterListener {
                override fun presensidata(presensi: PresensiModel, v: View) {
                    pref.prefidpresensi = presensi.id
                    pref.prefidkaryawanpresensi = presensi.idkaryawan
                    pref.prefiddivisipresensi = presensi.iddivisi
                    pref.prefketeranganpresensi = presensi.keterangan
                    pref.prefwaktumasukpresensi = presensi.waktumasuk
                    pref.prefwaktukeluarpresensi = presensi.waktukeluar
                    pref.preftanggalpresensi = presensi.tanggal
                    findNavController().navigate(R.id.action_presensiFragment_to_presensiKaryawanFragment)
                }
            })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = presensiAdapter
            setHasFixedSize(true)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getpresensi() {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef = database.getReference("Presensi").child(idPerusahaan!!).orderByChild("waktumasuk")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(PresensiModel::class.java)
                        if (datas!!.tanggal == date) {
                            data.add(datas)
                            pref.prefjpresesnsi = data.size
                        }
                    }
                    presensiAdapter.setData(data)
                    binding.emptyView.visibility = View.GONE
                    if (data.size != 0){
                        binding.ppJumlah.text = pref.prefjpresesnsi.toString()
                    } else {
                        pref.prefjpekerjaan = 0
                        binding.ppJumlah.text = pref.prefjpresesnsi.toString()
                        binding.emptyView.visibility = View.VISIBLE
                    }
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