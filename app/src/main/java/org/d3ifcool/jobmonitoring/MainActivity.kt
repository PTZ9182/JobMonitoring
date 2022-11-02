package org.d3ifcool.jobmonitoring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.jobmonitoring.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}