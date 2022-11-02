package org.d3ifcool.jobmonitoring.ui.divisi

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
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.AdapterDivisi
import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.data.SubmitDivisiModel
import org.d3ifcool.jobmonitoring.databinding.FragmentDivisiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DivisiFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private lateinit var divisiAdapter: AdapterDivisi
    private var _binding: FragmentDivisiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDivisiBinding.inflate(inflater, container, false)
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

        divisiAdapter = AdapterDivisi(arrayListOf(), object : AdapterDivisi.OnAdapterListener {

            override fun popupMenus(divisi: DivisiModel.Data, v: View) {
                val popupMenus = PopupMenu(context, v)
                popupMenus.inflate(R.menu.main_menu)
                popupMenus.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_divisi -> {
                            val td_divisi = divisi.divisi
                            setFragmentResult(
                                "divisi",
                                bundleOf("divisi" to td_divisi)
                            )

                            val td_id2 = divisi.id
                            setFragmentResult(
                                "id2",
                                bundleOf("id2" to td_id2)
                            )

                            findNavController().navigate(R.id.action_divisiFragment_to_editDivisiFragment)
                            true
                        }
                        R.id.delete_divisi -> {
                            AlertDialog.Builder(context).apply {
                                setMessage(R.string.pesan_hapus_divisi)
                                setPositiveButton("HAPUS") { _, _ ->
                                    api.delete(divisi.id!!)
                                        .enqueue(object : Callback<SubmitDivisiModel> {
                                            override fun onResponse(
                                                call: Call<SubmitDivisiModel>,
                                                response: Response<SubmitDivisiModel>
                                            ) {
                                                if (response.isSuccessful) {
                                                    Toast.makeText(
                                                        activity, "Divisi Telah Dihapus",
                                                        Toast.LENGTH_LONG).show()
                                                    getNode()
                                                } else
                                                    Toast.makeText(
                                                        activity, "Gagal",
                                                        Toast.LENGTH_LONG).show()
                                            }

                                            override fun onFailure(call: Call<SubmitDivisiModel>, t: Throwable) {

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
            adapter = divisiAdapter
            setHasFixedSize(true)
        }
        binding.swiperefreshlayout.setOnRefreshListener {
            getNode()
            binding.swiperefreshlayout.isRefreshing = false
        }

        binding.buttonTambahDivisi.setOnClickListener {
            it.findNavController().navigate(R.id.action_divisiFragment_to_tambahDivisiFragment)
        }
    }

    private fun getNode() {
        api.data().enqueue(object : Callback<DivisiModel> {
            override fun onResponse(call: Call<DivisiModel>, response: Response<DivisiModel>) {
                if (response.isSuccessful) {
                    val listData = response.body()!!.tabel_divisi

                    divisiAdapter.setData(listData)
                    binding.jumlahDivisi.text = listData.size.toString()
                }
            }

            override fun onFailure(call: Call<DivisiModel>, t: Throwable) {
                Toast.makeText(
                    activity, "Gagal Memuat",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

}

