package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
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
import org.d3ifcool.jobmonitoring.model.Preference


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val storage = Firebase.storage
    val dbRef = database.getReference("Perusahaan")
    val storageRef = storage.getReference("images")
    private lateinit var pref: Preference
    private lateinit var ImageUri: Uri
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
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

        val user = Firebase.auth.currentUser
        user?.let {
            nDialog.show()
            for (profile in it.providerData) {
                binding.isiformNamaperusahaan.setText(profile.displayName)
                binding.isiformEmail.setText(profile.email)
                binding.isiformAlamatperusahaan.setText(pref.prefalamatperusahaan)
                binding.isiformNohp.setText(pref.prefnohpperusahaan)
            }
            val id = user.uid
            val storageReff = storage.getReference("images").child("perusahaan").child(id).child("profil")
            storageReff.getBytes(10 * 1024 * 1024).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                binding.imgPerusahaan.setImageBitmap(bitmap)
                nDialog.cancel()
            }.addOnFailureListener {
                nDialog.cancel()
            }
        }

        binding.editProfilePerusahaan.setOnRefreshListener {
            binding.editProfilePerusahaan.isRefreshing = false
        }

        binding.editImg.setOnClickListener {
            selectPicture()
        }

        binding.buttonSimpan.setOnClickListener {
            val namaperusahaan = binding.isiformNamaperusahaan.text.toString()
            val alamatperusahaan = binding.isiformAlamatperusahaan.text.toString()
            val nohandphone = binding.isiformNohp.text.toString()
            val email = binding.isiformEmail.text.toString()

            //Validasi Nama Perusahaan
            if (namaperusahaan.isEmpty()) {
                binding.isiformNamaperusahaan.error = "Nama Perusahaan Harus Di isi!!"
                binding.isiformNamaperusahaan.requestFocus()
                return@setOnClickListener
            }

            //Validasi alamatperusahaan
            if (alamatperusahaan.isEmpty()) {
                binding.isiformAlamatperusahaan.error = "Alamat Perusahaan Harus Di isi!!"
                binding.isiformAlamatperusahaan.requestFocus()
                return@setOnClickListener
            }


            //Validasi Password
            if (nohandphone.isEmpty()) {
                binding.isiformNohp.error = "NoHandphone Harus Di isi!!"
                binding.isiformNohp.requestFocus()
                return@setOnClickListener
            }

            //Validasi Panjang Password
            if (nohandphone.length < 10) {
                binding.isiformNohp.error = "NoHandphone Minimal 8 angka!!"
                binding.isiformNohp.requestFocus()
                return@setOnClickListener
            }

            //Validasi Email
            if (email.isEmpty()) {
                binding.isiformEmail.error = "Email Harus Di isi!!"
                binding.isiformEmail.requestFocus()
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            binding.imgPerusahaan.setImageURI(ImageUri)

            val user = Firebase.auth.currentUser
            val profileUpdates = userProfileChangeRequest {
                photoUri = Uri.parse(ImageUri.toString())
            }
            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("Update", "Berhasil")

                    } else {
                        Log.i("Update", "Berhasil")
                    }
                }
        }
    }

    private fun editperusahaan(name: String, alamat: String, nohp: String, email: String) {
        nDialog.show()

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        val user = Firebase.auth.currentUser
        val id = user!!.uid
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            pref.prefalamatperusahaan = alamat
            pref.prefnohpperusahaan = nohp
        }
        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.child("perusahaan").child(id).child("profil")
                        .putFile(user.photoUrl!!)
                        .addOnSuccessListener {
                            Log.i("photo", "Berhasil")
                        }
                    val perusahaan = PerusahaanModel(
                        id,
                        name,
                        email,
                        pref.prefpasswordperusahaan!!,
                        alamat,
                        nohp
                    )
                    dbRef.child(id).setValue(perusahaan)
                        .addOnCompleteListener {
                            Log.i("Update", "Berhasil")
                        }.addOnFailureListener { tast ->
                            Log.i("Update", "Gagal")
                        }
                    nDialog.cancel()
                    Toast.makeText(activity, "Data perusahaan telah diubah.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    nDialog.cancel()
                    Toast.makeText(activity, "Gagal merubah data.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        user.updateEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                } else {
                    Log.d(TAG, "User email gagal diganti.")
                }
            }
    }
}