package org.d3ifcool.jobmonitoring.ui.karyawan

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.AdapterKaryawan
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.data.KaryawanModel
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KaryawanFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var karyawanAdapter: AdapterKaryawan
    private var _binding: FragmentKaryawanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getNode()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        karyawanAdapter = AdapterKaryawan(arrayListOf(),object : AdapterKaryawan.OnAdapterListener {
            override fun popupMenus(karyawan: KaryawanModel.Data, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.karyawan_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_Karyawan -> {
                            val kid = karyawan.id
                            setFragmentResult(
                                "id",
                                bundleOf("id" to kid)
                            )

                            val kNkaryawan = karyawan.nama_karyawan
                            setFragmentResult(
                                "nama_karyawan",
                                bundleOf("nama_karyawan" to kNkaryawan)
                            )
                            val kTLahir = karyawan.tanggal_lahir
                            setFragmentResult(
                                "tanggal_lahir",
                                bundleOf("tanggal_lahir" to kTLahir)
                            )

                            val kJkelamin = karyawan.jenis_kelamin
                            setFragmentResult(
                                "jenis_kelamin",
                                bundleOf("jenis_kelamin" to kJkelamin)
                            )
                            val kAlamat = karyawan.alamat
                            setFragmentResult(
                                "alamat",
                                bundleOf("alamat" to kAlamat)
                            )

                            val kNHp = karyawan.no_hp
                            setFragmentResult(
                                "no_hp",
                                bundleOf("no_hp" to kNHp)
                            )
                            val kDivisi = karyawan.divisi
                            setFragmentResult(
                                "divisi",
                                bundleOf("divisi" to kDivisi)
                            )

                            val kEmail = karyawan.email
                            setFragmentResult(
                                "email",
                                bundleOf("email" to kEmail)
                            )
                            val kPassword = karyawan.password
                            setFragmentResult(
                                "password",
                                bundleOf("password" to kPassword)
                            )
                            findNavController().navigate(R.id.action_karyawanFragment_to_editKaryawanFragment)
                            true
                        }
                        R.id.delete_Karyawan -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_karyawan)
                                setPositiveButton("HAPUS") { _, _ ->
                                    api.deleteKaryawan(karyawan.id!!)
                                        .enqueue(object : Callback<KaryawanModel> {
                                            override fun onResponse(
                                                call: Call<KaryawanModel>,
                                                response: Response<KaryawanModel>
                                            ) {
                                                if (response.isSuccessful) {
                                                    Toast.makeText(
                                                        activity, "Karyawan Telah Dihapus",
                                                        Toast.LENGTH_LONG).show()
                                                    getNode()
                                                } else
                                                    Toast.makeText(
                                                        activity, "Gagal",
                                                        Toast.LENGTH_LONG).show()
                                            }

                                            override fun onFailure(call: Call<KaryawanModel>, t: Throwable) {

                                            }
                                        })

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
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    context,
                    androidx.recyclerview.widget.RecyclerView.VERTICAL
                )
            )
            adapter = karyawanAdapter
            setHasFixedSize(true)
        }
        binding.layoutKaryawan.setOnRefreshListener {
            getNode()
            binding.layoutKaryawan.isRefreshing = false
        }
        binding.buttonTambahKaryawan.setOnClickListener {
            it.findNavController().navigate(R.id.action_karyawanFragment_to_tambahKaryawanFragment)
        }
    }

    private fun getNode() {
        api.dataKaryawan().enqueue(object : Callback<KaryawanModel> {
            override fun onResponse(call: Call<KaryawanModel>, response: Response<KaryawanModel>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!.tabel_karyawan

                    karyawanAdapter.setData(listData)
                    binding.jumlahKaryawan.text = listData.size.toString()
                }
            }

            override fun onFailure(call: Call<KaryawanModel>, t: Throwable) {
                Toast.makeText(
                    activity, "Gagal Memuat",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}