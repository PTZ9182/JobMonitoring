package org.d3ifcool.jobmonitoring.ui.loginRegister

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentLoginPerusahaanBinding

class LoginPerusahaanFragment : Fragment() {

    private var _binding: FragmentLoginPerusahaanBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    lateinit var nDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginPerusahaanBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            binding.loginPerusahaan.isRefreshing = false
        }
        binding.lpRegis.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginPerusahaanFragment_to_registerFragment)
        }

        binding.lpButtonLogin.setOnClickListener {
            if (binding.lpIsiformEmail.text.isEmpty()){
                binding.lpIsiformEmail.setError("Masukan Email")
                binding.lpIsiformEmail.requestFocus()
            } else if (binding.lpIsiformPassword.text.isEmpty()){
                binding.lpIsiformPassword.requestFocus()
                binding.lpIsiformPassword.setError("Masukan Password")
            } else {
                login(binding.lpIsiformEmail.text.toString(),binding.lpIsiformPassword.text.toString())
            }
        }
    }

    private fun login(email:String, password:String ) {
        nDialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.getResult() != null) {
                    if (task.getResult().user != null) {
                        nDialog.cancel()
                        findNavController().navigate(R.id.action_loginPerusahaanFragment_to_homePerusahaanFragment)
                    } else {
                        nDialog.cancel()
                        Toast.makeText(activity, "Login Gagal", Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }
}