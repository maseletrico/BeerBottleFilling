package com.maseletrico.beerbottlefilling.viewModel

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maseletrico.beerbottlefilling.extensions.bluetoothAdapter
import java.io.IOException
import java.util.*

class BlueToothViewModel(): BaseViewModel() {

    lateinit var  btSocket: BluetoothSocket

    fun blueToothConnect(address: String, uuid: UUID): MutableLiveData<Boolean> {

        var mutableData = MutableLiveData<Boolean>()

        bluetoothAdapter?.cancelDiscovery()
        val device = bluetoothAdapter?.getRemoteDevice(address)
        device?.let {
            try {
                btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
                btSocket.connect()
                mutableData.value = true
            }catch (e: IOException){
                mutableData.value = false
            }
        }
        return mutableData
    }
}