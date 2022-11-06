package org.d3ifcool.jobmonitoring.api

import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.data.ListKaryawanPekerjaanModel
import org.d3ifcool.jobmonitoring.data.PekerjaanModel
import org.d3ifcool.jobmonitoring.data.SubmitDivisiModel
import org.d3ifcool.jobmonitoring.data.KaryawanModel
import org.d3ifcool.jobmonitoring.data.ListDivisiModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndpoint {

    //Divisi
    @GET("Divisi/data.php")
    fun data() : Call<DivisiModel>

    @FormUrlEncoded
    @POST("Divisi/create.php")
    fun create(
        @Field("divisi") divisi: String
    ) : Call<SubmitDivisiModel>

    @FormUrlEncoded
    @POST("Divisi/update.php")
    fun update(
        @Field("id") id: String,
        @Field("divisi") divisi: String
    ) : Call<SubmitDivisiModel>

    @FormUrlEncoded
    @POST("Divisi/delete.php")
    fun delete(
        @Field("id") id: String
    ) : Call<SubmitDivisiModel>

    //Pekerjaan
    @GET("Pekerjaan/dataPekerjaan.php")
    fun dataPekerjaan() : Call<PekerjaanModel>

    @GET("Pekerjaan/listKaryawan.php")
    fun listKaryawan() : Call<ListKaryawanPekerjaanModel>

    @FormUrlEncoded
    @POST("Pekerjaan/createPekerjaan.php")
    fun createPekerjaan(
        @Field("nama_pekerjaan") nama_pekerjaan: String,
        @Field("deskripsi") deskripsi: String,
        @Field("perusahaan") perusahaan: String,
        @Field("karyawan") karyawan: String
    ) : Call<PekerjaanModel>

    @FormUrlEncoded
    @POST("Pekerjaan/updatePekerjaan.php")
    fun updatePekerjaan(
        @Field("id") id: Int,
        @Field("nama_pekerjaan") nama_pekerjaan: String,
        @Field("deskripsi") deskripsi: String,
        @Field("karyawan") karyawan: String
    ) : Call<PekerjaanModel>

    @FormUrlEncoded
    @POST("Pekerjaan/deletePekerjaan.php")
    fun deletePekerjaan(
        @Field("id") id: Int
    ) : Call<PekerjaanModel>

    //Karyawan
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