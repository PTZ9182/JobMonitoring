package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.hdodenhof.circleimageview.CircleImageView
import org.d3ifcool.jobmonitoring.databinding.FragmentEditProfileBinding



class EditProfileFragment : Fragment() {
    //private lateinit var TextView: TextView
    //private lateinit var imageView: CircleImageView
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //companion object{
        //val IMAGE_REQUEST_CODE = 100
    //}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //TextView.setOnClickListener {
            //pickImageGallery()

        //}



        binding.buttonSimpanDataPengaturan.setOnClickListener {
            val namaperusahaan = binding.textPasswordLamaDalamForm.text.toString()
            val alamatperusahaan = binding.textPasswordBaru1DalamForm.text.toString()
            val nohandphone = binding.textNohp2.text.toString()
            val email = binding.textEmail3.text.toString()

            //Validasi Nama Perusahaan
            if (namaperusahaan.isEmpty()){
                binding.textPasswordLamaDalamForm.error = "Nama Perusahaan Harus Di isi!!"
                binding.textPasswordLamaDalamForm.requestFocus()
                return@setOnClickListener
            }

            //Validasi alamatperusahaan
            if (alamatperusahaan.isEmpty()){
                binding.textPasswordBaru1DalamForm.error = "Alamat Perusahaan Harus Di isi!!"
                binding.textPasswordBaru1DalamForm.requestFocus()
                return@setOnClickListener
            }


            //Validasi Password
            if (nohandphone.isEmpty()){
                binding.textNohp2.error = "NoHandphone Harus Di isi!!"
                binding.textNohp2.requestFocus()
                return@setOnClickListener
            }

            //Validasi Panjang Password
            if (nohandphone.length < 10){
                binding.textNohp2.error = "NoHandphone Minimal 8 Karakter!!"
                binding.textNohp2.requestFocus()
                return@setOnClickListener
            }

            //Validasi Email
            if (email.isEmpty()){
                binding.textEmail3.error = "Email Harus Di isi!!"
                binding.textEmail3.requestFocus()
                return@setOnClickListener
            }
        }
    }


    //private fun pickImageGallery() {
        //val intent = Intent(Intent.ACTION_PICK)
        //intent.type = "image/*"
        //startActivityForResult(intent, IMAGE_REQUEST_CODE)
    //}


    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        //if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            //imageView.setImageURI(data?.data)
        //}
    //}
}