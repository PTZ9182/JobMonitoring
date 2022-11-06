package org.d3ifcool.jobmonitoring.data

class KaryawanModel(
    val tabel_karyawan: List<Data>
) {
    data class Data(
        val id: Int?,
        val nama_karyawan   : String?,
        val tanggal_lahir   : String?,
        val jenis_kelamin   : String?,
        val alamat          : String?,
        val no_hp           : String?,
        val divisi          : String?,
        val email           : String?,
        val password        : String?,
        val perusahaan      : String?,)
}