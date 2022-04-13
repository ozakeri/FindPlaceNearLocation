package com.example.findplacenearlocation.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.findplacenearlocation.VenueApplication
import com.example.findplacenearlocation.model.ResponseBean
import com.example.findplacenearlocation.model.Venue
import com.example.findplacenearlocation.repository.VenueRepository
import com.example.findplacenearlocation.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class VenueViewModel(application: Application, val repository: VenueRepository) :
    AndroidViewModel(application) {

    val venueList: MutableLiveData<Resource<ResponseBean>> = MutableLiveData()
    var pageNumber = 20
    var responseBeanVenue: ResponseBean? = null

    fun getVenueList(lng: String) = viewModelScope.launch {
        safeVenueCall(lng)
    }

    fun getVenueDetail(venueId: String) = viewModelScope.launch {
        safeVenueDetailCall(venueId)
    }


    suspend fun safeVenueCall(lng: String) {
        venueList.postValue(Resource.Loading())
        //try {
            if (hasInternetConnection()) {
                val response = repository.getNearPlaces(lng , pageNumber)
                venueList.postValue(handleSafeVenueCall(response))
            } else {
                venueList.postValue(Resource.Error("No internet connection"))
            }
       /* } catch (t: Throwable) {
            when (t) {
                is IOException -> venueList.postValue(Resource.Error("Network Failure"))
                else -> venueList.postValue(Resource.Error("Conversion Error"))
            }
        }*/
    }

    suspend fun safeVenueDetailCall(venueId: String) {
        venueList.postValue(Resource.Loading())
        //try {
            if (hasInternetConnection()) {
                val response = repository.getDetailPlace(venueId)
                venueList.postValue(handleSafeVenueDetailCall(response))
            } else {
                venueList.postValue(Resource.Error("No internet connection"))
            }
       /* } catch (t: Throwable) {
            when (t) {
                is IOException -> venueList.postValue(Resource.Error("Network Failure"))
                else -> venueList.postValue(Resource.Error("Conversion Error"))
            }
        }*/

    }

    fun handleSafeVenueCall(response: Response<ResponseBean>): Resource<ResponseBean> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                pageNumber+=20;
                if (responseBeanVenue == null) {
                    responseBeanVenue = result
                } else {

                    val oldData = responseBeanVenue?.response?.venues
                    val newData = result.response.venues
                    oldData?.addAll(newData)
                }

                return Resource.Success(responseBeanVenue ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    fun handleSafeVenueDetailCall(response: Response<ResponseBean>): Resource<ResponseBean> {
        if (response.isSuccessful) {
            response.body()?.let { responseDetail ->
                return Resource.Success(responseDetail)
            }
        }

        return Resource.Error(response.message())
    }

    fun saveVenue(venue: Venue) = viewModelScope.launch {
        repository.upsert(venue)
    }

    fun getAllVenueFromDb() = repository.getSaveVenus()

    fun deleteVenue(venue: Venue) = viewModelScope.launch {
        repository.delete(venue)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<VenueApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}