package com.example.marsphotos.data

import android.content.Context
import com.example.marsphotos.network.SICENETwService2
import com.example.marsphotos.network.retrofit22
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object ServiceLocator {
    private const val BASE_URL = "https://sicenet.surguanajuato.tecnm.mx"


    fun createService(context: Context): SICENETwService2 {
        val client = OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor(context))
            .addInterceptor(ReceivedCookiesInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        return retrofit.create(SICENETwService2::class.java)
    }







    /*
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    */

   // var service: AccesoLoginService = retrofit.create(AccesoLoginService::class.java)



}
