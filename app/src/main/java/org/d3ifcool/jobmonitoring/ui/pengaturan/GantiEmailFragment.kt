package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.databinding.FragmentGantiEmailBinding
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class GantiEmailFragment : Fragment() {

    private var _binding: FragmentGantiEmailBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGantiEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)



        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.buttonSimpanDataForm.setOnClickListener {
            val user = Firebase.auth.currentUser
            if (binding.gpIsiformPasswordLama.text.isEmpty()) {
                binding.gpIsiformPasswordLama.error = "Masukan Email"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.isEmpty()) {
                binding.gpIsiformPasswordBaru1.error = "Masukan Email"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (binding.gpIsiformPasswordBaru2.text.isEmpty()) {
                binding.gpIsiformPasswordBaru2.error = "Masukan Email"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.toString() == binding.gpIsiformPasswordLama.text.toString()) {
                binding.gpIsiformPasswordBaru1.error = "Email harus berbeda"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (binding.gpIsiformPasswordLama.text.toString() != user!!.email) {
                binding.gpIsiformPasswordLama.error = "Email Salah!"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.toString() != binding.gpIsiformPasswordBaru2.text.toString()) {
                binding.gpIsiformPasswordBaru2.error = "Email harus sesuai"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.gpIsiformPasswordLama.text.toString())
                    .matches()
            ) {
                binding.gpIsiformPasswordLama.error = "Email Tidak Valid!!!!"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.gpIsiformPasswordBaru1.text.toString())
                    .matches()
            ) {
                binding.gpIsiformPasswordBaru1.error = "Email Tidak Valid!!!!"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.gpIsiformPasswordBaru2.text.toString())
                    .matches()
            ) {
                binding.gpIsiformPasswordBaru2.error = "Email Tidak Valid!!!!"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else {
                nDialog.show()
                val user = Firebase.auth.currentUser
                val newEmail = binding.gpIsiformPasswordBaru1.text.toString()
                val password = pref.prefpasswordperusahaan
                val alamat = pref.prefalamatperusahaan
                val nohp = pref.prefnohpperusahaan

                user!!.updateEmail(newEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("task", "sukses")
                        }
                        val perusahaan = PerusahaanModel(
                            user.uid,
                            user.displayName!!,
                            newEmail,
                            password!!,
                            alamat,
                            nohp
                        )
                        dbRef.child(user.uid).setValue(perusahaan)
                            .addOnCompleteListener {
                                Log.i("pass", "Berhasil")
                            }.addOnFailureListener { tast ->
                                Log.i("pss", "Gagal")
                            }
                        nDialog.cancel()
                        findNavController().popBackStack()
                        Toast.makeText(context, "Email berhasil diubah", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnFailureListener {
                        nDialog.cancel()
                        Toast.makeText(context, "Gagal merubah email", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}