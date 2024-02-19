
package com.example.marsphotos.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marsphotos.MarsPhotosApplication
import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.data.ServiceLocator
import com.example.marsphotos.data.ServiceLocator.service
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.network.SICENETwService2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface MarsUiState {
    data class Success(val photos: String) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

class MarsViewModel(private val marsPhotosRepository: MarsPhotosRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

        fun realizarAccesoLogin() {
            val requestBody = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <accesoLogin xmlns=\"http://tempuri.org/\">\n" +
                    "      <strMatricula>s18120164</strMatricula>\n" +
                    "      <strContrasenia>3Yz$-a4G</strContrasenia>\n" +
                    "      <tipoUsuario>ALUMNO</tipoUsuario>\n" +
                    "    </accesoLogin>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>"
            val requestBody2= requestBody.toRequestBody(
                "text/xml".toMediaTypeOrNull())
            viewModelScope.launch(Dispatchers.IO) {
                val response = //service.realizarAccesoLogin(requestBody2)
                if (response.isSuccessful) {
                    val responseBodyString = response.body()?.string()
                    //val responseBodyString = response.raw()
                    Log.d("LOGIN", "Response: $responseBodyString")
                } else {
                    Log.d("Error: ", "ERROR: ${response.errorBody().toString()}")
                }
            }
        }

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
        realizarAccesoLogin()

    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMarsPhotos() {
        viewModelScope.launch {
            marsUiState = MarsUiState.Loading
            marsUiState = try {
                val listResult = marsPhotosRepository.getMarsPhotos()
                MarsUiState.Success(
                    "Success: ${listResult.size} Mars photos retrieved"
                )
            } catch (e: IOException) {
                MarsUiState.Error
            } catch (e: HttpException) {
                MarsUiState.Error
            }
        }
    }

    /**
     * Factory for [MarsViewModel] that takes [MarsPhotosRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
                val marsPhotosRepository = application.container.marsPhotosRepository
                MarsViewModel(marsPhotosRepository = marsPhotosRepository)
            }
        }
    }
}
