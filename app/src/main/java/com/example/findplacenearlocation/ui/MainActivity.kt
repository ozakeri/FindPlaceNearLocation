package com.example.findplacenearlocation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.findplacenearlocation.R
import com.example.findplacenearlocation.db.VenueDatabase
import com.example.findplacenearlocation.repository.VenueRepository
import com.example.findplacenearlocation.ui.viewmodel.VenueViewModel
import com.example.findplacenearlocation.ui.viewmodel.VenueViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: VenueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = VenueRepository(VenueDatabase(this))
        val viewModelProviderFactory = VenueViewModelProviderFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(VenueViewModel::class.java)
    }
}