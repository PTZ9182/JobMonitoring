@file:Suppress("DEPRECATION")

package org.d3ifcool.jobmonitoring

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        @Suppress("DEPRECATION") val Splash_Time_Out :Long = 3000
        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_praLoginFragment)
        }, Splash_Time_Out)
        return binding.root
    }

}