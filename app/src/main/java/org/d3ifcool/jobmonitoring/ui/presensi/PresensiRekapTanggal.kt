package org.d3ifcool.jobmonitoring.ui.presensi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
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
import org.d3ifcool.jobmonitoring.adapter.TanggalAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiRekapTanggalBinding
import org.d3ifcool.jobmonitoring.model.JadwalPresensiModel
import org.d3ifcool.jobmonitoring.model.Preference

class PresensiRekapTanggal : Fragment() {

    private var _binding: FragmentPresensiRekapTanggalBinding? = null
    private val binding get() = _binding!!

    private lateinit var tanggalAdapter: TanggalAdapter
    private lateinit var pref: Preference
    private val data = arrayListOf<JadwalPresensiModel>()
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPresensiRekapTanggalBinding.inflate(inflater, container, false)
        getTanggal()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutPekerjaanFilterDivisi.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutPekerjaanFilterDivisi.isRefreshing = false
        }

        tanggalAdapter = TanggalAdapter(arrayListOf(), object : TanggalAdapter.OnAdapterListener {
            override fun filter(jadwal: JadwalPresensiModel, v: View) {
                pref.prefrekaptanggalpresensi = jadwal.tanggal
                pref.prefpembedapresensi = "nofilter"
                pref.prefrekaptitlepresensi = "rekap"
                findNavController().navigate(R.id.action_presensiRekapTanggal_to_presensiFilterPresensiFragment)
            }
        })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = tanggalAdapter
            setHasFixedSize(true)
        }
    }

    private fun getTanggal() {
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef = database.getReference("Jadwal_Presensi").child(idPerusahaan!!)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(JadwalPresensiModel::class.java)
                        data.add(datas!!)
                    }
                    data.reverse()
                    tanggalAdapter.setData(data)
                    binding.emptyView.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}