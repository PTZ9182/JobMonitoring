package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.databinding.FragmentEditProfileBinding
import org.d3ifcool.jobmonitoring.model.PerusahaanModel


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    private lateinit var ImageUri: Uri
    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")
    val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.alamatPerusahaan.setOnClickListener {
            selectPicture()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSimpanDataPengaturan.setOnClickListener {
            val namaperusahaan = binding.textPasswordLamaDalamForm.text.toString()
            val alamatperusahaan = binding.textPasswordBaru1DalamForm.text.toString()
            val nohandphone = binding.textNohp2.text.toString()
            val email = binding.textEmail3.text.toString()

            //Validasi Nama Perusahaan
            if (namaperusahaan.isEmpty()) {
                binding.textPasswordLamaDalamForm.error = "Nama Perusahaan Harus Di isi!!"
                binding.textPasswordLamaDalamForm.requestFocus()
                return@setOnClickListener
            }

            //Validasi alamatperusahaan
            if (alamatperusahaan.isEmpty()) {
                binding.textPasswordBaru1DalamForm.error = "Alamat Perusahaan Harus Di isi!!"
                binding.textPasswordBaru1DalamForm.requestFocus()
                return@setOnClickListener
            }


            //Validasi Password
            if (nohandphone.isEmpty()) {
                binding.textNohp2.error = "NoHandphone Harus Di isi!!"
                binding.textNohp2.requestFocus()
                return@setOnClickListener
            }

            //Validasi Panjang Password
            if (nohandphone.length < 10) {
                binding.textNohp2.error = "NoHandphone Minimal 8 Karakter!!"
                binding.textNohp2.requestFocus()
                return@setOnClickListener
            }

            //Validasi Email
            if (email.isEmpty()) {
                binding.textEmail3.error = "Email Harus Di isi!!"
                binding.textEmail3.requestFocus()
                return@setOnClickListener
            } else {
                editperusahaan(namaperusahaan, alamatperusahaan, nohandphone, email)
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
        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            binding.imgProfilPerusahaan.setImageURI(ImageUri)
        }
    }

    private fun editperusahaan(name: String, alamat: String, nohp: String, email: String) {
        val user = Firebase.auth.currentUser
        val id = user!!.uid
        val namee = user.displayName
        val storageRef = storage.getReference("images")

        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(ImageUri.toString())
            storageRef.child("perusahaan").child(id).child("profil").putFile(ImageUri).addOnSuccessListener {
                binding.imgProfilPerusahaan.setImageURI(null)
            }
        }

        dbRef.child(id).setValue(name)
            .addOnCompleteListener {
                Log.i("Update", "Berhasil")
            }.addOnFailureListener { tast ->
                Log.i("Update", "Gagal")
            }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val peusahaan = PerusahaanModel(id, name, email, "suryana123", alamat, nohp)
                    dbRef.child(id).child(namee!!).setValue(peusahaan)
                        .addOnCompleteListener {
                            Log.i("Update", "Berhasil")
                        }.addOnFailureListener { tast ->
                            Log.i("Update", "Gagal")
                        }
                    Toast.makeText(activity, "Data perusahaan telah diubah.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        user.updateEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
    }
}