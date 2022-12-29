package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import org.d3ifcool.jobmonitoring.adapter.PekerjaanFilterDivisiAdapter
import org.d3ifcool.jobmonitoring.adapter.PekerjaanKaryawanAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanFilterDivisiBinding
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanFilterKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class PekerjaanFilterKaryawanFargment : Fragment() {

    private var _binding: FragmentPekerjaanFilterKaryawanBinding? = null
    private val binding get() = _binding!!

    private lateinit var pekerjaanKaryawanAdapter: PekerjaanKaryawanAdapter
    private lateinit var pref: Preference
    private val data = arrayListOf<KaryawanModel>()
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanFilterKaryawanBinding.inflate(inflater, container, false)
        getKaryawan()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutPekerjaanFilterDivisi.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutPekerjaanFilterDivisi.isRefreshing = false
        }

        binding.ppfkBedasarkandivisi.text = pref.preffilterkaryawan

        pekerjaanKaryawanAdapter = PekerjaanKaryawanAdapter(arrayListOf(),object : PekerjaanKaryawanAdapter.OnAdapterListener {
            override fun filter(karyawan: KaryawanModel, v: View) {
                pref.prefidkaryawanpekerjaan = karyawan.id
                pref.prefnamakaryawanpekerjaan = karyawan.namaKaryawan
                findNavController().navigate(R.id.action_pekerjaanFilterKaryawanFargment_to_pekerjaanFilterFragment)
            }
        })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = pekerjaanKaryawanAdapter
            setHasFixedSize(true)
        }
    }

    private fun getKaryawan() {
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val divisi = pref.prefiddivisipekerjaan
        val dbRef = database.getReference("Karyawan").child(idPerusahaan!!).orderByChild("iddivisi").equalTo(divisi)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        data.add(datas!!)
                    }
                   pekerjaanKaryawanAdapter.setData(data)
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