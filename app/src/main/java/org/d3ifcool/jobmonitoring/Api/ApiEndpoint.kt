package org.d3ifcool.jobmonitoring.Api

import org.d3ifcool.jobmonitoring.data.DivisiModel
import org.d3ifcool.jobmonitoring.data.TambahDivisiModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndpoint {

    @GET("data.php")
    fun data() : Call<DivisiModel>

    @FormUrlEncoded
    @POST("create.php")
    fun create(
        @Field("divisi") divisi: String
    ) : Call<TambahDivisiModel>
}