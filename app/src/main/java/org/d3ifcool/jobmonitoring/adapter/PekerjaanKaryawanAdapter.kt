package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterPekerjaanKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel

class PekerjaanKaryawanAdapter  (
    val data: ArrayList<KaryawanModel>,
    val listener : OnAdapterListener,

    ) : RecyclerView.Adapter<PekerjaanKaryawanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterPekerjaanKaryawanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val karyawan: KaryawanModel = data[position]
        holder.bind(karyawan)
        holder.coll.setOnClickListener { listener.filter(karyawan, it) }

    }

    class ViewHolder(private val itemBinding: AdapterPekerjaanKaryawanBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(karyawan: KaryawanModel) {
            itemBinding.ppfkNamakaryawan.text = karyawan.namaKaryawan
        }

        val coll = itemBinding.ppfkCollKaryawan
    }

    fun setData(datas: List<KaryawanModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun filter(karyawan : KaryawanModel, v : View)

    }
}