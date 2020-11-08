package com.maseletrico.beerbottlefilling.viewModel

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.*
import com.bumptech.glide.Glide.init
import com.maseletrico.beerbottlefilling.constants.Const
import com.maseletrico.beerbottlefilling.extensions.REQUEST_ENABLE_BT
import com.maseletrico.beerbottlefilling.extensions.bluetoothAdapter
import com.maseletrico.beerbottlefilling.extensions.isBlueToothAdapter
import java.io.IOException
import java.util.*

class BlueToothViewModel(): BaseViewModel() {

    lateinit var  btSocket: BluetoothSocket
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    var mutableData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    //@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun blueToothConnect(address: String, uuid: UUID) {
        Log.i("beerLog", "init blueToothConnect")
        bluetoothAdapter?.cancelDiscovery()
        val device = bluetoothAdapter?.getRemoteDevice(address)
        device?.let {
            try {
                Log.i("beerLog", "init try")
                btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
                btSocket.connect()
                mutableData.value = true
                Log.i("beerLog", "finish try")
            }catch (e: IOException){
                mutableData.value = false
            }
        }
    }

    fun startBlueTooth(): MutableLiveData<Boolean> {
        var isBluToothAdapter = MutableLiveData<Boolean>()
        bluetoothAdapter?.let {
            isBluToothAdapter.value = !it.isEnabled
        }
        return isBluToothAdapter
    }
}