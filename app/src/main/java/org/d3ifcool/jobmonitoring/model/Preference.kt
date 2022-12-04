package org.d3ifcool.jobmonitoring.model

import android.content.Context
import android.content.SharedPreferences

class Preference(context: Context) {
    private val TAG_STATUS = "status"
    private val TAG_LEVEL  = "level"
    private val TAG_APP = "app"
    private val TAG_PERUSAHAAN = "perusahaan"
    private val TAG_KEY = "key"
    private val TAG_FILKAR = "filterkaryawan"
    private val TAG_PEKDIV = "filter berdasarkan divisi"
    private val TAG_STATT = "status divisi"
    private val TAG_NAMEKAR = "nama karyawan"

    //pekerjaan karyawan set
    private val TAG_KPID = "id"
    private val TAG_KPDIV = "divisi"
    private val TAG_KPNP = "namapekerjaan"
    private val TAG_KPDESC = "desc"
    private val TAG_KPDNK = "namakaryawan"

    private val pref : SharedPreferences = context.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE)

    var prefstatus:Boolean
    get() = pref.getBoolean(TAG_STATUS,false)
    set(value) = pref.edit().putBoolean(TAG_STATUS,value).apply()

    var preflevel: String?
        get() = pref.getString(TAG_LEVEL,"")
        set(value) = pref.edit().putString(TAG_LEVEL,value).apply()

    var prefperusahaan: String?
        get() = pref.getString(TAG_PERUSAHAAN,"")
        set(value) = pref.edit().putString(TAG_PERUSAHAAN,value).apply()

    var prefkey: String?
        get() = pref.getString(TAG_KEY,"")
        set(value) = pref.edit().putString(TAG_KEY,value).apply()

    var preffilkar: String?
        get() = pref.getString(TAG_FILKAR,"")
        set(value) = pref.edit().putString(TAG_FILKAR,value).apply()

    var prefpekdiv: String?
        get() = pref.getString(TAG_PEKDIV,"")
        set(value) = pref.edit().putString(TAG_PEKDIV,value).apply()

    var prefstatt: String?
        get() = pref.getString(TAG_STATT,"")
        set(value) = pref.edit().putString(TAG_STATT,value).apply()

    var prefnamekar: String?
        get() = pref.getString(TAG_NAMEKAR,"")
        set(value) = pref.edit().putString(TAG_NAMEKAR,value).apply()

    var prefkpid: String?
        get() = pref.getString(TAG_KPID,"")
        set(value) = pref.edit().putString(TAG_KPID,value).apply()

    var prefkpdiv: String?
        get() = pref.getString(TAG_KPDIV,"")
        set(value) = pref.edit().putString(TAG_KPDIV,value).apply()

    var prefkpnp: String?
        get() = pref.getString(TAG_KPNP,"")
        set(value) = pref.edit().putString(TAG_KPNP,value).apply()

    var prefkpdesc: String?
        get() = pref.getString(TAG_KPDESC,"")
        set(value) = pref.edit().putString(TAG_KPDESC,value).apply()

    var prefkpdnk: String?
        get() = pref.getString(TAG_KPDNK,"")
        set(value) = pref.edit().putString(TAG_KPDNK,value).apply()

    fun prefClear(){
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_LEVEL).apply()
        pref.edit().remove(TAG_PERUSAHAAN).apply()
        pref.edit().remove(TAG_KEY).apply()
    }
}