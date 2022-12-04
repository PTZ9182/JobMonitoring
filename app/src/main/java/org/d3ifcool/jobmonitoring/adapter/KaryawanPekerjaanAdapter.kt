package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterKaryawanPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel

class KaryawanPekerjaanAdapter (
    val datas: ArrayList<PekerjaanModel>,

    ) : RecyclerView.Adapter<KaryawanPekerjaanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterKaryawanPekerjaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pekerjaan: PekerjaanModel = datas[position]
        holder.bind(pekerjaan)
    }

    override fun getItemCount()= datas.size


    class ViewHolder(private val itemBinding: AdapterKaryawanPekerjaanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(pekerjaan: PekerjaanModel) {
            val statuss = pekerjaan.status
            if(statuss == "1"){
                itemBinding.kpkStatusSelesai.text = "Selesai"
                itemBinding.kpkPekerjaanSelesai.text = pekerjaan.nama_pekerjaan
            } else if (statuss == "0"){
                itemBinding.kpkStatusBelumselesai.text = "Belum Selesai"
                itemBinding.kpkPekerjaanBelumselesai.text = pekerjaan.nama_pekerjaan
            }
        }
    }

    public fun setData(data: List<PekerjaanModel>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }
}