package com.example.marsphotos.data

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST



interface AccesoLoginService {
    @Headers(
        "Content-Type: text/xml",
        "Accept-Charset: utf-8",
        "SOAPAction: http://tempuri.org/accesoLogin"
        //"Cookie: .ASPXANONYMOUS=JZW7YLSW2gEkAAAAOGU0M2Q5YTYtNDZjNi00N2FkLTkwZjYtZTViMjFjNDM2MDllXCwK5kgKthEYMv4Vtrlk5RNDPaY1;"

    )
    @POST("/ws/wsalumnos.asmx")
    suspend fun realizarAccesoLogin(@Body requestBody: RequestBody): Response<ResponseBody>
}

