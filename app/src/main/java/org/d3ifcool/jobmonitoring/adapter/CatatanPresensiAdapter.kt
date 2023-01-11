package org.d3ifcool.jobmonitoring.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterCatatanPresensiBinding
import org.d3ifcool.jobmonitoring.model.PresensiModel
class CatatanPresensiAdapter(
    val data: ArrayList<PresensiModel>,
    ) : RecyclerView.Adapter<CatatanPresensiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterCatatanPresensiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val presensi: PresensiModel = data[position]
        holder.bind(presensi)

    }

    class ViewHolder(private val itemBinding: AdapterCatatanPresensiBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(presensi: PresensiModel) {
            itemBinding.catatan.text = presensi.tanggal
            itemBinding.waktumasuk.text = presensi.waktumasuk
            itemBinding.waktukeluar.text = presensi.waktukeluar
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(datas: List<PresensiModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }
}