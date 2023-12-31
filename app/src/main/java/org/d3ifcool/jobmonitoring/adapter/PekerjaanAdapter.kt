package org.d3ifcool.jobmonitoring.adapter

import android.annotation.SuppressLint
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
import org.d3ifcool.jobmonitoring.databinding.AdapterPekerjaanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
class PekerjaanAdapter (
    val datas: ArrayList<PekerjaanModel>,
    val listener : OnAdapterListener,

) : RecyclerView.Adapter<PekerjaanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterPekerjaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pekerjaan: PekerjaanModel = datas[position]
        holder.bind(pekerjaan)
        holder.menu.setOnClickListener { listener.popupMenus(pekerjaan,it) }
        holder.coll.setOnClickListener { listener.detail(pekerjaan,it) }
    }

    override fun getItemCount()= datas.size


    class ViewHolder(private val itemBinding: AdapterPekerjaanBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        @SuppressLint("SetTextI18n")
        fun bind(pekerjaan: PekerjaanModel) {
            val database = Firebase.database
            itemBinding.pkPekerjaanSelesai.text = pekerjaan.nama_pekerjaan
            val statuss = pekerjaan.status
            if(statuss == "1"){
                itemBinding.pkStatusSelesai.text = "Selesai"
            } else if (statuss == "0"){
                itemBinding.pkStatusBelumselesai.text = "Belum Selesai"
            }
            val user = Firebase.auth.currentUser
            val idPerusahaan = user?.uid
            val dbRef =
                database.getReference("Divisi").child(idPerusahaan!!).orderByChild("id").equalTo(pekerjaan.iddivisi)
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (datasnap in snapshot.children) {
                            val datas = datasnap.getValue(DivisiModel::class.java)
                            itemBinding.pkNamaKaryawanSelesai.text = datas!!.divisi
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
        val menu = itemBinding.pkTitik3
        val coll = itemBinding.pkCollPekerjaan
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<PekerjaanModel>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{

        fun popupMenus(pekerjaan : PekerjaanModel, v : View)
        fun detail(pekerjaan : PekerjaanModel, v : View)

    }
}