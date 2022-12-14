package org.d3ifcool.jobmonitoring.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.AdapterPresensiBinding
import org.d3ifcool.jobmonitoring.model.PresensiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PresensiAdapter (
    val data: ArrayList<PresensiModel>,
    val listener : OnAdapterListener,

    ) : RecyclerView.Adapter<PresensiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterPresensiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val presensi: PresensiModel = data[position]
        holder.bind(presensi)
        holder.coll.setOnClickListener { listener.presensidata(presensi, it) }
    }

    override fun getItemCount()= data.size


    class ViewHolder(private val itemBinding: AdapterPresensiBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(presensi: PresensiModel) {
            val storage: FirebaseStorage = Firebase.storage
            itemBinding.ppNamaHadir.text = presensi.nama
            itemBinding.ppHadir.text = "Masuk :"
            itemBinding.waktu.text = presensi.waktu
            val storageRef = storage.getReference("images").child("Karyawan").child(presensi.idUser).child("profil")
            storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                itemBinding.ppImg.setImageBitmap(bitmap)
            }.addOnFailureListener {
            }
        }
        val coll = itemBinding.collPresensi
    }

    public fun setData(datas: List<PresensiModel>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun presensidata(presensi : PresensiModel, v : View)

    }

}