package org.d3ifcool.jobmonitoring.data

data class PekerjaanModel(
    val tabel_pekerjaan: List<Data>
) {
    data class Data(
        val id: Int?,
        val nama_pekerjaan: String?,
        val deskripsi: String?,
        val perusahaan: String?,
        val karyawan: String?,
        val status: String?)
}
