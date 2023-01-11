@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.databinding.FragmentGantiPasswordPerusahaanBinding
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class GantiPasswordPerusahaanFragment : Fragment() {

    private var _binding: FragmentGantiPasswordPerusahaanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGantiPasswordPerusahaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.buttonSimpanDataForm.setOnClickListener {
            if (binding.gpIsiformPasswordLama.text.isEmpty()){
                binding.gpIsiformPasswordLama.error = "Masukan password"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.isEmpty()){
                binding.gpIsiformPasswordBaru1.error = "Masukan password"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.length <= 8 ){
                binding.gpIsiformPasswordBaru1.error = "Password Minimal 8 karakter"
                binding.gpIsiformPasswordBaru1.requestFocus()
            }else if (binding.gpIsiformPasswordBaru2.text.isEmpty()){
                binding.gpIsiformPasswordBaru2.error = "Masukan password"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else if(binding.gpIsiformPasswordBaru1.text.toString() == binding.gpIsiformPasswordLama.text.toString()) {
                binding.gpIsiformPasswordBaru1.error = "Password harus berbeda"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (binding.gpIsiformPasswordLama.text.toString() != pref.prefpasswordperusahaan){
                binding.gpIsiformPasswordLama.error = "Password Salah!"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.toString() != binding.gpIsiformPasswordBaru2.text.toString()){
                binding.gpIsiformPasswordBaru2.error = "Password harus sesuai"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else{
                nDialog.show()
                val user = Firebase.auth.currentUser
                val newPassword = binding.gpIsiformPasswordBaru1.text.toString()
                pref.prefpasswordperusahaan = newPassword
                val alamat = pref.prefalamatperusahaan
                val nohp = pref.prefnohpperusahaan
                user!!.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("task","sukses")
                        }
                        val perusahaan = PerusahaanModel(user.uid, user.displayName!!, user.email!!, newPassword, alamat, nohp)
                        dbRef.child(user.uid).setValue(perusahaan)
                            .addOnCompleteListener {
                                Log.i("pass", "Berhasil")
                            }.addOnFailureListener {
                                Log.i("pss", "Gagal")
                            }
                        nDialog.cancel()
                        findNavController().popBackStack()
                        Toast.makeText(context, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        nDialog.cancel()
                        Toast.makeText(context, "Gagal merubah password", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}