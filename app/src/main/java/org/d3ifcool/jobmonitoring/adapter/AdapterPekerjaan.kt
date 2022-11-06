package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.data.PekerjaanModel
import org.d3ifcool.jobmonitoring.databinding.AdapterPekerjaanBinding

class AdapterPekerjaan (
    val datas: ArrayList<PekerjaanModel.Data>,
    val listener : OnAdapterListener,

    ) : RecyclerView.Adapter<AdapterPekerjaan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterPekerjaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pekerjaan: PekerjaanModel.Data = datas[position]
        holder.bind(pekerjaan)
        holder.menu.setOnClickListener { listener.popupMenus(pekerjaan,it) }
    }

    override fun getItemCount()= datas.size


    class ViewHolder(private val itemBinding: AdapterPekerjaanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(pekerjaan: PekerjaanModel.Data) {
            val statuss = pekerjaan.status
            if(statuss == "1"){
                itemBinding.statusPekerjaan.text = "Selesai"
                itemBinding.namaKaryawan.text = pekerjaan.karyawan
                itemBinding.data1Pekerjaan.text = pekerjaan.nama_pekerjaan
            } else{
                itemBinding.statusPekerjaanBerjalan.text = "Berjalan"
                itemBinding.namaKaryawanBerjalan.text = pekerjaan.karyawan
                itemBinding.data1PekerjaanBerjalan.text = pekerjaan.nama_pekerjaan
            }
        }
        val menu = itemBinding.pekerjaanIconEdit
    }

    public fun setData(data: List<PekerjaanModel.Data>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun popupMenus(pekerjaan : PekerjaanModel.Data, v : View)

    }
}