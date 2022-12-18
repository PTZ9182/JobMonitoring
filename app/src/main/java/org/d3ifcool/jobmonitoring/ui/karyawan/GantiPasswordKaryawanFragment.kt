package org.d3ifcool.jobmonitoring.ui.karyawan

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
import org.d3ifcool.jobmonitoring.databinding.FragmentGantiPasswordBinding
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class GantiPasswordKaryawanFragment : Fragment() {

    private var _binding: FragmentGantiPasswordBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Karyawan")
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGantiPasswordBinding.inflate(inflater, container, false)
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
            } else if (binding.gpIsiformPasswordLama.text.toString() != pref.prefpasswordkaryawan) {
                binding.gpIsiformPasswordLama.error = "Password Salah!"
                binding.gpIsiformPasswordLama.requestFocus()
            } else if (binding.gpIsiformPasswordBaru1.text.toString() != binding.gpIsiformPasswordBaru2.text.toString()) {
                binding.gpIsiformPasswordBaru2.error = "Password harus sesuai"
                binding.gpIsiformPasswordBaru2.requestFocus()
            } else {
                nDialog.show()
                val contextt: Context
                contextt = requireActivity()
                pref = Preference(contextt)
                val user = Firebase.auth.currentUser
                val idPerusahaan = user?.uid
                val newpassword = binding.gpIsiformPasswordBaru1.text.toString()
                val karyawan = KaryawanModel(
                    pref.prefidkaryawan!!,
                    pref.prefnamakaryawan!!,
                    pref.preftanggallahirkaryawan!!,
                    pref.prefjeniskelaminkaryawan!!,
                    pref.prefalamatkaryawan!!,
                    pref.prefnohpkaryawan!!,
                    pref.prefiddivisikaryawan!!,
                    pref.prefemailkaryawan!!, newpassword
                )
                dbRef.child(idPerusahaan!!).child(pref.prefidkaryawan!!).setValue(karyawan)
                    .addOnCompleteListener {
                        pref.prefpasswordkaryawan = newpassword
                        nDialog.cancel()
                        Toast.makeText(activity, "Password karyawan Berhasil Diubah", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }.addOnFailureListener { tast ->
                        nDialog.cancel()
                        Toast.makeText(activity, "Gagal Mengubah Password Karyawan${tast.message}",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}