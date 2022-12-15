package org.d3ifcool.jobmonitoring.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.databinding.FragmentHomePerusahaanBinding
import org.d3ifcool.jobmonitoring.model.PerusahaanModel
import org.d3ifcool.jobmonitoring.model.Preference
import kotlin.math.log


class HomePerusahaanFragment : Fragment() {

    private var backPressedTime = 0L

    private var _binding: FragmentHomePerusahaanBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    val storage = Firebase.storage
    private lateinit var pref: Preference
    private val data = arrayListOf<PerusahaanModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomePerusahaanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contextt : Context
        contextt = requireActivity()
        pref = Preference(contextt)



        binding.homePerusahaan.setOnRefreshListener {
            binding.homePerusahaan.isRefreshing = false
        }

        binding.coliderMenuDivisi.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_divisiFragment)
        }
        binding.coliderMenuPekerjaan.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_pekerjaanFragment)
        }
        binding.coliderMenuKaryawan.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_karyawanFragment)
        }
        binding.coliderMenuPresensi.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_presensiFragment)
        }
        binding.coliderMenuPengaturan.setOnClickListener {
            it.findNavController().navigate(R.id.action_homePerusahaanFragment_to_pengaturanFragment)
        }

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                binding.namaProfilPerusahaan.text = profile.displayName
                binding.alamatPerusahaan.text = pref.prefalamatperusahaan
            }
        }
        val id = user!!.uid
        val storageRef = storage.getReference("images").child("perusahaan").child(id).child("profil")
        storageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imgProfilPerusahaan.setImageBitmap(bitmap)
        }.addOnFailureListener {

        }


        val dbRef = database.getReference("Perusahaan").orderByChild("id").equalTo(user.uid)
        dbRef.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.exists()){
                    for (datasnap in snapshot.children){
                        val datas = datasnap.getValue(PerusahaanModel::class.java)
                        data.add(datas!!)
                        pref.prefnohpperusahaan = datas.nohp
                        pref.prefalamatperusahaan = datas.alamat
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Gagal Memuat", Toast.LENGTH_LONG).show()
            }

        })
    }
}