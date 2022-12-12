package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahPekerjaanDivisiBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.Preference

class TambahPekerjaanDivisiFragment : Fragment() {

    private var _binding: FragmentTambahPekerjaanDivisiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    private lateinit var pref: Preference
    private lateinit var divisiAdapter: PekerjaanFilterDivisiAdapter
    private val data = arrayListOf<DivisiModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTambahPekerjaanDivisiBinding.inflate(inflater, container, false)
        getDivisi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutKaryawanFilter.setOnRefreshListener {
            binding.layoutKaryawanFilter.isRefreshing = false
        }

        divisiAdapter = PekerjaanFilterDivisiAdapter(arrayListOf(),object : PekerjaanFilterDivisiAdapter.OnAdapterListener {

            override fun filter(divisi: DivisiModel, v: View) {
                pref.prefdivisipekerjaan = divisi.divisi
                findNavController().navigate(R.id.action_tambahPekerjaanDivisiFragment_to_tambahPekerjaanFragment)

            }
        })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = divisiAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDivisi() {
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef = database.getReference("Divisi").child(idPerusahaan!!).orderByChild("divisi")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        data.add(datas!!)
                    }
                    divisiAdapter.setData(data)
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
