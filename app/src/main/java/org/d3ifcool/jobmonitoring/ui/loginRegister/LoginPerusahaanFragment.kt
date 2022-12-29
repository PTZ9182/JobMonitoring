package org.d3ifcool.jobmonitoring.ui.loginRegister

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentLoginPerusahaanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class LoginPerusahaanFragment : Fragment() {

    private var _binding: FragmentLoginPerusahaanBinding? = null
    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth
    private lateinit var pref: Preference
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginPerusahaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        nDialog = ProgressDialog(activity)
        nDialog.setMessage("Tunggu..")
        nDialog.setTitle("Sedang Login")
        nDialog.setIndeterminate(false)
        nDialog.setCancelable(true)

        binding.loginPerusahaan.setOnRefreshListener {
            activity?.let { recreate(it) }
            binding.loginPerusahaan.isRefreshing = false
        }

        binding.lpRegis.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginPerusahaanFragment_to_registerFragment)
        }

        binding.lpButtonLogin.setOnClickListener {
            val email = binding.lpIsiformEmail.text.toString()
            if (binding.lpIsiformEmail.text.isEmpty()) {
                binding.lpIsiformEmail.error = "Masukan Email"
                binding.lpIsiformEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.lpIsiformEmail.error = "Email Tidak Valid!!!!"
                binding.lpIsiformEmail.requestFocus()
            } else if (binding.lpIsiformPassword.text.isEmpty()){
                binding.lpIsiformPassword.requestFocus()
                binding.lpIsiformPassword.error = "Masukan Password"
            } else {
                login(binding.lpIsiformEmail.text.toString(),binding.lpIsiformPassword.text.toString())
            }
        }
    }

    private fun login(email:String, password:String ) {
        nDialog.show()
        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)
        nDialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    if (task.result.user != null) {
                        pref.prefpasswordperusahaan = binding.lpIsiformPassword.text.toString()
                        nDialog.cancel()
                        findNavController().navigate(R.id.action_loginPerusahaanFragment_to_homePerusahaanFragment)
                    } else {
                        nDialog.cancel()
                        Toast.makeText(activity, "Akun perusahaan belum terdaftar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    nDialog.cancel()
                    Toast.makeText(activity, "Login Gagal!", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun reload(){
        findNavController().navigate(R.id.action_loginPerusahaanFragment_to_homePerusahaanFragment)
    }
}