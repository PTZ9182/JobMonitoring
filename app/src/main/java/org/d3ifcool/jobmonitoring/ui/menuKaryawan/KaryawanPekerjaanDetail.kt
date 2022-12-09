package org.d3ifcool.jobmonitoring.ui.menuKaryawan

import android.content.Context
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
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanPekerjaanDetailBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class KaryawanPekerjaanDetail : Fragment() {

    private var _binding: FragmentKaryawanPekerjaanDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference
    val database = Firebase.database
    val dbRef = database.getReference("Perusahaan")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanPekerjaanDetailBinding.inflate(inflater, container, false)
        set()
        return binding.root

    }

    private fun set() {
        setFragmentResultListener("nama_pekerjaan") { requestKey, bundle ->
            val result = bundle.getString("nama_pekerjaan")
            binding.kdpJudul.setText(result)
        }
        setFragmentResultListener("deskripsi") { requestKey, bundle ->
            val result = bundle.getString("deskripsi")
            binding.kdpIsidesc.setText(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutKaryawanPekerjaanDetail.setOnRefreshListener {
            binding.layoutKaryawanPekerjaanDetail.isRefreshing = false
        }

        setFragmentResultListener("status") { requestKey, bundle ->
            val result = bundle.getString("status")

            binding.kdpButton.setOnClickListener {
                if (result == "0") {
                    val contextt: Context
                    contextt = requireActivity()
                    pref = Preference(contextt)
                    val per = pref.prefperusahaan
                    val key = pref.prefkpid
                    val div = pref.prefkpdiv
                    val namepeke = pref.prefkpnp
                    val desc = pref.prefkpdesc
                    val namekary = pref.prefkpdnk
                    val status = "1"
                    val pekerjaan = PekerjaanModel(key!!, div!!, namepeke!!, desc!!, namekary!!, status)
                    dbRef.child(per!!).child("Pekerjaan").child(key).setValue(pekerjaan)
                        .addOnCompleteListener {
                            Toast.makeText(activity, "Pekerjaan Selesai", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { tast ->
                            Toast.makeText(
                                activity,
                                "Gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}