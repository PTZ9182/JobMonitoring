package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.d3ifcool.jobmonitoring.databinding.FragmentEditProfileKaryawanBinding


class EditProfileKaryawanFragment : Fragment() {
    private var _binding: FragmentEditProfileKaryawanBinding? = null
    private val binding get() = _binding!!

    private lateinit var ImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditProfileKaryawanBinding.inflate(inflater, container, false)
        binding.alamatKaryawan.setOnClickListener {
            selectPicture()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSimpanDataPengaturan.setOnClickListener {
            val namakaryawan = binding.textNamaKaryawan.text.toString()
            val alamatkaryawan = binding.textAlamatkaryawan1.text.toString()
            val nohandphone = binding.textNohpkaryawan2.text.toString()

            //Validasi Nama Perusahaan
            if (namakaryawan.isEmpty()){
                binding.textNamaKaryawan.error = "Nama Karyawan Harus Di isi!!"
                binding.textNamaKaryawan.requestFocus()
                return@setOnClickListener
            }

            //Validasi alamatperusahaan
            if (alamatkaryawan.isEmpty()){
                binding.textAlamatkaryawan1.error = "Alamat Karyawan Harus Di isi!!"
                binding.textAlamatkaryawan1.requestFocus()
                return@setOnClickListener
            }


            //Validasi Password
            if (nohandphone.isEmpty()){
                binding.textNohpkaryawan2.error = "NoHandphone Harus Di isi!!"
                binding.textNohpkaryawan2.requestFocus()
                return@setOnClickListener
            }

            //Validasi Panjang Password
            if (nohandphone.length < 10){
                binding.textNohpkaryawan2.error = "NoHandphone Minimal 8 Karakter!!"
                binding.textNohpkaryawan2.requestFocus()
                return@setOnClickListener
            }
        }
    }


    private fun selectPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            ImageUri = data?.data!!
            binding.imgProfilKaryawan.setImageURI(ImageUri)
        }
    }
}