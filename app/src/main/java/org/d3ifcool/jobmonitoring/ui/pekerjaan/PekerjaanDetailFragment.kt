@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.pekerjaan

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_pekerjaan_detail.*
import org.d3ifcool.jobmonitoring.databinding.FragmentPekerjaanDetailBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference
import kotlin.math.max
import kotlin.math.min
import android.view.ScaleGestureDetector


class PekerjaanDetailFragment : Fragment() {

    private var _binding: FragmentPekerjaanDetailBinding? = null
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private val binding get() = _binding!!
    val database = Firebase.database
    private lateinit var pref: Preference
    val storage = Firebase.storage
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        _binding = FragmentPekerjaanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            imageSelfie.scaleX = scaleFactor
            imageSelfie.scaleY = scaleFactor
            return true
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.layoutPekerjaanDetail.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutPekerjaanDetail.isRefreshing = false
        }

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)

        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val iddivisi = pref.prefiddivisipekerjaan
        val dbRef =
            database.getReference("Divisi").child(idPerusahaan!!).orderByChild("id").equalTo(iddivisi)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        binding.ddpIsidivisi.text = datas!!.divisi
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        val idkaryawan = pref.prefidkaryawanpekerjaan
        val dbReff = database.getReference("Karyawan").child(idPerusahaan).orderByChild("id").equalTo(idkaryawan)
        dbReff.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(KaryawanModel::class.java)
                        binding.ddpIsinama.text = datas!!.namaKaryawan
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        binding.ddpIsinamapekerjaan.text = pref.prefnamapekerjaan
        binding.ddpIsidesc.text = pref.prefdeskripsipekerjaan
        binding.ddpTextprogress.text = pref.prefprogresspekerjaan.toString()
        val status = pref.prefstatuspekerjaan
        if (status == "1"){
            binding.ddpIsistatus.text = "Selesai"
        } else {
            binding.ddpIsistatusbelumseselai.text = "Belum Selesai"
        }

        val idPekerjaan = pref.prefidpekerjaan
        val idKaryawan = pref.prefidkaryawanpekerjaan
        nDialog.show()
        val storageRefff =
            storage.getReference("images").child("Pekerjaan").child(idPerusahaan).child(idKaryawan!!).child(idPekerjaan!!).child("bukti")
        storageRefff.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imageSelfie.setImageBitmap(bitmap)
            nDialog.cancel()
        }.addOnFailureListener {
            nDialog.cancel()
        }
    }
}