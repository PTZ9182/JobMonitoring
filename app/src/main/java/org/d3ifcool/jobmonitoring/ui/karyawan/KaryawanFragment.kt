package org.d3ifcool.jobmonitoring.ui.karyawan

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
import org.d3ifcool.jobmonitoring.adapter.KaryawanAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class KaryawanFragment : Fragment(){

    private var _binding: FragmentKaryawanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    private lateinit var pref: Preference
    private lateinit var karyawanAdapter: KaryawanAdapter
    private val data = arrayListOf<KaryawanModel>()
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanBinding.inflate(inflater, container, false)
        getKaryawan("")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutKaryawanPerusahaan.setOnRefreshListener {
            binding.layoutKaryawanPerusahaan.isRefreshing = false
        }
        binding.kpButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_karyawanFragment_to_tambahKaryawanFragment)
        }
        binding.kpCollFillter.setOnClickListener {
            findNavController().navigate(R.id.action_karyawanFragment_to_karyawanFilterFragment)
        }

        searchView = view.findViewById(R.id.kp_search)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getKaryawan(query!!)
                return false
            }

            override fun onQueryTextChange(newtext: String?): Boolean {
                getKaryawan(newtext!!)
                return false
            }

        })
        karyawanAdapter = KaryawanAdapter(arrayListOf(),object : KaryawanAdapter.OnAdapterListener{
            override fun popupMenus(karyawan: KaryawanModel, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.karyawan_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_Karyawan -> {
                            pref.prefidkaryawan = karyawan.id
                            pref.prefnamakaryawan = karyawan.namaKaryawan
                            pref.preftanggallahirkaryawan = karyawan.tanggallahir
                            pref.prefjeniskelaminkaryawan = karyawan.jenisKelamin
                            pref.prefalamatkaryawan = karyawan.alamat
                            pref.prefnohpkaryawan = karyawan.nohandphone
                            pref.prefiddivisikaryawan= karyawan.iddivisi
                            pref.prefemailkaryawan = karyawan.email
                            pref.prefpasswordkaryawan = karyawan.password
                            findNavController().navigate(R.id.action_karyawanFragment_to_editKaryawanFragment)
                            true
                        }
                        R.id.delete_Karyawan -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_karyawan)
                                setPositiveButton("HAPUS") { _, _ ->
                                    val user = Firebase.auth.currentUser
                                    val idPerusahaan = user?.uid
                                    val dbRef = database.getReference("Karyawan").child(idPerusahaan!!).child(karyawan.id)
                                    val task = dbRef.removeValue()
                                    task.addOnSuccessListener{
                                        Toast.makeText(activity,"Karyawan Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener{ tast ->
                                        Toast.makeText(activity,"Gagal Menghapus Karyawan${tast.message}", Toast.LENGTH_SHORT).show()
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

            override fun profil(karyawan: KaryawanModel, v: View) {
                pref.prefidkaryawan = karyawan.id
                pref.prefnamakaryawan = karyawan.namaKaryawan
                pref.preftanggallahirkaryawan = karyawan.tanggallahir
                pref.prefjeniskelaminkaryawan = karyawan.jenisKelamin
                pref.prefalamatkaryawan = karyawan.alamat
                pref.prefnohpkaryawan = karyawan.nohandphone
                pref.prefiddivisikaryawan= karyawan.iddivisi
                pref.prefemailkaryawan = karyawan.email
                pref.prefpasswordkaryawan = karyawan.password
                findNavController().navigate(R.id.action_karyawanFragment_to_profilKaryawan)
            }

        })
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = karyawanAdapter
            setHasFixedSize(true)
        }

    }
    private fun getKaryawan(text: String){
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idperusahan = user?.uid
        val dbRef = database.getReference("Karyawan").child(idperusahan!!).orderByChild("namaKaryawan")
            .startAt(text).endAt(text + "\uf8ff")
        dbRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        data.add(datas!!)
                        pref.prefjkaryawan = data.size
                    }
                    karyawanAdapter.setData(data)
                    binding.emptyView.visibility = View.GONE
                    if (data.size != 0){
                        binding.kpJumlah.text = pref.prefjkaryawan.toString()
                    } else {
                        pref.prefjkaryawan = 0
                        binding.kpJumlah.text = pref.prefjkaryawan.toString()
                        binding.emptyView.visibility = View.VISIBLE
                    }
                } else {
                    pref.prefjkaryawan = 0
                    binding.kpJumlah.text = pref.prefjkaryawan.toString()
                    karyawanAdapter.setData(data)
                    binding.emptyView.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}