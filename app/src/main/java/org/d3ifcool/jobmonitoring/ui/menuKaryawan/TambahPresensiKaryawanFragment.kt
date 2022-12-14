package org.d3ifcool.jobmonitoring.ui.menuKaryawan

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_tambah_presensi_karyawan.*
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahPresensiKaryawanBinding
import org.d3ifcool.jobmonitoring.model.Preference
import org.d3ifcool.jobmonitoring.model.PresensiModel
import java.text.SimpleDateFormat
import java.util.*

class TambahPresensiKaryawanFragment : Fragment() {

    private var _binding: FragmentTambahPresensiKaryawanBinding? = null
    private val binding get() = _binding!!
    val database = Firebase.database
    val storage = Firebase.storage
    val dbRef = database.getReference("Presensi")
    val storageRef = storage.getReference("images")
    private lateinit var pref: Preference
    private lateinit var ImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTambahPresensiKaryawanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputTanggal.setOnClickListener {
            openTimeDatePicker(inputTanggal)
        }

        binding.imageSelfie.setOnClickListener {
            selectPicture()
        }

        binding.btnAbsen.setOnClickListener {
            val namaKaryawan = binding.inputNama.text.toString()
            val tanggaldanwaktu = binding.inputTanggal.text.toString()
            val keterangan = binding.inputKeterangan.text.toString()

            //Validasi Nama Karyawan
            if (namaKaryawan.isEmpty()) {
                binding.inputNama.error = "Nama Karyawan Harus Di isi!!"
                binding.inputNama.requestFocus()
                return@setOnClickListener
            }
            //Validasi Tanggal dan Waktu
            else if (tanggaldanwaktu.isEmpty()) {
                binding.inputTanggal.error = "Tanggal harus Di isi!"
                binding.inputTanggal.requestFocus()
                return@setOnClickListener
            }
            //Validasi Keterangan
            else if (keterangan.isEmpty()) {
                binding.inputKeterangan.error = "Keterangan Harus Ada!"
                binding.inputKeterangan.requestFocus()
                return@setOnClickListener
            } else {
                tambahAbsensi(namaKaryawan,tanggaldanwaktu,keterangan,)
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
        }
    }

    private fun openTimeDatePicker(inputTanggal: EditText) {
        val tanggalAbsen = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                tanggalAbsen.set(Calendar.YEAR, year)
                tanggalAbsen.set(Calendar.MONTH, monthOfYear)
                tanggalAbsen.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val strFormatDefault = "dd MMMM yyyy HH:mm"
                val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
                inputTanggal.setText(simpleDateFormat.format(tanggalAbsen.time))
            }
        inputTanggal.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    date,
                    tanggalAbsen.get(Calendar.YEAR),
                    tanggalAbsen.get(Calendar.MONTH),
                    tanggalAbsen.get(
                        Calendar.DAY_OF_MONTH
                    )
                ).show()
            }
        }
    }

    private fun tambahAbsensi(name: String, tanggal: String, keterangan: String){
        val context: Context
        context = requireActivity()
        pref = Preference(context)
        val idPresensi = dbRef.push().key!!
        val idPerusahaan = pref.prefidperusahaanuser
        val idUser = pref.prefiduser
        val presensi = PresensiModel(
            idPresensi,name,tanggal,keterangan)
        dbRef.child(idPerusahaan!!).child(idUser!!).child(idPresensi).setValue(presensi).addOnCompleteListener{
            Toast.makeText(activity,"Presensi Ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener{ tast ->
            Toast.makeText(activity,"Gagal menambahkan Presensi${tast.message}", Toast.LENGTH_SHORT).show()
        }

    }
}