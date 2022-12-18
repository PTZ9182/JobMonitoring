package org.d3ifcool.jobmonitoring.model

class PekerjaanModel(
    var id :String = "",
    var idkaryawan : String = "",
    var iddivisi : String = "",
    val nama_pekerjaan: String = "",
    val deskripsi: String = "",
    val progress: Int = 0,
    val status: String = "0"
)
