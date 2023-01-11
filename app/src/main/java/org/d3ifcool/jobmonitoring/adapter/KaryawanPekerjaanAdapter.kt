package org.d3ifcool.jobmonitoring.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterKaryawanPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
class KaryawanPekerjaanAdapter (
    val datas: ArrayList<PekerjaanModel>,
    val listener : OnAdapterListener,

    ) : RecyclerView.Adapter<KaryawanPekerjaanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterKaryawanPekerjaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pekerjaan: PekerjaanModel = datas[position]
        holder.bind(pekerjaan)
        holder.coll.setOnClickListener { listener.detail(pekerjaan, it) }
    }

    override fun getItemCount()= datas.size


    class ViewHolder(private val itemBinding: AdapterKaryawanPekerjaanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        @SuppressLint("SetTextI18n")
        fun bind(pekerjaan: PekerjaanModel) {
            val statuss = pekerjaan.status
            itemBinding.kpkPekerjaanSelesai.text = pekerjaan.nama_pekerjaan
            if(statuss == "1"){
                itemBinding.kpkStatusSelesai.text = "Selesai"
            } else if (statuss == "0"){
                itemBinding.kpkStatusBelumselesai.text = "Belum Selesai"
            }
        }
        val coll = itemBinding.kpkCollPekerjaan
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<PekerjaanModel>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun detail(pekerjaan : PekerjaanModel, v : View)

    }
}