package org.d3ifcool.jobmonitoring.ui.pengaturan


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentPengaturanBinding
import org.d3ifcool.jobmonitoring.model.Preference

class PengaturanFragment : Fragment(){

    private var _binding: FragmentPengaturanBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: Preference
    lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPengaturanBinding.inflate(inflater, container, false)

        val contextt: Context
        contextt = requireActivity()
        pref = Preference(contextt)

        binding.constraintLayout4.setOnClickListener {
            it.findNavController().navigate(R.id.action_pengaturanFragment_to_editProfileFragment)
        }
        binding.constraintLayout3.setOnClickListener{
            it.findNavController().navigate(R.id.action_pengaturanFragment_to_gantiPasswordPerusahaanFragment)
        }
        binding.constraintLayout5.setOnClickListener {
            findNavController().navigate(R.id.action_pengaturanFragment_to_gantiEmailFragment)
        }
        binding.constraintLayout2.setOnClickListener{
            pref.prefClear()
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_pengaturanFragment_to_praLoginFragment)
        }
        return binding.root
    }
}