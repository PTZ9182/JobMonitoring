package org.d3ifcool.jobmonitoring.ui.presensi

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_tambah_presensi.*
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahPresensiBinding
import java.text.SimpleDateFormat
import java.util.*

class TambahPresensiFragment : Fragment() {

    private var _binding: FragmentTambahPresensiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTambahPresensiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTambahPresensiBinding.bind(view)

        binding.ppIsiformWaktu.setOnClickListener {
            openTimePicker(pp_isiform_waktu)
        }

        binding.apply {
            ppIsiformTanggalpresensi.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY"){
                        val date = bundle.getString("SELECTED_DATE")
                        //tanggal.text = date
                        ppIsiformTanggalpresensi.text = date
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }

    }

    fun openTimePicker(ppIsiformWaktu : TextView){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            ppIsiformWaktu.text = SimpleDateFormat("HH:mm").format(cal.time)
        }

        ppIsiformWaktu.setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE), true).show()
        }
    }
}