package com.example.findplacenearlocation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.findplacenearlocation.repository.VenueRepository

class VenueViewModelProviderFactory(val application: Application,val repository: VenueRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VenueViewModel(application,repository) as T
    }
}