@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring.ui.karyawan


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_tambah_karyawan.*
import org.d3ifcool.jobmonitoring.databinding.FragmentTambahKaryawanBinding
import org.d3ifcool.jobmonitoring.model.DivisiModel
import org.d3ifcool.jobmonitoring.model.KaryawanModel
import org.d3ifcool.jobmonitoring.model.Preference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TambahKaryawanFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentTambahKaryawanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val dbRef = database.getReference("Karyawan")
    private var listDivisi = ArrayList<String>()
    private var listidDivisi = ArrayList<String>()
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTambahKaryawanBinding.inflate(inflater, container, false)
        listDivisi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang memuat")
        nDialog.isIndeterminate = false
        nDialog.setCancelable(true)

        binding.tkFormTanggallahir.setOnClickListener{
            openTimeDatePicker(tk_form_tanggallahir)
        }

        binding.layoutTambahKaryawan.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.layoutTambahKaryawan.isRefreshing = false
        }

        binding.tkButtonSimpan.setOnClickListener {
            val email = binding.tkFormEmail.text.toString()
            val password = binding.tkFormPassword.text.toString()
            val addres = arrayOf(email)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"

            val user = Firebase.auth.currentUser
            intent.putExtra(Intent.EXTRA_EMAIL,addres)
            intent.putExtra(Intent.EXTRA_SUBJECT,"Selamat Akun anda Telah Terdaftar di aplikasi Job monitoring")
            intent.putExtra(Intent.EXTRA_TEXT," Nama Perusahaan :\t ${user!!.displayName} \n Email user :\t $email \n Password :\t $password ")
            startActivity(intent)

            if (binding.tkFormNama.text.isEmpty()) {
                binding.tkFormNama.error = "Nama tidak boleh kosong"
                binding.tkFormNama.requestFocus()
            } else if (binding.tkFormTanggallahir.text.isEmpty()) {
                binding.tkFormTanggallahir.error = "Tanggal lahir tidak boleh kosong"
                binding.tkFormTanggallahir.requestFocus()
            } else if (binding.tkFormJeniskelamin.selectedItem.equals("Jenis Kelamin")) {
                Toast.makeText(context, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                binding.tkFormJeniskelamin.requestFocus()
            } else if (binding.tkFormAlamat.text.isEmpty()) {
                binding.tkFormAlamat.error = "Alamat tidak boleh kosong"
                binding.tkFormAlamat.requestFocus()
            } else if (binding.tkFormNohp.text.isEmpty()) {
                binding.tkFormNohp.error = "No.Handphone tidak boleh kosong"
                binding.tkFormNohp.requestFocus()
            } else if (binding.tkFormPilihdivisi.selectedItem.equals("Pilih Divisi")) {
                Toast.makeText(context, "Divisi belum ada", Toast.LENGTH_SHORT).show()
                binding.tkFormPilihdivisi.requestFocus()
            } else if (binding.tkFormEmail.text.isEmpty()) {
                binding.tkFormEmail.error = "Email tidak boleh kosong"
                binding.tkFormEmail.requestFocus()
            } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tkFormEmail.error = "Email Tidak Valid!!!!"
                binding.tkFormEmail.requestFocus()
            } else if (binding.tkFormPassword.text.isEmpty()) {
                binding.tkFormPassword.error = "Password tidak boleh kosong"
                binding.tkFormPassword.requestFocus()
            } else if (binding.tkFormPassword.text.length < 8) {
                binding.tkFormPassword.error = "Password minimal 8 character"
                binding.tkFormPassword.requestFocus()
            } else {
                tambahKaryawan()
            }
        }
    }

    private fun openTimeDatePicker(tkFormTanggallahir: EditText) {
        val tanggalAbsen = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            tanggalAbsen.set(Calendar.YEAR,year)
            tanggalAbsen.set(Calendar.MONTH, monthOfYear)
            tanggalAbsen.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val strFormatDefault = "dd MMMM yyyy"
            val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
            tkFormTanggallahir.setText(simpleDateFormat.format(tanggalAbsen.time))
        }
            context?.let { it1 ->
                DatePickerDialog(
                    it1,date, tanggalAbsen.get(Calendar.YEAR), tanggalAbsen.get(Calendar.MONTH), tanggalAbsen.get(
                        Calendar.DAY_OF_MONTH)).show()
            }
    }

    private fun tambahKaryawan() {
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        nDialog.show()
        val idKaryawan = dbRef.push().key!!
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val karyawan = KaryawanModel(
            idKaryawan,
            binding.tkFormNama.text.toString(),
            binding.tkFormTanggallahir.text.toString(),
            binding.tkFormJeniskelamin.selectedItem.toString(),
            binding.tkFormAlamat.text.toString(),
            binding.tkFormNohp.text.toString(),
            pref.prefiddivisikaryawan!!,
            binding.tkFormEmail.text.toString(),
            binding.tkFormPassword.text.toString()
        )
        dbRef.child(idPerusahaan!!).child(idKaryawan).setValue(karyawan).addOnCompleteListener {
            nDialog.cancel()
            Toast.makeText(activity, "Karyawan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }.addOnFailureListener { tast ->
            nDialog.cancel()
            Toast.makeText(
                activity,
                "Gagal menambahkan Karyawan${tast.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun listDivisi() {
        val user = Firebase.auth.currentUser
        val idPerusahaan = user?.uid
        val dbRef = database.getReference("Divisi").child(idPerusahaan!!).orderByChild("divisi")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listDivisi.clear()
                if (snapshot.exists()) {
                    for (datasnap in snapshot.children) {
                        val datas = datasnap.getValue(DivisiModel::class.java)
                        listDivisi.add(datas!!.divisi)
                        listidDivisi.add(datas.id)

                    }
                    binding.tkFormPilihdivisi.onItemSelectedListener = this@TambahKaryawanFragment
                    val adapter = context?.let {
                        ArrayAdapter(
                            it,
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                            listDivisi
                        )
                    }
                    binding.tkFormPilihdivisi.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal memuat divisi", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        p0?.getItemAtPosition(p2)
        pref.prefiddivisikaryawan = listidDivisi[p2]

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}