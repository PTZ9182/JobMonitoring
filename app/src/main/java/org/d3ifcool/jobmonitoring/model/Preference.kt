package org.d3ifcool.jobmonitoring.model

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class Preference(context: Context) {
    private val TAG_STATUS = "status"
    private val TAG_LEVEL  = "level"
    private val TAG_APP = "app"

    //PERUSAHAAN
    private val TAG_PASSWORDPERUSAHAAN = "password perusahaan"
    private val TAG_ALAMATPERUSAHAAN = "alamat perusahaan"
    private val TAG_NOHPPERUSAHAAN = "nohp perusahaan"
    private val TAG_IMGPERUSAHAAN = "img perusahaan"
    private val TAG_NAMAPERUSAHAAN = "nama perusahaan"
    private val TAG_EMAILPERUSAHAAN = "email perusahaan"

    //DIVISI
    private val TAG_IDDIVISI = "id divisi"
    private val TAG_DIVISI = "divisi"
    private val TAG_JDIVISI = "jumlah divisi"

    //KARYAWAN
    private val TAG_IDKARYAWAN = "id karyawan"
    private val TAG_NAMAKARYAWAN = "nama karyawan"
    private val TAG_TANGGALLAHIRKARYAWAN = "tanggal lahir karyawan"
    private val TAG_JENISKELAMINKARYAWAN = "jenis kelamin karyawan"
    private val TAG_ALAMATKARYAWAN = "alamat karyawan"
    private val TAG_NOHPKARYAWAN = "nohp karyawan"
    private val TAG_DIVISIKARYAWAN = "divisi karyawan"
    private val TAG_EMAILKARYAWAN = "email karyawan"
    private val TAG_PASSWORDKARYAWAN = "password karyawan"
    private val TAG_JKARYAWAN = "jumlah karyawan"
    private val TAG_FILTERKARYAWAN = "filter karyawan"

    //PRESENSI
    private val TAG_IDPRESENSI = "id presensi"
    private val TAG_IDUSERPRESENSI = "iduser presensi"
    private val TAG_NAMAPRESENSI = "nama user presensi"
    private val TAG_DIVISIPRESENSI = "divisi presensi"
    private val TAG_KETERANGANPRESENSI = "keterangan presensi"
    private val TAG_WAKTUPRESENSI = "waktu presensi"
    private val TAG_TANGGALPRESENSI = "tanggal presensi"
    private val TAG_JPRESENSI = "jumlah presensi"
    private val TAG_FILTERPRESENSI = "filter presensi"
    private val TAG_JBUTTONRESENSI = "button presensi"

    //PEKERJAAN
    private val TAG_IDPEKERJAAN = "id pekerjaan"
    private val TAG_DIVISIPEKERJAAN = "divisi pekerjaan"
    private val TAG_NAMAPEKERJAAN = "nama pekerjaan"
    private val TAG_DESKRIPSIPEKERJAAN = "deskripsi pekerjaan"
    private val TAG_KARYAWANPEKERJAAN = "karyawan pekerjaan"
    private val TAG_JPEKERJAAN = "jumlah pekerjaan"
    private val TAG_STATUSPEKERJAAN = "status pekerjaan"

    //USER KARYAWAN
    private val TAG_IDPERUSAHAANUSER = "id perusahaan user"
    private val TAG_PERUSAHAANUSER = "perusahaan user"
    private val TAG_IDUSER = "id user"
    private val TAG_NAMAUSER = "nama user"
    private val TAG_TANGGALLAHIRUSER = "tanggal lahir user"
    private val TAG_JENISKELAMINUSER = "jenis kelamin user"
    private val TAG_ALAMATUSER = "alamat user"
    private val TAG_NOHPUSER = "nohp user"
    private val TAG_DIVISIUSER = "divisi user"
    private val TAG_EMAILUSER = "email user"
    private val TAG_PASSWORDUSER = "password user"
    private val TAG_IMGUSER = "img user"

    //USER PEKERJAAN
    private val TAG_IDPEKERJAANUSER = "id pekerjaan user"
    private val TAG_DIVISIPEKERJAANUSER = "divisi pekerjaan user"
    private val TAG_NAMAPEKERJAANUSER = "nama pekerjaan user"
    private val TAG_DESKRIPSIPEKERJAANUSER = "deskripsi pekerjaan user"
    private val TAG_KARYAWANPEKERJAANUSER = "karyawan pekerjaan user"
    private val TAG_STATUSPEKERJAANUSER = "status pekerjaan user"

    private val pref : SharedPreferences = context.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE)

    var prefstatus:Boolean
    get() = pref.getBoolean(TAG_STATUS,false)
    set(value) = pref.edit().putBoolean(TAG_STATUS,value).apply()

    var preflevel: String?
        get() = pref.getString(TAG_LEVEL,"")
        set(value) = pref.edit().putString(TAG_LEVEL,value).apply()

    //PERUSAHAAN
    var prefpasswordperusahaan: String?
        get() = pref.getString(TAG_PASSWORDPERUSAHAAN,"")
        set(value) = pref.edit().putString(TAG_PASSWORDPERUSAHAAN,value).apply()

    var prefalamatperusahaan: String?
        get() = pref.getString(TAG_ALAMATPERUSAHAAN,"")
        set(value) = pref.edit().putString(TAG_ALAMATPERUSAHAAN,value).apply()

    var prefnohpperusahaan: String?
        get() = pref.getString(TAG_NOHPPERUSAHAAN,"")
        set(value) = pref.edit().putString(TAG_NOHPPERUSAHAAN,value).apply()

    var prefimgperusahaan: String?
        get() = pref.getString(TAG_IMGPERUSAHAAN,"")
        set(value) = pref.edit().putString(TAG_IMGPERUSAHAAN,value).apply()

    var prefnamaperusahaan: String?
        get() = pref.getString(TAG_NAMAPERUSAHAAN,"")
        set(value) = pref.edit().putString(TAG_NAMAPERUSAHAAN,value).apply()

    var prefemailperusahaan: String?
        get() = pref.getString(TAG_EMAILPERUSAHAAN,"")
        set(value) = pref.edit().putString(TAG_EMAILPERUSAHAAN,value).apply()

    //Divisi
    var prefiddivisi: String?
        get() = pref.getString(TAG_IDDIVISI,"")
        set(value) = pref.edit().putString(TAG_IDDIVISI,value).apply()

    var prefdivisi: String?
        get() = pref.getString(TAG_DIVISI,"")
        set(value) = pref.edit().putString(TAG_DIVISI,value).apply()

    var prefjdivisi: Int?
        get() = pref.getInt(TAG_JDIVISI,0)
        set(value) = pref.edit().putInt(TAG_JDIVISI,value!!).apply()

    //Karyawan
    var prefidkaryawan: String?
        get() = pref.getString(TAG_IDKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_IDKARYAWAN,value).apply()

    var prefnamakaryawan: String?
        get() = pref.getString(TAG_NAMAKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_NAMAKARYAWAN,value).apply()

    var preftanggallahirkaryawan: String?
        get() = pref.getString(TAG_TANGGALLAHIRKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_TANGGALLAHIRKARYAWAN,value).apply()

    var prefjeniskelaminkaryawan: String?
        get() = pref.getString(TAG_JENISKELAMINKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_JENISKELAMINKARYAWAN,value).apply()

    var prefalamatkaryawan: String?
        get() = pref.getString(TAG_ALAMATKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_ALAMATKARYAWAN,value).apply()

    var prefnohpkaryawan: String?
        get() = pref.getString(TAG_NOHPKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_NOHPKARYAWAN,value).apply()

    var prefdivisikaryawan: String?
        get() = pref.getString(TAG_DIVISIKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_DIVISIKARYAWAN,value).apply()

    var prefemailkaryawan: String?
        get() = pref.getString(TAG_EMAILKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_EMAILKARYAWAN,value).apply()

    var prefpasswordkaryawan: String?
        get() = pref.getString(TAG_PASSWORDKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_PASSWORDKARYAWAN,value).apply()

    var prefjkaryawan: Int?
        get() = pref.getInt(TAG_JKARYAWAN,0)
        set(value) = pref.edit().putInt(TAG_JKARYAWAN,value!!).apply()

    var preffilterkaryawan: String?
        get() = pref.getString(TAG_FILTERKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_FILTERKARYAWAN,value).apply()

    //Presensi
    var prefidpresensi: String?
        get() = pref.getString(TAG_IDPRESENSI,"")
        set(value) = pref.edit().putString(TAG_IDPRESENSI,value).apply()

    var prefiduserpresensi: String?
        get() = pref.getString(TAG_IDUSERPRESENSI,"")
        set(value) = pref.edit().putString(TAG_IDUSERPRESENSI,value).apply()

    var prefnamauserpresensi: String?
        get() = pref.getString(TAG_NAMAPRESENSI,"")
        set(value) = pref.edit().putString(TAG_NAMAPRESENSI,value).apply()

    var prefdivisipresensi: String?
        get() = pref.getString(TAG_DIVISIPRESENSI,"")
        set(value) = pref.edit().putString(TAG_DIVISIPRESENSI,value).apply()

    var prefketeranganpresensi: String?
        get() = pref.getString(TAG_KETERANGANPRESENSI,"")
        set(value) = pref.edit().putString(TAG_KETERANGANPRESENSI,value).apply()

    var prefwaktupresensi: String?
        get() = pref.getString(TAG_WAKTUPRESENSI,"")
        set(value) = pref.edit().putString(TAG_WAKTUPRESENSI,value).apply()

    var preftanggalpresensi: String?
        get() = pref.getString(TAG_TANGGALPRESENSI,"")
        set(value) = pref.edit().putString(TAG_TANGGALPRESENSI,value).apply()

    var prefjpresesnsi: Int?
        get() = pref.getInt(TAG_JPRESENSI,0)
        set(value) = pref.edit().putInt(TAG_JPRESENSI,value!!).apply()

    var preffilterpresensi: String?
        get() = pref.getString(TAG_FILTERPRESENSI,"")
        set(value) = pref.edit().putString(TAG_FILTERPRESENSI,value).apply()

    var prefbuttonpresensi: String?
        get() = pref.getString(TAG_JBUTTONRESENSI,"")
        set(value) = pref.edit().putString(TAG_JBUTTONRESENSI,value!!).apply()

    //Pekerjaan
    var prefidpekerjaan: String?
        get() = pref.getString(TAG_IDPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_IDPEKERJAAN,value).apply()

    var prefdivisipekerjaan: String?
        get() = pref.getString(TAG_DIVISIPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_DIVISIPEKERJAAN,value).apply()

    var prefnamapekerjaan: String?
        get() = pref.getString(TAG_NAMAPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_NAMAPEKERJAAN,value).apply()

    var prefdeskripsipekerjaan: String?
        get() = pref.getString(TAG_DESKRIPSIPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_DESKRIPSIPEKERJAAN,value).apply()

    var prefkaryawanpekerjaan: String?
        get() = pref.getString(TAG_KARYAWANPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_KARYAWANPEKERJAAN,value).apply()

    var prefstatuspekerjaan: String?
        get() = pref.getString(TAG_STATUSPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_STATUSPEKERJAAN,value).apply()

    var prefjpekerjaan: Int?
        get() = pref.getInt(TAG_JPEKERJAAN,0)
        set(value) = pref.edit().putInt(TAG_JPEKERJAAN,value!!).apply()

    //USER KARYAWAN
    var prefidperusahaanuser: String?
        get() = pref.getString(TAG_IDPERUSAHAANUSER,"")
        set(value) = pref.edit().putString(TAG_IDPERUSAHAANUSER,value!!).apply()

    var prefperusahaanuser: String?
        get() = pref.getString(TAG_PERUSAHAANUSER,"")
        set(value) = pref.edit().putString(TAG_PERUSAHAANUSER,value!!).apply()

    var prefiduser: String?
        get() = pref.getString(TAG_IDUSER,"")
        set(value) = pref.edit().putString(TAG_IDUSER,value!!).apply()

    var prefnamauser: String?
        get() = pref.getString(TAG_NAMAUSER,"")
        set(value) = pref.edit().putString(TAG_NAMAUSER,value!!).apply()

    var preftanggallahiruser: String?
        get() = pref.getString(TAG_TANGGALLAHIRUSER,"")
        set(value) = pref.edit().putString(TAG_TANGGALLAHIRUSER,value).apply()

    var prefjeniskelaminuser: String?
        get() = pref.getString(TAG_JENISKELAMINUSER,"")
        set(value) = pref.edit().putString(TAG_JENISKELAMINUSER,value).apply()

    var prefalamatuser: String?
        get() = pref.getString(TAG_ALAMATUSER,"")
        set(value) = pref.edit().putString(TAG_ALAMATUSER,value).apply()

    var prefnohpuser: String?
        get() = pref.getString(TAG_NOHPUSER,"")
        set(value) = pref.edit().putString(TAG_NOHPUSER,value).apply()

    var prefdivisiuser: String?
        get() = pref.getString(TAG_DIVISIUSER,"")
        set(value) = pref.edit().putString(TAG_DIVISIUSER,value).apply()

    var prefemailuser: String?
        get() = pref.getString(TAG_EMAILUSER,"")
        set(value) = pref.edit().putString(TAG_EMAILUSER,value).apply()

    var prefpassworduser: String?
        get() = pref.getString(TAG_PASSWORDUSER,"")
        set(value) = pref.edit().putString(TAG_PASSWORDUSER,value).apply()

    var prefimguser: String?
        get() = pref.getString(TAG_IMGUSER,"")
        set(value) = pref.edit().putString(TAG_IMGUSER,value).apply()

    //USER PEKERJAAN
    var prefidpekerjaanuser: String?
        get() = pref.getString(TAG_IDPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_IDPEKERJAANUSER,value).apply()

    var prefdivisipekerjaanuser: String?
        get() = pref.getString(TAG_DIVISIPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_DIVISIPEKERJAANUSER,value).apply()

    var prefnamapekerjaanuser: String?
        get() = pref.getString(TAG_NAMAPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_NAMAPEKERJAANUSER,value).apply()

    var prefdeskripsipekerjaanuser: String?
        get() = pref.getString(TAG_DESKRIPSIPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_DESKRIPSIPEKERJAANUSER,value).apply()

    var prefkaryawanpekerjaanuser: String?
        get() = pref.getString(TAG_KARYAWANPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_KARYAWANPEKERJAANUSER,value).apply()

    var prefstatuspekerjaanuser: String?
        get() = pref.getString(TAG_STATUSPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_STATUSPEKERJAANUSER,value).apply()
    //


    fun prefClear(){
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_LEVEL).apply()

        //PERUSHAAN
        pref.edit().remove(TAG_PASSWORDPERUSAHAAN).apply()
        pref.edit().remove(TAG_ALAMATPERUSAHAAN).apply()
        pref.edit().remove(TAG_NOHPPERUSAHAAN).apply()
        pref.edit().remove(TAG_IMGPERUSAHAAN).apply()
        //DIVISI
        pref.edit().remove(TAG_IDDIVISI).apply()
        pref.edit().remove(TAG_DIVISI).apply()
        pref.edit().remove(TAG_JDIVISI).apply()
        //KARYAWAN
        pref.edit().remove(TAG_IDKARYAWAN).apply()
        pref.edit().remove(TAG_NAMAKARYAWAN).apply()
        pref.edit().remove(TAG_TANGGALLAHIRKARYAWAN).apply()
        pref.edit().remove(TAG_JENISKELAMINKARYAWAN).apply()
        pref.edit().remove(TAG_ALAMATKARYAWAN).apply()
        pref.edit().remove(TAG_NOHPKARYAWAN).apply()
        pref.edit().remove(TAG_DIVISIKARYAWAN).apply()
        pref.edit().remove(TAG_EMAILKARYAWAN).apply()
        pref.edit().remove(TAG_PASSWORDKARYAWAN).apply()
        pref.edit().remove(TAG_JKARYAWAN).apply()
        pref.edit().remove(TAG_FILTERKARYAWAN).apply()
        //PEKERJAAN
        pref.edit().remove(TAG_IDPEKERJAAN).apply()
        pref.edit().remove(TAG_DIVISIPEKERJAAN).apply()
        pref.edit().remove(TAG_NAMAPEKERJAAN).apply()
        pref.edit().remove(TAG_DESKRIPSIPEKERJAAN).apply()
        pref.edit().remove(TAG_KARYAWANPEKERJAAN).apply()
        pref.edit().remove(TAG_JPEKERJAAN).apply()
        pref.edit().remove(TAG_STATUSPEKERJAAN).apply()
        //PRESENSI
        pref.edit().remove(TAG_IDPRESENSI).apply()
        pref.edit().remove(TAG_IDUSERPRESENSI).apply()
        pref.edit().remove(TAG_NAMAPRESENSI).apply()
        pref.edit().remove(TAG_DIVISIPRESENSI).apply()
        pref.edit().remove(TAG_KETERANGANPRESENSI).apply()
        pref.edit().remove(TAG_WAKTUPRESENSI).apply()
        pref.edit().remove(TAG_TANGGALPRESENSI).apply()
        pref.edit().remove(TAG_JPRESENSI).apply()
        pref.edit().remove(TAG_FILTERPRESENSI).apply()
        pref.edit().remove(TAG_JBUTTONRESENSI).apply()
        //USER KARYAWAN
        pref.edit().remove(TAG_IDPERUSAHAANUSER).apply()
        pref.edit().remove(TAG_PERUSAHAANUSER).apply()
        pref.edit().remove(TAG_IDUSER).apply()
        pref.edit().remove(TAG_NAMAUSER).apply()
        pref.edit().remove(TAG_TANGGALLAHIRUSER).apply()
        pref.edit().remove(TAG_JENISKELAMINUSER).apply()
        pref.edit().remove(TAG_ALAMATUSER).apply()
        pref.edit().remove(TAG_NOHPUSER).apply()
        pref.edit().remove(TAG_DIVISIUSER).apply()
        pref.edit().remove(TAG_EMAILUSER).apply()
        pref.edit().remove(TAG_PASSWORDUSER).apply()
        pref.edit().remove(TAG_IMGUSER).apply()
        //USER PEKERJAAN
        pref.edit().remove(TAG_IDPEKERJAANUSER).apply()
        pref.edit().remove(TAG_DIVISIPEKERJAANUSER).apply()
        pref.edit().remove(TAG_NAMAPEKERJAANUSER).apply()
        pref.edit().remove(TAG_DESKRIPSIPEKERJAANUSER).apply()
        pref.edit().remove(TAG_KARYAWANPEKERJAANUSER).apply()
        pref.edit().remove(TAG_STATUSPEKERJAANUSER).apply()
    }
}