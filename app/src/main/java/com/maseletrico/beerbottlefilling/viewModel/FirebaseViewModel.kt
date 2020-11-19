package com.maseletrico.beerbottlefilling.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.maseletrico.beerbottlefilling.constants.Const
import com.maseletrico.beerbottlefilling.data.network.Repository
import com.maseletrico.beerbottlefilling.model.FillingTimes

class FirebaseViewModel : BaseViewModel() {
    private val repository = Repository()
    var updateSuccess : MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun fetchFillingTimes(): LiveData<MutableList<FillingTimes>> {
        val mutableData = MutableLiveData<MutableList<FillingTimes>>()
        repository.getFillingTimes().observeForever { fillerTimes ->
            mutableData.value = fillerTimes
        }
        return mutableData
    }

    fun saveFillingTimes(
        co2InPurge: String,
        co2Pressure: String,
        co2Residual: String,
        co2OutPurge: String,
        fillingTime: String,
        fillingVolume: String,
        interval: String,
        document: String
    ): MutableLiveData<Boolean> {
        // Add a new document with a generated ID
        val firebase =
            FirebaseFirestore.getInstance()//.collection("bottleFillerSetups").orderBy("fillingVolume").get()

        //DocumentReference ref = db.collection("user_details").document();
        //String id = ref.getId();

//        val fillingTimes = hashMapOf(
//            "co2InPurge" to co2InPurge,
//            "co2Pressure" to co2Pressure,
//            "co2Residual" to co2Residual,
//            "co2OutPurge" to co2OutPurge,
//            "fillingTime" to fillingTime,
//            "fillingVolume" to fillingVolume,
//            "interval" to interval,
//            "id" to document
//        )

        val fillingTimesUpdate = mutableMapOf(
            "co2InPurge" to co2InPurge,
            "co2Pressure" to co2Pressure,
            "co2Residual" to co2Residual,
            "co2OutPurge" to co2OutPurge,
            "fillingTime" to fillingTime,
            "fillingVolume" to fillingVolume,
            "interval" to interval,
        )

        firebase.collection(Const.fireStoreCollection)
            .document(document)
            .update(fillingTimesUpdate as Map<String, Any>)
            .addOnSuccessListener {
                Log.d("beerLog", "DocumentSnapshot success updated")
                updateSuccess.value = true

            }
            .addOnFailureListener { e ->
                Log.w("beerLog", "Error updating document", e)
                updateSuccess.value = false
            }
        return updateSuccess
    }
}