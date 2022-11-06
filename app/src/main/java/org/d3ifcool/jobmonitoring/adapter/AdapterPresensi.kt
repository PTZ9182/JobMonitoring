package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.AdapterPresensiBinding
import org.d3ifcool.jobmonitoring.model.PresensiModel

class AdapterPresensi (
    val datas: ArrayList<PresensiModel.Data>,

    ) : RecyclerView.Adapter<AdapterPresensi.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterPresensiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val presensi: PresensiModel.Data = datas[position]
        holder.bind(presensi)
        holder.menu.setOnClickListener { it.findNavController().navigate(R.id.action_presensiFragment_to_presensiKaryawanFragment) }
    }

    override fun getItemCount() = datas.size

    class ViewHolder(private val itemBinding: AdapterPresensiBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(presensi: PresensiModel.Data) {
            val statuss = presensi.status
            if (statuss == "1"){
                itemBinding.statusPekerjaan.text = "Hadir"
                itemBinding.namaKaryawan.text = presensi.nama_karyawan
            }else{
                itemBinding.statusPekerjaanBerjalan.text = "Tidak Hadir"
                itemBinding.namaKaryawan.text = presensi.nama_karyawan
            }
        }
        val menu = itemBinding.coliderPekerjaan
    }

    public fun setData(data: List<PresensiModel.Data>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }


}