@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.loginRegister

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentRegisterBinding
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.d3ifcool.jobmonitoring.model.Preference


@Suppress("NAME_SHADOWING")
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Membuat akun")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)

        binding.register.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.register.isRefreshing = false
        }

        binding.rgLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_praLoginFragment)
        }

        binding.rgButtonDaftar.setOnClickListener {
            val email = binding.rgIsiformEmail.text.toString()
            if (binding.rgIsiformNamaPerusahaan.text.isEmpty()) {
                binding.rgIsiformNamaPerusahaan.error = "Nama Perusahaan tidak boleh kosong"
                binding.rgIsiformNamaPerusahaan.requestFocus()
            } else if (binding.rgIsiformEmail.text.isEmpty()) {
                binding.rgIsiformEmail.error = "Email tidak boleh kosong"
                binding.rgIsiformEmail.requestFocus()
            } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.rgIsiformEmail.error = "Email Tidak Valid!!!!"
                binding.rgIsiformEmail.requestFocus()
            } else if (binding.rgIsiformPassword.text.isEmpty()) {
                binding.rgIsiformPassword.error = "Password tidak boleh kosong"
                binding.rgIsiformPassword.requestFocus()
            } else if (binding.rgIsiformPassword.text.toString().length < 8) {
                binding.rgIsiformPassword.error = "Password minimal 8 character"
                binding.rgIsiformPassword.requestFocus()
            } else {
                register(
                    binding.rgIsiformNamaPerusahaan.text.toString(),
                    binding.rgIsiformEmail.text.toString(),
                    binding.rgIsiformPassword.text.toString()
                )
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        nDialog.show()
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val user = Firebase.auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val id = user.uid
                            val name = user.displayName
                            val perusahaan = PerusahaanModel(
                                id,
                                binding.rgIsiformNamaPerusahaan.text.toString(),
                                binding.rgIsiformEmail.text.toString(),
                                binding.rgIsiformPassword.text.toString()
                            )
                            if (name != null) {
                                dbRef.child(id).setValue(perusahaan)
                                    .addOnCompleteListener {
                                        Log.i("Register", "Berhasil")
                                    }.addOnFailureListener { tast ->
                                        Log.i("Register", "Gagal")
                                    }
                            }
                            nDialog.cancel()
                            Toast.makeText(
                                activity,
                                "Perusahaan telah didaftarkan.",
                                Toast.LENGTH_SHORT
                            ).show()
                            pref.prefClear()
                            Firebase.auth.signOut()
                            findNavController().navigate(R.id.action_registerFragment_to_praLoginFragment)
                        } else {
                            nDialog.cancel()
                            Toast.makeText(activity, "Gagal mendaftar", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                nDialog.cancel()
                Toast.makeText(activity, "Email telah terdaftar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}