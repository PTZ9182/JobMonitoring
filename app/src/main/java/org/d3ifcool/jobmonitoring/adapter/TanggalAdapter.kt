package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterTanggalBinding
import org.d3ifcool.jobmonitoring.model.JadwalPresensiModel

class TanggalAdapter (
    val data: ArrayList<JadwalPresensiModel>,
    val listener : OnAdapterListener,

    ) : RecyclerView.Adapter<TanggalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterTanggalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jadwal: JadwalPresensiModel = data[position]
        holder.bind(jadwal)
        holder.coll.setOnClickListener { listener.filter(jadwal, it) }

    }

    class ViewHolder(private val itemBinding: AdapterTanggalBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(jadwal: JadwalPresensiModel) {
            itemBinding.rkp.text = jadwal.tanggal
        }

        val coll = itemBinding.ppfkCollTanggal
    }

    fun setData(datas: List<JadwalPresensiModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun filter(jadwal : JadwalPresensiModel, v : View)

    }
}