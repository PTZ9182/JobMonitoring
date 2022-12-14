package org.d3ifcool.jobmonitoring.ui.menuKaryawan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahPresensiKaryawanBinding
import org.d3ifcool.jobmonitoring.model.Preference
import org.d3ifcool.jobmonitoring.model.PresensiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TambahPresensiKaryawanFragment : Fragment() {

    private var _binding: FragmentTambahPresensiKaryawanBinding? = null
    private val binding get() = _binding!!
    val database = Firebase.database
    val storage = Firebase.storage
    val dbRef = database.getReference("Presensi")
    val storageRef = storage.getReference("images")
    private lateinit var pref: Preference
    private lateinit var ImageUri: Uri
    private lateinit var url :String
    private lateinit var img :String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTambahPresensiKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context: Context
        context = requireActivity()
        pref = Preference(context)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(formatter)
        binding.inputTanggal.text = date
        binding.inputNama.text = pref.prefnamauser
        val setbutton = pref.prefbuttonpresensi
        if (setbutton == date.toString()){
            binding.btnAbsen.visibility = View.GONE
        } else {
            binding.btnAbsen.visibility = View.VISIBLE
        }
        url = "img"
        img = "img"

        binding.imageSelfie.setOnClickListener {
            selectPicture()
        }

        binding.btnAbsen.setOnClickListener {
            val keterangan = binding.inputKeterangan.text.toString()

            //Validasi Keterangan
            if (keterangan.isEmpty()) {
                binding.inputKeterangan.error = "Keterangan Harus Ada!"
                binding.inputKeterangan.requestFocus()
                return@setOnClickListener
            } else {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = LocalDateTime.now().format(formatter)
                val formatterr = DateTimeFormatter.ofPattern("HH:mm")
                val waktu = LocalDateTime.now().format(formatterr)
                tambahAbsensi(pref.prefnamauser!!,keterangan,waktu.toString(),date.toString())
                pref.prefbuttonpresensi = date.toString()
            }
        }
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
            url = ImageUri.toString()
        }
    }


    private fun tambahAbsensi(name: String, keterangan: String, waktu: String, tanggal: String){
        val context: Context
        context = requireActivity()
        pref = Preference(context)
        val idPresensi = dbRef.push().key!!
        val idPerusahaan = pref.prefidperusahaanuser
        val idUser = pref.prefiduser
        val divisi = pref.prefdivisiuser
        val presensi = PresensiModel(
            idPresensi,idUser!!,name,divisi!!,keterangan,waktu,tanggal)
        dbRef.child(idPerusahaan!!).child(idPresensi).setValue(presensi).addOnCompleteListener{
            Toast.makeText(activity,"Presensi Ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener{ tast ->
            Toast.makeText(activity,"Gagal menambahkan Presensi${tast.message}", Toast.LENGTH_SHORT).show()
        }
        if (img != url) {
            storageRef.child("Presensi").child(idPerusahaan).child(idUser).child(tanggal)
                .putFile(ImageUri)
                .addOnSuccessListener {
                    Log.i("photo", "Berhasil")
                }
        }
    }
}