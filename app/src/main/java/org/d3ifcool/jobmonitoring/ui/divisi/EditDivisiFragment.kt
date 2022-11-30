package org.d3ifcool.jobmonitoring.ui.divisi


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentEditDivisiBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel


class EditDivisiFragment : Fragment() {

    private var _binding: FragmentEditDivisiBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditDivisiBinding.inflate(inflater, container, false)
        set()
        return binding.root
    }

    private fun set() {
        setFragmentResultListener("divisi") { requestKey, bundle ->
            val result = bundle.getString("divisi")
            binding.edFormNamaDivisi.setText(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutEditDivisi.setOnRefreshListener {
            binding.layoutEditDivisi.isRefreshing = false
        }
        binding.edButtonSimpan.setOnClickListener {
            if (binding.edFormNamaDivisi.text.isEmpty()){
                binding.edFormNamaDivisi.setError("Nama divisi tidak boleh kosong")
                binding.edFormNamaDivisi.requestFocus()
            } else {
                editDivisi()
            }
        }
    }

    private fun editDivisi() {
        setFragmentResultListener("id") { requestKey, bundle ->
            val result = bundle.getString("id")
            val user = Firebase.auth.currentUser
            val name = user?.displayName
            val divisi = DivisiModel(result!!,binding.edFormNamaDivisi.text.toString())
            dbRef.child(name!!).child("Divisi").child(result).setValue(divisi).addOnCompleteListener{
                Toast.makeText(activity,"Divisi Berhasil Diubah", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_editDivisiFragment_to_divisiFragment)
            }.addOnFailureListener{ tast ->
                Toast.makeText(activity,"Gagal Mengubah Divisi${tast.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
