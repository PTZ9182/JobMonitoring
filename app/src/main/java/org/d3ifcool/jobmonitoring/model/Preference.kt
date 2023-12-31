package org.d3ifcool.jobmonitoring.model

import android.content.Context
import android.content.SharedPreferences
class Preference(context: Context) {
    private val TAG_STATUS = "status"
    private val TAG_LEVEL  = "level"
    private val TAG_APP = "app"

    //PERUSAHAAN
    private val TAG_PASSWORDPERUSAHAAN = "password perusahaan"
    private val TAG_ALAMATPERUSAHAAN = "alamat perusahaan"
    private val TAG_NOHPPERUSAHAAN = "nohp perusahaan"

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
    private val TAG_EMAILKARYAWAN = "email karyawan"
    private val TAG_PASSWORDKARYAWAN = "password karyawan"
    private val TAG_JKARYAWAN = "jumlah karyawan"
    private val TAG_FILTERKARYAWAN = "filter karyawan"
    private val TAG_IDDIVISIKARYAWAN = "id divisi karyawan"

    //JADWAL PRESENSI
    private val TAG_IDJRESENSI = "id jadwal presensi"
    private val TAG_TANGGALJPRESENSI = "tanggal jadwal presensi"
    private val TAG_WAKTUMASUKJPRESENSI = "waktu masuk jadwal presensi"
    private val TAG_WAKTUKELUARJPRESENSI = "waktu keluar jadwal presensi"

    //PRESENSI
    private val TAG_IDPRESENSI = "id presensi"
    private val TAG_IDKARYAWANPRESENSI = "id karyawan presensi"
    private val TAG_IDDIVISIPRESENSI = "id divisi presensi"
    private val TAG_KETERANGANPRESENSI = "keterangan presensi"
    private val TAG_WAKTUMASUKPRESENSI = "waktu masuk presensi"
    private val TAG_WAKTUKELUARPRESENSI = "waktu keluar presensi"
    private val TAG_WAKTUMASUKPRESENSILONG = "waktu masuk presensi long"
    private val TAG_WAKTUKELUARPRESENSILONG = "waktu keluar presensi long"
    private val TAG_TANGGALPRESENSI = "tanggal presensi"
    private val TAG_JPRESENSI = "jumlah presensi"
    private val TAG_FILTERPRESENSI = "filter presensi"
    private val TAG_JBUTTONRESENSI = "button presensi"
    private val TAG_REKAPTANGGALPRESENSI = "rekap tanggal presensi"
    private val TAG_REKAPTITLEPRESENSI = "rekap title presensi"
    private val TAG_PEMBEDAPRESENSI = "pembeda presensi"

    //PEKERJAAN
    private val TAG_IDPEKERJAAN = "id pekerjaan"
    private val TAG_IDKARYAWANPEKERJAAN = "id karyawan pekerjaan"
    private val TAG_IDDIVISIPEKERJAAN = "ID divisi pekerjaan"
    private val TAG_NAMAPEKERJAAN = "nama pekerjaan"
    private val TAG_DESKRIPSIPEKERJAAN = "deskripsi pekerjaan"
    private val TAG_PROGRESSPEKERJAAN = "progress pekerjaan"
    private val TAG_STATUSPEKERJAAN = "status pekerjaan"
    private val TAG_JPEKERJAAN = "jumlah pekerjaan"
    private val TAG_PEMBEDAPEKERJAAN = "pembeda pekerjaan"
    private val TAG_NAMAKARYAWANPEKERJAAN = "nama karyawan pekerjaan"

    //USER KARYAWAN
    private val TAG_IDPERUSAHAANUSER = "id perusahaan user"
    private val TAG_PERUSAHAANUSER = "perusahaan user"
    private val TAG_IDUSER = "id user"
    private val TAG_NAMAUSER = "nama user"
    private val TAG_TANGGALLAHIRUSER = "tanggal lahir user"
    private val TAG_JENISKELAMINUSER = "jenis kelamin user"
    private val TAG_ALAMATUSER = "alamat user"
    private val TAG_NOHPUSER = "nohp user"
    private val TAG_IDDIVISIUSER = "divisi user"
    private val TAG_EMAILUSER = "email user"
    private val TAG_PASSWORDUSER = "password user"
    private val TAG_IMGUSER = "img user"

    //USER PEKERJAAN
    private val TAG_IDPEKERJAANUSER = "id pekerjaan user"
    private val TAG_IDKARYAWANPEKERJAANUSER = "id karyawan pekerjaan user"
    private val TAG_IDDIVISIPEKERJAANUSER = "id divisi pekerjaan user"
    private val TAG_NAMAPEKERJAANUSER = "nama pekerjaan user"
    private val TAG_DESKRIPSIPEKERJAANUSER = "deskripsi pekerjaan user"
    private val TAG_PROGRESSPEKERJAANUSER = "progress pekerjaan user"
    private val TAG_STATUSPEKERJAANUSER = "status pekerjaan user"
    private val TAG_IMGUSERPEKERJAAN = "img user pekerjaan"

    private val pref : SharedPreferences = context.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE)

    var prefstatus:Boolean
        get() = pref.getBoolean(TAG_STATUS,false)
        set(value) = pref.edit().putBoolean(TAG_STATUS,value).apply()


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

    var prefiddivisikaryawan: String?
        get() = pref.getString(TAG_IDDIVISIKARYAWAN,"")
        set(value) = pref.edit().putString(TAG_IDDIVISIKARYAWAN,value).apply()

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

    //JADWAL PRESENSI
    var prefidjpresensi: String?
        get() = pref.getString(TAG_IDJRESENSI,"")
        set(value) = pref.edit().putString(TAG_IDJRESENSI,value).apply()

    var preftanggaljpresensi: String?
        get() = pref.getString(TAG_TANGGALJPRESENSI,"")
        set(value) = pref.edit().putString(TAG_TANGGALJPRESENSI,value).apply()

    var prefwaktumasukjpresensi: String?
        get() = pref.getString(TAG_WAKTUMASUKJPRESENSI,"")
        set(value) = pref.edit().putString(TAG_WAKTUMASUKJPRESENSI,value).apply()

    var prefwaktukeluarjpresensi: String?
        get() = pref.getString(TAG_WAKTUKELUARJPRESENSI,"")
        set(value) = pref.edit().putString(TAG_WAKTUKELUARJPRESENSI,value).apply()

    //Presensi
    var prefidpresensi: String?
        get() = pref.getString(TAG_IDPRESENSI,"")
        set(value) = pref.edit().putString(TAG_IDPRESENSI,value).apply()

    var prefidkaryawanpresensi: String?
        get() = pref.getString(TAG_IDKARYAWANPRESENSI,"")
        set(value) = pref.edit().putString(TAG_IDKARYAWANPRESENSI,value).apply()

    var prefiddivisipresensi: String?
        get() = pref.getString(TAG_IDDIVISIPRESENSI,"")
        set(value) = pref.edit().putString(TAG_IDDIVISIPRESENSI,value).apply()

    var prefketeranganpresensi: String?
        get() = pref.getString(TAG_KETERANGANPRESENSI,"")
        set(value) = pref.edit().putString(TAG_KETERANGANPRESENSI,value).apply()

    var prefwaktumasukpresensi: String?
        get() = pref.getString(TAG_WAKTUMASUKPRESENSI,"")
        set(value) = pref.edit().putString(TAG_WAKTUMASUKPRESENSI,value).apply()

    var prefwaktukeluarpresensi: String?
        get() = pref.getString(TAG_WAKTUKELUARPRESENSI,"")
        set(value) = pref.edit().putString(TAG_WAKTUKELUARPRESENSI,value).apply()

    var prefwaktumasukpresensilong: Long
        get() = pref.getLong(TAG_WAKTUMASUKPRESENSILONG,1)
        set(value) = value.let { pref.edit().putLong(TAG_WAKTUMASUKPRESENSILONG, it).apply() }

    var prefwaktukeluarpresensilong: Long
        get() = pref.getLong(TAG_WAKTUKELUARPRESENSILONG,1)
        set(value) = value.let { pref.edit().putLong(TAG_WAKTUKELUARPRESENSILONG, it).apply() }

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

    var prefrekaptanggalpresensi: String?
        get() = pref.getString(TAG_REKAPTANGGALPRESENSI,"")
        set(value) = pref.edit().putString(TAG_REKAPTANGGALPRESENSI,value!!).apply()

    var prefrekaptitlepresensi: String?
        get() = pref.getString(TAG_REKAPTITLEPRESENSI,"")
        set(value) = pref.edit().putString(TAG_REKAPTITLEPRESENSI,value!!).apply()

    var prefpembedapresensi: String?
        get() = pref.getString(TAG_PEMBEDAPRESENSI,"")
        set(value) = pref.edit().putString(TAG_PEMBEDAPRESENSI,value!!).apply()

    //Pekerjaan
    var prefidpekerjaan: String?
        get() = pref.getString(TAG_IDPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_IDPEKERJAAN,value).apply()

    var prefidkaryawanpekerjaan: String?
        get() = pref.getString(TAG_IDKARYAWANPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_IDKARYAWANPEKERJAAN,value).apply()

    var prefiddivisipekerjaan: String?
        get() = pref.getString(TAG_IDDIVISIPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_IDDIVISIPEKERJAAN,value).apply()

    var prefnamapekerjaan: String?
        get() = pref.getString(TAG_NAMAPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_NAMAPEKERJAAN,value).apply()

    var prefdeskripsipekerjaan: String?
        get() = pref.getString(TAG_DESKRIPSIPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_DESKRIPSIPEKERJAAN,value).apply()

    var prefstatuspekerjaan: String?
        get() = pref.getString(TAG_STATUSPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_STATUSPEKERJAAN,value).apply()

    var prefprogresspekerjaan: String?
        get() = pref.getString(TAG_PROGRESSPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_PROGRESSPEKERJAAN,value).apply()

    var prefjpekerjaan: Int?
        get() = pref.getInt(TAG_JPEKERJAAN,0)
        set(value) = pref.edit().putInt(TAG_JPEKERJAAN,value!!).apply()

    var prefpembedapekerjaan: String?
        get() = pref.getString(TAG_PEMBEDAPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_PEMBEDAPEKERJAAN,value).apply()

    var prefnamakaryawanpekerjaan: String?
        get() = pref.getString(TAG_NAMAKARYAWANPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_NAMAKARYAWANPEKERJAAN,value).apply()


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

    var prefiddivisiuser: String?
        get() = pref.getString(TAG_IDDIVISIUSER,"")
        set(value) = pref.edit().putString(TAG_IDDIVISIUSER,value).apply()

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

    var prefidkaryawanpekerjaanuser: String?
        get() = pref.getString(TAG_IDKARYAWANPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_IDKARYAWANPEKERJAANUSER,value).apply()

    var prefiddivisipekerjaanuser: String?
        get() = pref.getString(TAG_IDDIVISIPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_IDDIVISIPEKERJAANUSER,value).apply()

    var prefnamapekerjaanuser: String?
        get() = pref.getString(TAG_NAMAPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_NAMAPEKERJAANUSER,value).apply()

    var prefdeskripsipekerjaanuser: String?
        get() = pref.getString(TAG_DESKRIPSIPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_DESKRIPSIPEKERJAANUSER,value).apply()

    var prefstatuspekerjaanuser: String?
        get() = pref.getString(TAG_STATUSPEKERJAANUSER,"")
        set(value) = pref.edit().putString(TAG_STATUSPEKERJAANUSER,value).apply()

    var prefimguserpekerjaan: String?
        get() = pref.getString(TAG_IMGUSERPEKERJAAN,"")
        set(value) = pref.edit().putString(TAG_IMGUSERPEKERJAAN,value).apply()
    //


    fun prefClear(){
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_LEVEL).apply()

        //PERUSHAAN
        pref.edit().remove(TAG_PASSWORDPERUSAHAAN).apply()
        pref.edit().remove(TAG_ALAMATPERUSAHAAN).apply()
        pref.edit().remove(TAG_NOHPPERUSAHAAN).apply()
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
        pref.edit().remove(TAG_EMAILKARYAWAN).apply()
        pref.edit().remove(TAG_PASSWORDKARYAWAN).apply()
        pref.edit().remove(TAG_JKARYAWAN).apply()
        pref.edit().remove(TAG_FILTERKARYAWAN).apply()
        pref.edit().remove(TAG_IDDIVISIKARYAWAN).apply()
        //PEKERJAAN
        pref.edit().remove(TAG_IDPEKERJAAN).apply()
        pref.edit().remove(TAG_IDKARYAWANPEKERJAAN).apply()
        pref.edit().remove(TAG_IDDIVISIPEKERJAAN).apply()
        pref.edit().remove(TAG_NAMAPEKERJAAN).apply()
        pref.edit().remove(TAG_DESKRIPSIPEKERJAAN).apply()
        pref.edit().remove(TAG_PROGRESSPEKERJAAN).apply()
        pref.edit().remove(TAG_STATUSPEKERJAAN).apply()
        pref.edit().remove(TAG_JPEKERJAAN).apply()
        //PRESENSI
        pref.edit().remove(TAG_IDPRESENSI).apply()
        pref.edit().remove(TAG_IDKARYAWANPRESENSI).apply()
        pref.edit().remove(TAG_IDDIVISIPRESENSI).apply()
        pref.edit().remove(TAG_KETERANGANPRESENSI).apply()
        pref.edit().remove(TAG_WAKTUMASUKPRESENSI).apply()
        pref.edit().remove(TAG_WAKTUKELUARPRESENSI).apply()
        pref.edit().remove(TAG_TANGGALPRESENSI).apply()
        pref.edit().remove(TAG_JPRESENSI).apply()
        pref.edit().remove(TAG_FILTERPRESENSI).apply()
        //JADWAL PRESENSI
        pref.edit().remove(TAG_IDJRESENSI).apply()
        pref.edit().remove(TAG_TANGGALJPRESENSI).apply()
        pref.edit().remove(TAG_WAKTUMASUKJPRESENSI).apply()
        pref.edit().remove(TAG_WAKTUKELUARJPRESENSI).apply()
        //USER KARYAWAN
        pref.edit().remove(TAG_IDPERUSAHAANUSER).apply()
        pref.edit().remove(TAG_PERUSAHAANUSER).apply()
        pref.edit().remove(TAG_IDUSER).apply()
        pref.edit().remove(TAG_NAMAUSER).apply()
        pref.edit().remove(TAG_TANGGALLAHIRUSER).apply()
        pref.edit().remove(TAG_JENISKELAMINUSER).apply()
        pref.edit().remove(TAG_ALAMATUSER).apply()
        pref.edit().remove(TAG_NOHPUSER).apply()
        pref.edit().remove(TAG_IDDIVISIUSER).apply()
        pref.edit().remove(TAG_EMAILUSER).apply()
        pref.edit().remove(TAG_PASSWORDUSER).apply()
        pref.edit().remove(TAG_IMGUSER).apply()
        //USER PEKERJAAN
        pref.edit().remove(TAG_IDPEKERJAANUSER).apply()
        pref.edit().remove(TAG_IDKARYAWANPEKERJAANUSER).apply()
        pref.edit().remove(TAG_IDDIVISIPEKERJAANUSER).apply()
        pref.edit().remove(TAG_NAMAPEKERJAANUSER).apply()
        pref.edit().remove(TAG_DESKRIPSIPEKERJAANUSER).apply()
        pref.edit().remove(TAG_PROGRESSPEKERJAANUSER).apply()
        pref.edit().remove(TAG_STATUSPEKERJAANUSER).apply()
        pref.edit().remove(TAG_IMGUSERPEKERJAAN).apply()

        pref.edit().remove(TAG_JBUTTONRESENSI).apply()
    }
}