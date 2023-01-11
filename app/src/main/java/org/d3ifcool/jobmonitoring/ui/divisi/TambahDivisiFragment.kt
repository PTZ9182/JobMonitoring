@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.divisi

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahDivisiBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel

class TambahDivisiFragment : Fragment() {

    private var _binding: FragmentTambahDivisiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Divisi")
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTambahDivisiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)

        binding.layoutTambahDivisi.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutTambahDivisi.isRefreshing = false
        }

        binding.tdButtonTambah.setOnClickListener {
            if (binding.tdFormNamaDivisi.text.isEmpty()) {
                binding.tdFormNamaDivisi.error = "Nama divisi tidak boleh kosong"
                binding.tdFormNamaDivisi.requestFocus()
            } else {
                tambahDivisi()
            }
        }
    }

    private fun tambahDivisi() {
        nDialog.show()
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val idDivisi = dbRef.push().key!!
        val divisi = DivisiModel(idDivisi, binding.tdFormNamaDivisi.text.toString())
        dbRef.child(idPerusahaan!!).child(idDivisi).setValue(divisi).addOnCompleteListener {
            nDialog.cancel()
            Toast.makeText(activity, "Divisi Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener { tast ->
            nDialog.cancel()
            Toast.makeText(activity, "Gagal menambahkan Divisi${tast.message}", Toast.LENGTH_SHORT).show()
        }

    }
}