package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel

class KaryawanAdapter (
    val data: ArrayList<KaryawanModel>,
    val listener : OnAdapterListener,

    ) : RecyclerView.Adapter<KaryawanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterKaryawanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val karyawan: KaryawanModel = data[position]
        holder.bind(karyawan)
        holder.menu.setOnClickListener { listener.popupMenus(karyawan,it) }
    }

    override fun getItemCount()= data.size


    class ViewHolder(private val itemBinding: AdapterKaryawanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(karyawan: KaryawanModel) {
            itemBinding.kpNama.text = karyawan.namaKaryawan
            itemBinding.kpEmail.text = karyawan.email
            itemBinding.kpDivisi.text = karyawan.divisi
        }
        val menu = itemBinding.kpTiti3
    }

    public fun setData(datas: List<KaryawanModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun popupMenus(karyawan : KaryawanModel, v : View)

    }

}