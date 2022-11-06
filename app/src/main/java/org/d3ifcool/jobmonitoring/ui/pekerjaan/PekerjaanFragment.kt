package org.d3ifcool.jobmonitoring.ui.pekerjaan

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.AdapterPekerjaan
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.data.PekerjaanModel
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PekerjaanFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var pekerjaanAdapter: AdapterPekerjaan
    private var _binding: FragmentPekerjaanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPekerjaanBinding.inflate(inflater, container, false)
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

        pekerjaanAdapter = AdapterPekerjaan(arrayListOf(), object  : AdapterPekerjaan.OnAdapterListener {
            override fun popupMenus(pekerjaan: PekerjaanModel.Data, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.pekerjaan_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_pekerjaan -> {
                            val pid = pekerjaan.id
                            setFragmentResult(
                                "id",
                                bundleOf("id" to pid)
                            )

                            val pnpekerjaan = pekerjaan.nama_pekerjaan
                            setFragmentResult(
                                "nama_pekerjaan",
                                bundleOf("nama_pekerjaan" to pnpekerjaan)
                            )
                            val pdeskripsi = pekerjaan.deskripsi
                            setFragmentResult(
                                "deskripsi",
                                bundleOf("deskripsi" to pdeskripsi)
                            )

                            val pkaryawan = pekerjaan.karyawan
                            setFragmentResult(
                                "karyawan",
                                bundleOf("karyawan" to pkaryawan)
                            )
                            findNavController().navigate(R.id.action_pekerjaanFragment_to_editPekerjaanFragment)
                            true
                        }
                        R.id.delete_pekerjaan -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_pekerjaan)
                                setPositiveButton("HAPUS") { _, _ ->
                                    api.deletePekerjaan(pekerjaan.id!!)
                                        .enqueue(object : Callback<PekerjaanModel> {
                                            override fun onResponse(
                                                call: Call<PekerjaanModel>,
                                                response: Response<PekerjaanModel>
                                            ) {
                                                if (response.isSuccessful) {
                                                    Toast.makeText(
                                                        activity, "Pekerjaan Telah Dihapus",
                                                        Toast.LENGTH_LONG).show()
                                                    getNode()
                                                } else
                                                    Toast.makeText(
                                                        activity, "Gagal",
                                                        Toast.LENGTH_LONG).show()
                                            }

                                            override fun onFailure(call: Call<PekerjaanModel>, t: Throwable) {

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
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = pekerjaanAdapter
            setHasFixedSize(true)
        }
        binding.layoutPekerjaan.setOnRefreshListener {
            binding.layoutPekerjaan.isRefreshing = false
        }

        binding.buttonTambahPekerjaan.setOnClickListener {
            it.findNavController().navigate(R.id.action_pekerjaanFragment_to_tambahPekerjaanFragment)
        }
    }

    private fun getNode() {
        api.dataPekerjaan().enqueue(object : Callback<PekerjaanModel> {
            override fun onResponse(call: Call<PekerjaanModel>, response: Response<PekerjaanModel>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!.tabel_pekerjaan

                    pekerjaanAdapter.setData(listData)
                    binding.jumlahPekerjaan.text = listData.size.toString()
                    binding.status
                }
            }

            override fun onFailure(call: Call<PekerjaanModel>, t: Throwable) {
                Toast.makeText(
                    activity, "Gagal Memuat",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}