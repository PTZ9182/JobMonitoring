package org.d3ifcool.jobmonitoring.ui.menuKaryawan

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home_karyawan.*
import kotlinx.android.synthetic.main.fragment_karyawan_pekerjaan_detail.*
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanPekerjaanDetailBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class KaryawanPekerjaanDetail : Fragment() {

    private var _binding: FragmentKaryawanPekerjaanDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference
    val database = Firebase.database
    val dbRef = database.getReference("Pekerjaan")
    val storage = Firebase.storage
    val storageRef = storage.getReference("images")
    private lateinit var ImageUri: Uri
    lateinit var nDialog: ProgressDialog
    var starttPoint = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanPekerjaanDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        binding.kdpJudul.text = pref.prefnamapekerjaanuser
        binding.kdpIsidesc.text = pref.prefdeskripsipekerjaanuser

        binding.layoutKaryawanPekerjaanDetail.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutKaryawanPekerjaanDetail.isRefreshing = false
        }

        starttPoint = pref.prefprogresspekerjaan!!.toInt()
        progressbar()
        binding.kdpTambah.setOnClickListener {
            if (starttPoint <= 90) {
                starttPoint += 10
                progressbar()
                if (starttPoint == 100) {
                    binding.kdpButton.visibility = View.VISIBLE
                    binding.kdpButtonn.visibility = View.GONE
                } else {
                    binding.kdpButton.visibility = View.GONE
                    binding.kdpButtonn.visibility = View.VISIBLE
                }
            }
        }

        binding.kdpKurang.setOnClickListener {
            if (starttPoint >= 10) {
                starttPoint -= 10
                progressbar()
                if (starttPoint == 100) {
                    binding.kdpButton.visibility = View.VISIBLE
                    binding.kdpButtonn.visibility = View.GONE
                } else {
                    binding.kdpButton.visibility = View.GONE
                    binding.kdpButtonn.visibility = View.VISIBLE
                }
            }
        }
        binding.kdptextProgress.text = seekBar.progress.toString()

        binding.constraintLayout6.setOnClickListener {
            selectPicture()
        }

        val statuss = pref.prefstatuspekerjaanuser
        if (statuss == "1") {
            binding.kdpButton.visibility = View.GONE
            binding.kdpTambah.visibility = View.GONE
            binding.kdpKurang.visibility = View.GONE
            binding.kdpButtonn.visibility = View.GONE
        } else {
            binding.kdpTambah.visibility = View.VISIBLE
            binding.kdpKurang.visibility = View.VISIBLE
        }

        binding.kdpButtonn.setOnClickListener {
            buktifoto()
            val idPerusahaan = pref.prefidperusahaanuser
            val iduser = pref.prefiduser
            val idPekerjaan = pref.prefidpekerjaanuser
            val iddivisi = pref.prefiddivisipekerjaanuser
            val namaPekerjaan = pref.prefnamapekerjaanuser
            val desc = pref.prefdeskripsipekerjaanuser
            val status = "0"
            val pekerjaan = PekerjaanModel(
                idPekerjaan!!,iduser!!,
                iddivisi!!,
                namaPekerjaan!!,
                desc!!,
                starttPoint,
                status
            )
            dbRef.child(idPerusahaan!!).child(idPekerjaan!!).setValue(pekerjaan)
                .addOnCompleteListener {
                    findNavController().popBackStack()
                    Toast.makeText(activity, "Update Progress selesai", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { tast ->
                    Toast.makeText(
                        activity,
                        "Gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        binding.kdpButton.setOnClickListener {
            buktifoto()
            if (statuss == "0") {
                val idPerusahaan = pref.prefidperusahaanuser
                val iduser = pref.prefiduser
                val idPekerjaan = pref.prefidpekerjaanuser
                val iddivisi = pref.prefiddivisipekerjaanuser
                val namaPekerjaan = pref.prefnamapekerjaanuser
                val desc = pref.prefdeskripsipekerjaanuser
                val status = "1"
                val pekerjaan = PekerjaanModel(
                    idPekerjaan!!,iduser!!,
                    iddivisi!!,
                    namaPekerjaan!!,
                    desc!!, starttPoint,
                    status
                )
                dbRef.child(idPerusahaan!!).child(idPekerjaan!!).setValue(pekerjaan)
                    .addOnCompleteListener {
                        findNavController().popBackStack()
                        Toast.makeText(activity, "Pekerjaan Selesai", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { tast ->
                        Toast.makeText(
                            activity,
                            "Gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    activity,
                    "Anda sudah menyelesaikan pekerjaan ini",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    fun progressbar() {
        seekBar.progress = starttPoint
        binding.kdptextProgress.text = seekBar.progress.toString()
    }
    private fun selectPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            ImageUri = data?.data!!
            binding.imageSelfie.setImageURI(ImageUri)
            val contextt: Context
            contextt = requireActivity()
            pref = Preference(contextt)
            pref.prefimguserpekerjaan = ImageUri.toString()
        }
    }

    private fun buktifoto() {
        nDialog.show()
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val idUser = pref.prefiduser
        val idPekerjaan = pref.prefidpekerjaanuser
        val idPerusahaan = pref.prefidperusahaanuser

        storageRef.child("Pekerjaan").child(idPerusahaan!!).child(idUser!!).child(idPekerjaan!!).child("bukti")
            .putFile(pref.prefimguserpekerjaan!!.toUri())
            .addOnSuccessListener {
                nDialog.cancel()
                Log.i("photo", "Berhasil")
            } .addOnFailureListener {
                nDialog.cancel()
            }
        nDialog.cancel()
    }
}

