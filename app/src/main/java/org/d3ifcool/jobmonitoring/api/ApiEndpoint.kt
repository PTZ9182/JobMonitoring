package org.d3ifcool.jobmonitoring.api

import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.data.ListKaryawanPekerjaanModel
import org.d3ifcool.jobmonitoring.data.PekerjaanModel
import org.d3ifcool.jobmonitoring.data.SubmitDivisiModel
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
}