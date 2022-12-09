package org.d3ifcool.jobmonitoring.model

class PerusahaanModel(
    var id:  String = "",
    var perusahaan: String = "",
    var email: String = "",
    var password: String = "",
    var alamat: String? = null,
    var nohp: String? = null,
    var level: String = "Admin"
)