package com.g7.soft.pureDot.ui.viewModel

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g7.soft.pureDot.listener.LocationListener

class LocationVM: ViewModel() {

    var location: MutableLiveData<Location>? = MutableLiveData<Location>()
    var locationRepository: LocationListener? = null

    fun setLocationRepository(context: Context) {
        locationRepository = LocationListener.getInstance(context)
    }

    fun enableLocationServices(){
        locationRepository?.let {
            it.startService()
        }
    }
}