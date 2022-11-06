package org.d3ifcool.jobmonitoring.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.databinding.AdapterDivisiBinding


class AdapterDivisi(
    val tabel_divisi: ArrayList<DivisiModel.Data>,
    val listener : OnAdapterListener,

) : RecyclerView.Adapter<AdapterDivisi.DivisiHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivisiHolder {
        val binding =
            AdapterDivisiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DivisiHolder(binding)

    }
    override fun getItemCount(): Int = tabel_divisi.size

    override fun onBindViewHolder(holder: DivisiHolder, position: Int) {
        val divisi: DivisiModel.Data = tabel_divisi[position]
        holder.bind(divisi)
        holder.menu.setOnClickListener { listener.popupMenus(divisi,it) }

    }

    class DivisiHolder(private val itemBinding: AdapterDivisiBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(divisi: DivisiModel.Data) {
            itemBinding.data1Divisi.text = divisi.divisi
        }
        val menu = itemBinding.divisiIconEdit
    }

    public fun setData(data: List<DivisiModel.Data>) {
        tabel_divisi.clear()
        tabel_divisi.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun popupMenus(divisi : DivisiModel.Data, v : View)

    }
}