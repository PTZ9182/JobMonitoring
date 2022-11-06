package org.d3ifcool.jobmonitoring.model

data class PresensiModel(
    val tabel_presensi: List<Data>
) {
    data class Data(
        val id: Int?,
        val nama_karyawan: String?,
        val perusahaan: String?,
        val status: String?)
}
