package com.maseletrico.beerbottlefilling.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.maseletrico.beerbottlefilling.model.FillingTimes

class Repository {

    fun getFillingTimes(): LiveData<MutableList<FillingTimes>> {
        val mutableData = MutableLiveData<MutableList<FillingTimes>>()

        FirebaseFirestore.getInstance().collection("bottleFillerSetups").orderBy("fillingVol").get()
            .addOnSuccessListener { result ->

                val listData = mutableListOf<FillingTimes>()

                for (document in result) {
                    val co2InPurge = document.getString("co2InPurge")
                    val co2OutPurge = document.getString("co2OutPurge")
                    val co2Pressure = document.getString("co2Pressure")
                    val co2Residual = document.getString("co2Residual")
                    val fillingTime = document.getString("fillingTime")
                    val fillingVol = document.getString("fillingVol")
                    val interval = document.getString("interval")

                    val bottleFillerTimes = FillingTimes(
                        document.id,
                        co2InPurge,
                        co2OutPurge,
                        co2Pressure,
                        co2Residual,
                        fillingTime,
                        fillingVol,
                        interval
                    )
                    listData.add(bottleFillerTimes)
                }
                mutableData.value = listData
            }
        return mutableData
    }
}