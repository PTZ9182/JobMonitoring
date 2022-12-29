package org.d3ifcool.jobmonitoring.ui.presensi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_tambah_presensi.*
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahPresensiBinding
import org.d3ifcool.jobmonitoring.model.JadwalPresensiModel
import org.d3ifcool.jobmonitoring.model.Preference
import java.text.SimpleDateFormat
import java.util.*


class TambahPresensiFragment : Fragment() {

    private var _binding: FragmentTambahPresensiBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val dbRef = database.getReference("Jadwal_Presensi")
    private lateinit var pref: Preference
    var yearr = 0
    var monthh = 0
    var dayy = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTambahPresensiBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTambahPresensiBinding.bind(view)

        binding.ppIsiformTanggalpresensi.setOnClickListener {
            openTimeDatePicker(pp_isiform_tanggalpresensi)
        }

        binding.ppIsiformWaktuMasuk.setOnClickListener {
            getTime(pp_isiform_waktu_masuk)
        }

        binding.ppIsiformWaktuKeluar.setOnClickListener {
            getTime2(pp_isiform_waktu_keluar)

        }

        binding.ppButtonSimpan.setOnClickListener {
            tambahPresensi(
                binding.ppIsiformTanggalpresensi.text.toString(),
                pref.prefwaktumasukjpresensi.toString(),
                pref.prefwaktukeluarjpresensi.toString()
            )
        }
    }


    private fun openTimeDatePicker(pp_isiform_tanggalpresensi: TextView) {
        val tanggalAbsen = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                tanggalAbsen.set(Calendar.YEAR, year)
                yearr = year
                tanggalAbsen.set(Calendar.MONTH, monthOfYear)
                monthh = monthOfYear
                tanggalAbsen.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dayy = dayOfMonth
                val strFormatDefault = "yyyy-MM-dd"
                val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
                pp_isiform_tanggalpresensi.setText(simpleDateFormat.format(tanggalAbsen.time))
            }
        context?.let { it1 ->
            DatePickerDialog(
                it1,
                date,
                tanggalAbsen.get(Calendar.YEAR),
                tanggalAbsen.get(Calendar.MONTH),
                tanggalAbsen.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
    }

    fun getTime(pp_isiform_waktu_masuk: TextView) {

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.YEAR, yearr)
            cal.set(Calendar.MONTH, monthh)
            cal.set(Calendar.DAY_OF_MONTH, dayy)
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 0)
            val strFormatDefault = "HH.mm"
            val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
            pref.prefwaktumasukjpresensi = cal.getTimeInMillis().toString()
            pp_isiform_waktu_masuk.setText(simpleDateFormat.format(cal.time))
        }
        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun getTime2(pp_isiform_waktu_masuk: TextView) {

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.YEAR, yearr)
            cal.set(Calendar.MONTH, monthh)
            cal.set(Calendar.DAY_OF_MONTH, dayy)
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 0)
            val strFormatDefault = "HH.mm"
            val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
            pref.prefwaktukeluarjpresensi = cal.getTimeInMillis().toString()
            pp_isiform_waktu_masuk.setText(simpleDateFormat.format(cal.time))
        }

            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()

    }


    private fun tambahPresensi(tanggal: String, waktumasuk: String, waktukeluar: String) {
        val user = Firebase.auth.currentUser

        val idjadwalPresensi = dbRef.push().key!!
        val idPerusahaan = user!!.uid
        val presensi = JadwalPresensiModel(
            idjadwalPresensi, tanggal, waktumasuk!!, waktukeluar!!
        )
        dbRef.child(idPerusahaan!!).child(tanggal).setValue(presensi).addOnCompleteListener {
            Toast.makeText(activity, "Presensi Ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener { tast ->
            Toast.makeText(
                activity,
                "Gagal menambahkan Presensi${tast.message}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}