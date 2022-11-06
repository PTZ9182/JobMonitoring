package org.d3ifcool.jobmonitoring.data

data class ListDivisiModel(
    val tabel_divisi: List<Data>
) {
    data class Data(
        val id: Int,
        val divisi: String)
}
