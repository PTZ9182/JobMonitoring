@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.databinding.FragmentGantiPasswordAkunKaryawanBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference

class GantiPasswordAkunKaryawanFragment : Fragment() {

    private var _binding: FragmentGantiPasswordAkunKaryawanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Karyawan")
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGantiPasswordAkunKaryawanBinding.inflate(inflater, container, false)
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
            if (binding.gpIsiformPasswordLama.text.isEmpty()) {
                binding.gpIsiformPasswordLama.error = "Masukan password"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.isEmpty()) {
                binding.gpIsiformPasswordBaru1.error = "Masukan password"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.length <= 8) {
                binding.gpIsiformPasswordBaru1.error = "Password Minimal 8 karakter"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (binding.gpIsiformPasswordBaru2.text.isEmpty()) {
                binding.gpIsiformPasswordBaru2.error = "Masukan password"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.toString() == binding.gpIsiformPasswordLama.text.toString()) {
                binding.gpIsiformPasswordBaru1.error = "Password harus berbeda"
                binding.gpIsiformPasswordBaru1.requestFocus()
            } else if (binding.gpIsiformPasswordLama.text.toString() != pref.prefpassworduser) {
                binding.gpIsiformPasswordLama.error = "Password Salah!"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.toString() != binding.gpIsiformPasswordBaru2.text.toString()) {
                binding.gpIsiformPasswordBaru2.error = "Password harus sesuai"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else {
                nDialog.show()
                val newPassword = binding.gpIsiformPasswordBaru1.text.toString()
                val idPerusahaan = pref.prefidperusahaanuser
                pref.prefpassworduser = newPassword
                val id = pref.prefiduser
                val nama = pref.prefnamauser
                val tanggallahir = pref.preftanggallahiruser
                val jeniskelamin = pref.prefjeniskelaminuser
                val alamat = pref.prefalamatuser
                val nohp = pref.prefnohpuser
                val divisi = pref.prefiddivisiuser
                val email = pref.prefemailuser

                val user = KaryawanModel(id!!, nama!!, tanggallahir!!, jeniskelamin!!, alamat!!, nohp!!,divisi!!,email!!,pref.prefpassworduser!!
                )
                dbRef.child(idPerusahaan!!).child(id).setValue(user)
                    .addOnCompleteListener {
                        nDialog.cancel()
                        findNavController().popBackStack()
                        Toast.makeText(context, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        nDialog.cancel()
                        Toast.makeText(context, "Gagal merubah password", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
