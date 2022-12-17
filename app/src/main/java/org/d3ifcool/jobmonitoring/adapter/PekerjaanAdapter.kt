package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel

class PekerjaanAdapter (
    val datas: ArrayList<PekerjaanModel>,
    val listener : OnAdapterListener,

) : RecyclerView.Adapter<PekerjaanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterPekerjaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pekerjaan: PekerjaanModel = datas[position]
        holder.bind(pekerjaan)
        holder.menu.setOnClickListener { listener.popupMenus(pekerjaan,it) }
        holder.coll.setOnClickListener { listener.detail(pekerjaan,it) }
    }

    override fun getItemCount()= datas.size


    class ViewHolder(private val itemBinding: AdapterPekerjaanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(pekerjaan: PekerjaanModel) {
            itemBinding.pkNamaKaryawanSelesai.text = pekerjaan.divisi
            itemBinding.pkPekerjaanSelesai.text = pekerjaan.nama_pekerjaan
            val statuss = pekerjaan.status
            if(statuss == "1"){
                itemBinding.pkStatusSelesai.text = "Selesai"
            } else if (statuss == "0"){
                itemBinding.pkStatusBelumselesai.text = "Belum Selesai"
            }
        }
        val menu = itemBinding.pkTitik3
        val coll = itemBinding.pkCollPekerjaan
    }

    public fun setData(data: List<PekerjaanModel>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun popupMenus(pekerjaan : PekerjaanModel, v : View)
        fun detail(pekerjaan : PekerjaanModel, v : View)

    }
}