package org.d3ifcool.jobmonitoring.api

import org.d3ifcool.jobmonitoring.data.KaryawanModel
import org.d3ifcool.jobmonitoring.data.ListDivisiModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndpoint {

    @GET("Karyawan/dataKaryawan.php")
    fun dataKaryawan() : Call<KaryawanModel>

    @GET("Karyawan/data.php")
    fun listDivisi() : Call<ListDivisiModel>

    @FormUrlEncoded
    @POST("Karyawan/createKaryawan.php")
    fun createKaryawan(
        @Field("nama_karyawan") nama_karyawan   : String,
        @Field("tanggal_lahir") tanggal_lahir   : String,
        @Field("jenis_kelamin") jenis_kelamin   : String,
        @Field("alamat")        alamat          : String,
        @Field("no_hp")         no_hp           : String,
        @Field("divisi")        divisi          : String,
        @Field("email")         email           : String,
        @Field("password")      password        : String,
        @Field("perusahaan")    perusahaan      : String
    ) : Call<KaryawanModel>

    @FormUrlEncoded
    @POST("Karyawan/deleteKaryawan.php")
    fun deleteKaryawan(
        @Field("id") id: Int
    ) : Call<KaryawanModel>

    @FormUrlEncoded
    @POST("Karyawan/updateKaryawan.php")
    fun updateKaryawan(
        @Field("id") id: Int,
        @Field("nama_karyawan") nama_karyawan   : String,
        @Field("tanggal_lahir") tanggal_lahir   : String,
        @Field("jenis_kelamin") jenis_kelamin   : String,
        @Field("alamat")        alamat          : String,
        @Field("no_hp")         no_hp           : String,
        @Field("divisi")        divisi          : String,
        @Field("email")         email           : String
    ) : Call<KaryawanModel>
}