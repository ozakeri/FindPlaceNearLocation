package com.example.findplacenearlocation.ui.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.findplacenearlocation.ui.viewmodel.VenueViewModel

class DetailFragment : Fragment(){

    lateinit var viewModel: VenueViewModel
    val args : DetailFragmentArgs by navArgs()

}