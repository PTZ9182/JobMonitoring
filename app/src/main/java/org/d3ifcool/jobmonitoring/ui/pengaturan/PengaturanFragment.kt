package org.d3ifcool.jobmonitoring.ui.pengaturan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentPengaturanBinding

class PengaturanFragment : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPengaturanBinding.inflate(layoutInflater)

        binding.editProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_pengaturanFragment_to_editProfileFragment)
        }
        binding.gantiProfilePassword.setOnClickListener{
            it.findNavController().navigate(R.id.action_pengaturanFragment_to_gantiPasswordPerusahaanFragment)
        }
        binding.logout.setOnClickListener{
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_pengaturanFragment_to_praLoginFragment)
        }
        return binding.root
    }
}