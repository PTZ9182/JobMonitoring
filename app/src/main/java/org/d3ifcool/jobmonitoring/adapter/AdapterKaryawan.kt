package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.data.KaryawanModel
import org.d3ifcool.jobmonitoring.databinding.AdapterKaryawanBinding

class AdapterKaryawan (
    val datas: ArrayList<KaryawanModel.Data>,
    val listener : OnAdapterListener,

) : RecyclerView.Adapter<AdapterKaryawan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterKaryawanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val karyawan: KaryawanModel.Data = datas[position]
        holder.bind(karyawan)
        holder.menu.setOnClickListener { listener.popupMenus(karyawan,it) }
    }

    override fun getItemCount()= datas.size


    class ViewHolder(private val itemBinding: AdapterKaryawanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(karyawan: KaryawanModel.Data) {
            itemBinding.namaKaryawan.text = karyawan.nama_karyawan
            itemBinding.emailKaryawan.text = karyawan.email
            itemBinding.divisiKaryawan.text = karyawan.divisi
        }
        val menu = itemBinding.karyawanIconEdit
    }

    public fun setData(data: List<KaryawanModel.Data>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun popupMenus(karyawan : KaryawanModel.Data, v : View)

    }

}