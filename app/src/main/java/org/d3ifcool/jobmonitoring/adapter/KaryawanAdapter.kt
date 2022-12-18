package org.d3ifcool.jobmonitoring.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.AdapterKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
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
            val database = Firebase.database
            val storage: FirebaseStorage = Firebase.storage

            itemBinding.kpNama.text = karyawan.namaKaryawan
            itemBinding.kpEmail.text = karyawan.email

            val user = Firebase.auth.currentUser
            val idPerusahaan = user?.uid
            val dbRef =
                database.getReference("Divisi").child(idPerusahaan!!).orderByChild("id").equalTo(karyawan.iddivisi)
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (datasnap in snapshot.children) {
                            val datas = datasnap.getValue(DivisiModel::class.java)
                            itemBinding.kpDivisi.text = datas!!.divisi
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
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