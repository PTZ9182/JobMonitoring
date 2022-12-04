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

    fun prefClear(){
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_LEVEL).apply()
        pref.edit().remove(TAG_PERUSAHAAN).apply()
        pref.edit().remove(TAG_KEY).apply()
    }
}