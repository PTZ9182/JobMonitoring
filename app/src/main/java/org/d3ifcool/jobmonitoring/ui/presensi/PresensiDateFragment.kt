package org.d3ifcool.jobmonitoring.ui.presensi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.adapter.CatatanPresensiAdapter
import org.d3ifcool.jobmonitoring.adapter.KaryawanAdapter
import org.d3ifcool.jobmonitoring.databinding.FragmentPresensiDateBinding
import org.d3ifcool.jobmonitoring.model.JadwalPresensiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference
import org.d3ifcool.jobmonitoring.model.PresensiModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class PresensiDateFragment : Fragment() {

    private var _binding: FragmentPresensiDateBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: Preference
    val database = Firebase.database
    private val data = arrayListOf<PresensiModel>()
    private lateinit var catatanPresensiAdapter: CatatanPresensiAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPresensiDateBinding.inflate(inflater, container, false)
        getJpresensi()
        getcatatan()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutPekerjaanDetail.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutPekerjaanDetail.isRefreshing = false
        }


        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter)
        binding.tanggal.text = date
        val cal = Calendar.getInstance()

        val waktuSerkarang = cal.timeInMillis

        val waktumasuk: Long = pref.prefwaktumasukpresensilong
        val waktukeluar: Long = pref.prefwaktukeluarpresensilong

        if (waktumasuk == 1L && waktukeluar == 1L) {
            binding.waktumasuk.text = "00.00"
            binding.waktukeluar.text = "00.00"
            binding.buttonMasuk.setOnClickListener {
                Toast.makeText(context, "Waktu presensi belum ada", Toast.LENGTH_SHORT).show()
            }
        } else {
//            val wm = pref.prefwaktumasukjpresensi
//            val wk = pref.prefwaktukeluarjpresensi
//            val waktumasuk : Long = pref.prefwaktumasukjpresensi!!.toLong()
//            val waktukeluar : Long = pref.prefwaktukeluarjpresensi!!.toLong()
            binding.waktumasuk.text = getTime(waktumasuk)
            binding.waktukeluar.text = getTime(waktukeluar)
            if (waktuSerkarang >= waktumasuk && waktuSerkarang <= waktukeluar ) {
                binding.buttonMasuk.setOnClickListener {
                    findNavController().navigate(R.id.action_presensiDateFragment_to_tambahPresensiKaryawanFragment)
                }
            } else if (waktuSerkarang <= waktumasuk && waktuSerkarang <= waktukeluar){
                binding.buttonMasuk.setOnClickListener {
                    Toast.makeText(context, "Belum waktunya presensi ", Toast.LENGTH_SHORT).show()
                }
                binding.buttonKeluar.setOnClickListener {
                    Toast.makeText(context, "Belum waktunya presensi", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.buttonMasuk.setOnClickListener {
                    Toast.makeText(context, "Waktu presensi telah habis ", Toast.LENGTH_SHORT).show()
                }
            }
        }

        catatanPresensiAdapter = CatatanPresensiAdapter(arrayListOf())
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = catatanPresensiAdapter
            setHasFixedSize(true)
        }

        if (pref.prefwaktukeluarpresensi == "-") {
            binding.buttonKeluar.setOnClickListener {
                val formatterr = DateTimeFormatter.ofPattern("HH:mm")
                val waktu = LocalDateTime.now().format(formatterr)
                pref.prefwaktukeluarpresensi = waktu
                tambahAbsensi()
            }
        } else {
            binding.buttonKeluar.setOnClickListener {

                Toast.makeText(context, "Anda sudah keluar dari jam kerja", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun tambahAbsensi(){
        val context: Context
        context = requireActivity()
        pref = Preference(context)
        val idPerusahaan = pref.prefidperusahaanuser
        val idUser = pref.prefiduser
        val iddivisi = pref.prefiddivisiuser
        val idpresensi = pref.prefidpresensi
        val keterangan = pref.prefketeranganpresensi
        val waktumasuk = pref.prefwaktumasukpresensi
        val tanggal = pref.preftanggalpresensi
        val formatterr = DateTimeFormatter.ofPattern("HH:mm")
        val waktu = LocalDateTime.now().format(formatterr)
        val presensi = PresensiModel(
            idpresensi!!,idUser!!,iddivisi!!,keterangan!!,waktumasuk!!,waktu,tanggal!!)
        val dbRef = database.getReference("Presensi").child(idPerusahaan!!).child(idpresensi).setValue(presensi)
            dbRef.addOnCompleteListener{
                Toast.makeText(context, "Keluar dari jam kerja", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{ tast ->
            Toast.makeText(context,"Gagal Keluar dari jam kerja${tast.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getcatatan(){
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val idPerusahaan = pref.prefidperusahaanuser
        val idUser = pref.prefiduser
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter).toString()
        val tanggal = date
        val dbRef = database.getReference("Presensi").child(idPerusahaan!!).orderByChild("tanggal").equalTo(tanggal)
        dbRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(PresensiModel::class.java)
                        if (datas!!.idkaryawan == idUser) {
                            data.add(datas!!)
                            pref.prefidpresensi = datas.id
                            pref.prefwaktukeluarpresensi = datas.waktukeluar
                            pref.prefketeranganpresensi = datas.keterangan
                            pref.prefwaktumasukpresensi = datas.waktumasuk
                            pref.preftanggalpresensi = datas.tanggal
                        }
                    }
                    data.reverse()
                    catatanPresensiAdapter.setData(data)

                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getJpresensi() {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val idPerusahaan = pref.prefidperusahaanuser!!
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter).toString()
        val tanggal = date
        val dbRef = database.getReference("Jadwal_Presensi").child(idPerusahaan!!).orderByChild("tanggal").equalTo(tanggal)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(JadwalPresensiModel::class.java)
                        pref.prefidjpresensi = datas!!.id
                        pref.preftanggaljpresensi = datas.tanggal
                        pref.prefwaktumasukpresensilong = datas.waktumasuk.toLong()
                        pref.prefwaktukeluarpresensilong = datas.waktukeluar.toLong()
                        Log.i("tai","Ini1 masuk ${pref.prefwaktumasukpresensilong}")
                    }
                } else {
                    pref.prefwaktumasukpresensilong = 1L
                    pref.prefwaktukeluarpresensilong = 1L
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTime(waktu : Long) : String{
        val formatter = DateTimeFormatter.ofPattern("HH.mm");
        val instant = Instant.ofEpochMilli(waktu)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return (formatter.format(date))
    }
}
