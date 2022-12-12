package org.d3ifcool.jobmonitoring.ui.menuKaryawan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_karyawan_pekerjaan_detail.*
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentKaryawanPekerjaanDetailBinding
import org.d3ifcool.jobmonitoring.model.PekerjaanModel
import org.d3ifcool.jobmonitoring.model.Preference

class KaryawanPekerjaanDetail : Fragment() {

    private var _binding: FragmentKaryawanPekerjaanDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: Preference
    val database = Firebase.database
    val dbRef = database.getReference("Pekerjaan")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentKaryawanPekerjaanDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.kdpJudul.text = pref.prefnamapekerjaanuser
        binding.kdpIsidesc.text = pref.prefdeskripsipekerjaanuser

        binding.layoutKaryawanPekerjaanDetail.setOnRefreshListener {
            binding.layoutKaryawanPekerjaanDetail.isRefreshing = false
        }

        var endtPoint = 0

        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                binding.kdptextProgress.text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                var starttPoint = 50
                var strat = 50
                if (p0 != null) {
                    seekBar.progress = strat
                    starttPoint = seekBar.progress
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    endtPoint = seekBar.progress
                }
            }

        })

        val statuss = pref.prefstatuspekerjaanuser
        binding.kdpButton.setOnClickListener {
            if (statuss == "0") {
                val idPerusahaan = pref.prefidperusahaanuser
                val idPekerjaan = pref.prefidpekerjaanuser
                val divisi = pref.prefdivisipekerjaanuser
                val namaPekerjaan = pref.prefnamapekerjaanuser
                val desc = pref.prefdeskripsipekerjaanuser
                val namaUser = pref.prefnamauser
                val status = "1"
                val pekerjaan = PekerjaanModel(
                    idPerusahaan!!,
                    divisi!!,
                    namaPekerjaan!!,
                    desc!!,
                    namaUser!!,
                    status
                )
                dbRef.child(idPerusahaan!!).child(idPekerjaan!!).setValue(pekerjaan)
                    .addOnCompleteListener {
                        findNavController().popBackStack()
                        Toast.makeText(activity, "Pekerjaan Selesai", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { tast ->
                        Toast.makeText(
                            activity,
                            "Gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    activity,
                    "Anda sudah menyelesaikan pekerjaan ini",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}

