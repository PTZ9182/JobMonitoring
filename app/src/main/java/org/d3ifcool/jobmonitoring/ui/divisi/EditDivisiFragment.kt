package org.d3ifcool.jobmonitoring.ui.divisi


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.d3ifcool.jobmonitoring.api.ApiRetrofit
import org.d3ifcool.jobmonitoring.R
import org.d3ifcool.jobmonitoring.data.SubmitDivisiModel
import org.d3ifcool.jobmonitoring.databinding.FragmentEditDivisiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditDivisiFragment : Fragment() {

    private val api by lazy { ApiRetrofit().endpoint }
    private var _binding: FragmentEditDivisiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditDivisiBinding.inflate(inflater, container, false)
        setFL()
        return binding.root
    }

    private fun setFL() {
        setFragmentResultListener("divisi") { requestKey, bundle ->
            val result = bundle.getString("divisi")
            binding.textNamaDivisiDalamForm.setText(result)
        }
        setFragmentResultListener("id") { requestKey, bundle ->
            val result = bundle.getString("id")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editDivisi()

    }

    private fun editDivisi() {
        setFragmentResultListener("id2") { requestKey, bundle ->
            val result = bundle.getString("id2")
            binding.buttonEditDivisiForm.setOnClickListener {
                api.update(result!!, binding.textNamaDivisiDalamForm.text.toString())
                    .enqueue(object : Callback<SubmitDivisiModel> {
                        override fun onResponse(
                            call: Call<SubmitDivisiModel>,
                            response: Response<SubmitDivisiModel>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "Perubahan Berhasil",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_editDivisiFragment_to_divisiFragment)
                            } else
                                Toast.makeText(
                                    context, "Perubahan Gagal",
                                    Toast.LENGTH_LONG
                                ).show()
                        }

                        override fun onFailure(call: Call<SubmitDivisiModel>, t: Throwable) {
                            Toast.makeText(
                                context, "Perubahan Gagal",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }

    }
}
