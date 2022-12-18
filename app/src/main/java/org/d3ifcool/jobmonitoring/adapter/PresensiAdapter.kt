package org.d3ifcool.jobmonitoring.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.AdapterPresensiBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
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
        @SuppressLint("SetTextI18n")
        fun bind(presensi: PresensiModel) {
            val storage: FirebaseStorage = Firebase.storage
            val database = Firebase.database
            val user = Firebase.auth.currentUser
            val idPerusahaan = user?.uid
            val dbReff = database.getReference("Karyawan").child(idPerusahaan!!).orderByChild("id").equalTo(presensi.idkaryawan)
            dbReff.addValueEventListener(object  : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for (datasnap in snapshot.children){
                            val datas = datasnap.getValue(KaryawanModel::class.java)
                            itemBinding.ppNamaHadir.text = datas!!.namaKaryawan
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
            itemBinding.ppHadir.text = "Masuk :"
            itemBinding.waktu.text = presensi.waktu
            val storageRef = storage.getReference("images").child("Karyawan").child(presensi.idkaryawan).child("profil")
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