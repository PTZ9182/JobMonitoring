package org.d3ifcool.jobmonitoring.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.databinding.AdapterDivisiBinding
import android.content.Context as Context


class AdapterDivisi(
    val tabel_divisi: ArrayList<DivisiModel.Data>,
    val c: Context,

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
        holder.menu.setOnClickListener { popupMenus(it) }
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

    private fun popupMenus(v: View) {
        val popupMenus = PopupMenu(c,v)
        popupMenus.inflate(R.menu.main_menu)
        popupMenus.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_divisi -> {
                    Toast.makeText(c, "Edit", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.delete_divisi -> {
                    Toast.makeText(c, "Delete", Toast.LENGTH_LONG).show()
                    true
                }
                else -> true
            }
        }
        popupMenus.show()
    }

}