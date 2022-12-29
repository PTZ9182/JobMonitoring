package org.d3ifcool.jobmonitoring.ui.divisi


import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.databinding.FragmentEditDivisiBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.Preference


class EditDivisiFragment : Fragment() {

    private var _binding: FragmentEditDivisiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Divisi")
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditDivisiBinding.inflate(inflater, container, false)
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

        binding.layoutEditDivisi.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutEditDivisi.isRefreshing = false
        }

        binding.edFormNamaDivisi.setText(pref.prefdivisi)

        binding.edButtonSimpan.setOnClickListener {
            if (binding.edFormNamaDivisi.text.isEmpty()) {
                binding.edFormNamaDivisi.error = "Nama divisi tidak boleh kosong"
                binding.edFormNamaDivisi.requestFocus()
            } else {
                editDivisi()
            }
        }
    }

    private fun editDivisi() {
        nDialog.show()
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val idDivisi = pref.prefiddivisi
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val divisi = DivisiModel(idDivisi!!, binding.edFormNamaDivisi.text.toString())
        dbRef.child(idPerusahaan!!).child(idDivisi).setValue(divisi).addOnCompleteListener {
            nDialog.cancel()
            Toast.makeText(activity, "Divisi Berhasil Diubah", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener { tast ->
            nDialog.cancel()
            Toast.makeText(activity, "Gagal Mengubah Divisi${tast.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
