package org.d3ifcool.jobmonitoring.model

class PekerjaanModel(
    var id :String = "",
    var iduser : String = "",
    var divisi : String = "",
    val nama_pekerjaan: String = "",
    val deskripsi: String = "",
    val karyawan: String = "",
    val progress: Int = 0,
    val status: String = "0"
)
