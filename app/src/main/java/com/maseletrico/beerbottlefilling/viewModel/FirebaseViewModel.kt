package com.maseletrico.beerbottlefilling.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maseletrico.beerbottlefilling.data.network.Repository
import com.maseletrico.beerbottlefilling.model.FillingTimes

class FirebaseViewModel : BaseViewModel(){
    private val repository = Repository()

    fun fetchFillingTimes(): LiveData<MutableList<FillingTimes>> {
        val mutableData = MutableLiveData<MutableList<FillingTimes>>()
        repository.getFillingTimes().observeForever { fillerTimes ->
            mutableData.value = fillerTimes
        }
        return mutableData
    }
}