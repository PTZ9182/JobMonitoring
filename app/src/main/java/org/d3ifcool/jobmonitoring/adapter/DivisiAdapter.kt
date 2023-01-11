package org.d3ifcool.jobmonitoring.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.databinding.AdapterDivisiBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
class DivisiAdapter (
    val data: ArrayList<DivisiModel>,
    val listener : OnAdapterListener,
    ) : RecyclerView.Adapter<DivisiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterDivisiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(binding)
    }
    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder:  ViewHolder, position: Int) {
        val divisi: DivisiModel = data[position]
        holder.bind(divisi)
        holder.menu.setOnClickListener { listener.popupMenus(divisi,it) }
    }

    class ViewHolder(private val itemBinding: AdapterDivisiBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(divisi: DivisiModel) {
            itemBinding.dpNamadivisi.text = divisi.divisi
        }
        val menu = itemBinding.dpTitik3
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(datas: List<DivisiModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun popupMenus(divisi : DivisiModel, v : View)
    }
}