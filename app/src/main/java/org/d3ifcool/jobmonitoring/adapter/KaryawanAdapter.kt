package org.d3ifcool.jobmonitoring.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
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
        holder.coll.setOnClickListener { listener.profil(karyawan, it) }
    }

    override fun getItemCount()= data.size


    class ViewHolder(private val itemBinding: AdapterKaryawanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(karyawan: KaryawanModel) {
            val storage: FirebaseStorage = Firebase.storage
            itemBinding.kpNama.text = karyawan.namaKaryawan
            itemBinding.kpEmail.text = karyawan.email
            itemBinding.kpDivisi.text = karyawan.divisi
            val storageRef = storage.getReference("images").child("Karyawan").child(karyawan.id).child("profil")
            storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                itemBinding.kpImg.setImageBitmap(bitmap)
            }.addOnFailureListener {
            }
        }
        val menu = itemBinding.kpTiti3
        val coll = itemBinding.collKaryawan
    }

    public fun setData(datas: List<KaryawanModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun popupMenus(karyawan : KaryawanModel, v : View)
        fun profil(karyawan : KaryawanModel, v : View)

    }

}