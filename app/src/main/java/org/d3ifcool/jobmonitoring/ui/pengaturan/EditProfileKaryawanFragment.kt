@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_edit_profile_karyawan.*
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentEditProfileKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference
import java.text.SimpleDateFormat
import java.util.*


class EditProfileKaryawanFragment : Fragment() {
    private var _binding: FragmentEditProfileKaryawanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val storage = Firebase.storage
    val dbRef = database.getReference("Karyawan")
    val storageRef = storage.getReference("images")
    private lateinit var pref: Preference
    private lateinit var imageUri: Uri
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditProfileKaryawanBinding.inflate(inflater, container, false)
        binding.edit.setOnClickListener {
            selectPicture()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context: Context
        context = requireActivity()
        pref = Preference(context)

        binding.homeeditprofile.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.homeeditprofile.isRefreshing = false
        }

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)

        binding.isiformNama.setText(pref.prefnamauser)
        binding.isiformTanggallahir.setText(pref.preftanggallahiruser)
        binding.isiformAlamat.setText(pref.prefalamatuser)
        binding.isiformNohp.setText(pref.prefnohpuser)
        binding.isiformEmail.setText(pref.prefemailuser)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tk_isiform_jenis_kelamin,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.isiformJeniskelamin.adapter = adapter
        if (pref.prefjeniskelaminuser != null) {
            val spinnerPosition = adapter.getPosition(pref.prefjeniskelaminuser)
            binding.isiformJeniskelamin.setSelection(spinnerPosition)
        }

        nDialog.show()
        val id = pref.prefiduser
        val storageReff = storage.getReference("images").child("Karyawan").child(id!!).child("profil")
        storageReff.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imgKaryawan.setImageBitmap(bitmap)
            nDialog.cancel()
        }.addOnFailureListener {
            nDialog.cancel()
        }

        binding.isiformTanggallahir.setOnClickListener {
            openTimeDatePicker(isiform_tanggallahir)
        }

        binding.edit.setOnClickListener {
            selectPicture()
        }

        binding.buttonSimpanDataPengaturan.setOnClickListener {
            val email = binding.isiformEmail.text.toString()

            if (binding.isiformNama.text.isEmpty()){
                binding.isiformNama.error = "Nama Karyawan Harus Di isi!!"
                binding.isiformNama.requestFocus()
                return@setOnClickListener
            }

            if (binding.isiformTanggallahir.text.isEmpty()){
                binding.isiformTanggallahir.error = "Tanggal Karyawan Harus Di isi!!"
                binding.isiformTanggallahir.requestFocus()
                return@setOnClickListener
            }

            if (binding.isiformJeniskelamin.selectedItem.equals("Jenis Kelamin")){
                binding.jeniskelamin.error = "Jenis kelamin Harus Di isi!!"
                binding.jeniskelamin.requestFocus()
                return@setOnClickListener
            }

            if (binding.isiformAlamat.text.isEmpty()){
                binding.isiformAlamat.error = "Alamat Minimal 8 Karakter!!"
                binding.isiformAlamat.requestFocus()
                return@setOnClickListener
            }

            if (binding.isiformNohp.text.isEmpty()) {
                binding.isiformNohp.error = "No handphone Harus Di isi"
                binding.isiformNohp.requestFocus()
                return@setOnClickListener
            }

            if (binding.isiformEmail.text.isEmpty()) {
                binding.isiformEmail.error = "Email Harus Di isi"
                binding.isiformEmail.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.isiformEmail.error = "Email Tidak Valid!!!!"
                binding.isiformEmail.requestFocus()

            } else {
                edituser(binding.isiformNama.text.toString(),
                binding.isiformTanggallahir.text.toString(),
                binding.isiformJeniskelamin.selectedItem.toString(),
                binding.isiformAlamat.text.toString(),
                binding.isiformNohp.text.toString(),
                binding.isiformEmail.text.toString())
            }
        }
    }

    private fun openTimeDatePicker(isiformTanggallahir: EditText) {
        val tanggalAbsen = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            tanggalAbsen.set(Calendar.YEAR,year)
            tanggalAbsen.set(Calendar.MONTH, monthOfYear)
            tanggalAbsen.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val strFormatDefault = "dd MMMM yyyy"
            val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
            isiformTanggallahir.setText(simpleDateFormat.format(tanggalAbsen.time))
        }
            context?.let { it1 ->
                DatePickerDialog(
                    it1,date, tanggalAbsen.get(Calendar.YEAR), tanggalAbsen.get(Calendar.MONTH), tanggalAbsen.get(
                        Calendar.DAY_OF_MONTH)).show()
            }

    }

    private fun selectPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            binding.imgKaryawan.setImageURI(imageUri)
            val contextt: Context
            contextt = requireActivity()
            pref = Preference(contextt)
            pref.prefimguser = imageUri.toString()
        }
    }

    private fun edituser(nama:String, tanggallahir:String, jeniskelamin:String, alamat:String, nohp:String, email:String) {
        nDialog.show()
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val idUser = pref.prefiduser
        val divisi = pref.prefiddivisiuser
        val pass = pref.prefpassworduser
        val idPerusahaan = pref.prefidperusahaanuser

        storageRef.child("Karyawan").child(idUser!!).child("profil")
            .putFile(pref.prefimguser!!.toUri())
            .addOnSuccessListener {
                Log.i("photo", "Berhasil")
            }
        val user = KaryawanModel(idUser,
            nama, tanggallahir, jeniskelamin,
            alamat,nohp,divisi!!,email,pass!!

        )
        dbRef.child(idPerusahaan!!).child(idUser).setValue(user)
            .addOnCompleteListener {
                pref.prefnamauser = nama
                pref.preftanggallahiruser = tanggallahir
                pref.prefjeniskelaminuser = jeniskelamin
                pref.prefalamatuser = alamat
                pref.prefnohpuser = nohp
                pref.prefemailuser = email
                nDialog.cancel()
                findNavController().popBackStack()
                Toast.makeText(context, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                nDialog.cancel()
                Toast.makeText(context, "Data gagal diubah", Toast.LENGTH_SHORT).show()
            }
    }
}