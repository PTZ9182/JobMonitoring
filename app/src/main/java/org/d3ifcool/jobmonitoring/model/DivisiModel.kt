package org.d3ifcool.jobmonitoring.model

import com.google.firebase.database.Exclude


class DivisiModel (
    @Exclude
    var id : String = "",
    var divisi : String = ""
)