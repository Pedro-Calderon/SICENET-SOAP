package com.example.marsphotos.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val URL = "https://sicenet.surguanajuato.tecnm.mx"

interface SICENETwService2 {
    @Headers(
        "Content-Type: text/xml; charset=utf-8",
        "SOAPAction: \"http://tempuri.org/accesoLogin\"\n",
        "Host: sicenet.surguanajuato.tecnm.mx"
      // "Cookie: .ASPXANONYMOUS=JZW7YLSW2gEkAAAAOGU0M2Q5YTYtNDZjNi00N2FkLTkwZjYtZTViMjFjNDM2MDllXCwK5kgKthEYMv4Vtrlk5RNDPaY1;"

    )
    @POST("/ws/wsalumnos.asmx")
    suspend fun acceso(@Body requestBody: RequestBody): Response<ResponseBody>
}


private const val matricula = "s18120164"
private const val contrasenia = "3Yz$-a4G"

val requestBodyXmlText ="<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "  <soap:Body>\n" +
        "    <accesoLogin xmlns=\"http://tempuri.org/\">\n" +
        "      <strMatricula>s18120164</strMatricula>\n" +
        "      <strContrasenia>$contrasenia</strContrasenia>\n" +
        "      <tipoUsuario>ALUMNO</tipoUsuario>\n" +
        "    </accesoLogin>\n" +
        "  </soap:Body>\n" +
        "</soap:Envelope>"

val requestBody= requestBodyXmlText.toRequestBody(
    "text/xml".toMediaTypeOrNull()
)
val retrofit22 = Retrofit.Builder()
    .baseUrl(URL)
    .addConverterFactory(SimpleXmlConverterFactory.create())
    .build()

var service4:SICENETwService2= retrofit22.create(
    SICENETwService2::class.java)

suspend fun main() {
    try {
        val response = service4.acceso(requestBody)
        if (response.isSuccessful) {
            val responseBodyString = response.body()?.string()
            //val responseBodyString = response.raw()
            println("Response: ${service4.acceso(requestBody)}")
            println("Response: $responseBodyString")
        } else {
            println("Error: ${response.errorBody()?.string()}")
        }
    } catch (e: Exception) {
        println("Exception: $e")
    }
}