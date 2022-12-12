package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterKaryawanFilterBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel

class KaryawanFilterAdapter(
    val data: ArrayList<DivisiModel>,
    val listener: OnAdapterListener,

    ) : RecyclerView.Adapter<KaryawanFilterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterKaryawanFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val divisi: DivisiModel = data[position]
        holder.bind(divisi)
        holder.coll.setOnClickListener { listener.filter(divisi, it) }

    }

    class ViewHolder(private val itemBinding: AdapterKaryawanFilterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(divisi: DivisiModel) {
            itemBinding.fkNamadivisi.text = divisi.divisi
        }

        val coll = itemBinding.fkCollDivisi
    }

    fun setData(datas: List<DivisiModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun filter(divisi : DivisiModel, v : View)

    }
}