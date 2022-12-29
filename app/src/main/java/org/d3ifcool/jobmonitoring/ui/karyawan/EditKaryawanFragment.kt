package org.d3ifcool.jobmonitoring.ui.karyawan

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_edit_karyawan.*
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentEditKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditKaryawanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentEditKaryawanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Karyawan")
    private lateinit var pref: Preference
    private var listDivisi = ArrayList<String>()
    private var listidDivisi = ArrayList<String>()
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditKaryawanBinding.inflate(inflater, container, false)
        listDivisi()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        binding.layoutEditKaryawan.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutEditKaryawan.isRefreshing = false
        }

        binding.ekFormTanggallahir.setOnClickListener {
            openTimeDatePicker(ek_form_tanggallahir)
        }

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.ekFormNama.setText(pref.prefnamakaryawan)
        binding.ekFormTanggallahir.setText(pref.preftanggallahirkaryawan)
        //binding.ekFormJeniskelamin.getItemAtPosition(pref.prefjeniskelaminkaryawan!!.toInt())
        binding.ekFormAlamat.setText(pref.prefalamatkaryawan)
        binding.ekFormNohp.setText(pref.prefnohpkaryawan)
        //binding.ekFormPilihdivisi.getItemAtPosition(pref.prefdivisikaryawan!!.toInt())
        binding.ekFormEmail.setText(pref.prefemailkaryawan)


        binding.ekGantipassword.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_editKaryawanFragment_to_gantiPasswordKaryawanFragment)
        }
        binding.buttonSimpanDataForm.setOnClickListener {
            val email = binding.ekFormEmail.text.toString()
            if (binding.ekFormNama.text.isEmpty()) {
                binding.ekFormNama.error = "Nama tidak boleh kosong"
                binding.ekFormNama.requestFocus()
            } else if (binding.ekFormTanggallahir.text.isEmpty()) {
                binding.ekFormTanggallahir.error = "Tanggal lahir tidak boleh kosong"
                binding.ekFormTanggallahir.requestFocus()
            } else if (binding.ekFormJeniskelamin.selectedItem.equals("Jenis Kelamin")) {
                Toast.makeText(context, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                binding.ekFormJeniskelamin.requestFocus()
            } else if (binding.ekFormAlamat.text.isEmpty()) {
                binding.ekFormAlamat.error = "Alamat tidak boleh kosong"
                binding.ekFormAlamat.requestFocus()
            } else if (binding.ekFormNohp.text.isEmpty()) {
                binding.ekFormNohp.error = "No.Handphone tidak boleh kosong"
                binding.ekFormNohp.requestFocus()
            } else if (binding.ekFormPilihdivisi.selectedItem.equals("Pilih Divisi")) {
                Toast.makeText(context, "Pilih divisi", Toast.LENGTH_SHORT).show()
                binding.ekFormPilihdivisi.requestFocus()
            } else if (binding.ekFormEmail.text.isEmpty()) {
                binding.ekFormEmail.error = "Email tidak boleh kosong"
                binding.ekFormEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.ekFormEmail.error = "Email Tidak Valid!!!!"
                binding.ekFormEmail.requestFocus()
            } else {
                editKaryawan()
            }
        }
    }

    private fun openTimeDatePicker(ekFormTanggallahir: EditText) {
        val tanggalAbsen = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                tanggalAbsen.set(Calendar.YEAR, year)
                tanggalAbsen.set(Calendar.MONTH, monthOfYear)
                tanggalAbsen.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val strFormatDefault = "dd MMMM yyyy"
                val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
                ekFormTanggallahir.setText(simpleDateFormat.format(tanggalAbsen.time))
            }
        context?.let { it1 ->
            DatePickerDialog(
                it1,
                date,
                tanggalAbsen.get(Calendar.YEAR),
                tanggalAbsen.get(Calendar.MONTH),
                tanggalAbsen.get(
                    Calendar.DAY_OF_MONTH
                )
            ).show()
        }
    }

    private fun editKaryawan() {
        nDialog.show()
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val karyawan = KaryawanModel(
            pref.prefidkaryawan!!,
            binding.ekFormNama.text.toString(),
            binding.ekFormTanggallahir.text.toString(),
            binding.ekFormJeniskelamin.selectedItem.toString(),
            binding.ekFormAlamat.text.toString(),
            binding.ekFormNohp.text.toString(),
            pref.prefiddivisikaryawan!!,
            binding.ekFormEmail.text.toString(), pref.prefpasswordkaryawan!!
        )
        dbRef.child(idPerusahaan!!).child(pref.prefidkaryawan!!).setValue(karyawan)
            .addOnCompleteListener {
                nDialog.cancel()
                Toast.makeText(activity, "Data Karyawan Berhasil Diubah", Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }.addOnFailureListener { tast ->
                nDialog.cancel()
                Toast.makeText(
                    activity,
                    "Gagal Mengubah Data Karyawan${tast.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun listDivisi() {
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef =
            database.getReference("Divisi").child(idPerusahaan!!).orderByChild("divisi")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listDivisi.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        listDivisi.add(datas!!.divisi)
                        listidDivisi.add(datas!!.id)

                    }
                    binding.ekFormPilihdivisi.onItemSelectedListener = this@EditKaryawanFragment
                    val adapter = context?.let {
                        ArrayAdapter(
                            it,
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                            listDivisi
                        )
                    }
                    binding.ekFormPilihdivisi.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)
        p0?.getItemAtPosition(p2)
        pref.prefiddivisikaryawan = listidDivisi[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
