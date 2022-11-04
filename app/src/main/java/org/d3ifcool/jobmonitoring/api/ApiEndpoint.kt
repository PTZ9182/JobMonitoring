package org.d3ifcool.jobmonitoring.api

import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.data.SubmitDivisiModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndpoint {

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
}