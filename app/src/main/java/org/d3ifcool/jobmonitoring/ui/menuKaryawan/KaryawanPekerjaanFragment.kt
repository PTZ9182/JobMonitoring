package org.d3ifcool.jobmonitoring.ui.menuKaryawan

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.KaryawanAdapter
import org.d3ifcool.jobmonitoring.adapter.KaryawanPekerjaanAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class KaryawanPekerjaanFragment: Fragment() {

    private var _binding: FragmentKaryawanPekerjaanBinding? = null
    private val binding get() = _binding!!

    private lateinit var karyawanPekerjaanAdapter: KaryawanPekerjaanAdapter
    private val data = arrayListOf<PekerjaanModel>()

    private lateinit var pref: Preference
    val database = Firebase.database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanPekerjaanBinding.inflate(inflater, container, false)
        getPekerjaan()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutKaryawanPekerjaan.setOnRefreshListener {
            binding.layoutKaryawanPekerjaan.isRefreshing = false
        }

        karyawanPekerjaanAdapter = KaryawanPekerjaanAdapter(arrayListOf(),object : KaryawanPekerjaanAdapter.OnAdapterListener{
            override fun detail(pekerjaan: PekerjaanModel, v: View) {
                val context: Context
                context = requireActivity()
                pref = Preference(context)

                pref.prefnamapekerjaanuser = pekerjaan.nama_pekerjaan
                pref.prefdeskripsipekerjaanuser = pekerjaan.deskripsi
                pref.prefstatuspekerjaanuser = pekerjaan.status

                pref.prefidpekerjaanuser = pekerjaan.id
                pref.prefdivisipekerjaanuser = pekerjaan.divisi
                pref.prefkaryawanpekerjaanuser = pekerjaan.karyawan
                findNavController().navigate(R.id.action_karyawanPekerjaanFragment_to_karyawanPekerjaanDetail)
            }
        })
                with(binding.recyclerView) {
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    context,
                    androidx.recyclerview.widget.RecyclerView.VERTICAL
                )
            )
            adapter = karyawanPekerjaanAdapter
            setHasFixedSize(true)
        }
    }

    private fun getPekerjaan(){
        val context: Context
        context = requireActivity()
        pref = Preference(context)
        val name = pref.prefnamauser
        val idperushaan =pref.prefidperusahaanuser
        val dbRef = database.getReference("Pekerjaan").child(idperushaan!!).orderByChild("karyawan").equalTo(name)
        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        Log.i("kpk","$snapshot")
                        val datas = datasnap.getValue(PekerjaanModel::class.java)
                        data.add(datas!!)
                    }
                    karyawanPekerjaanAdapter.setData(data)
                    binding.emptyView.visibility = View.GONE
                } else {
                    karyawanPekerjaanAdapter.setData(data)
                    binding.emptyView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }


}
