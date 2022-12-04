package org.d3ifcool.jobmonitoring.ui.pengaturan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentPengaturanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class PengaturanKaryawanFragment : Fragment(){

    private lateinit var pref: Preference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPengaturanBinding.inflate(layoutInflater)

        binding.logout.setOnClickListener{
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_pengaturanKaryawanFragment_to_praLoginFragment)
        }
        return binding.root
    }
}