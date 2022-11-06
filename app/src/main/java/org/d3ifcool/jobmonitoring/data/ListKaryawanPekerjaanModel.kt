package org.d3ifcool.jobmonitoring.data

data class ListKaryawanPekerjaanModel(
    val list_karyawan_pekerjaan: List<Data>
) {
    data class Data(
        val id: Int,
        val nama_karyawan: String)
}
