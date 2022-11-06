package org.d3ifcool.jobmonitoring.data

import java.io.Serializable

class DivisiModel (
    val tabel_divisi: List<Data>
    ) {
        data class Data(val id: String?, val divisi: String?) : Serializable
}