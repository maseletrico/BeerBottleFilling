package com.maseletrico.beerbottlefilling

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.maseletrico.beerbottlefilling.constants.Const
import com.maseletrico.beerbottlefilling.userInterface.BottleFillerControlActivity
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread

class BlueToothObserver(private val bottleFillerControlActivity: BottleFillerControlActivity) :
    LifecycleObserver {

    private lateinit var blueToothAddress: String

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var btSocket: BluetoothSocket

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun blueToothConnect() {
        Log.i("beerLog", "On resume Observer")
        blueToothAddress = bottleFillerControlActivity.blueToothAddress
        blueToothConnectObserver(blueToothAddress, Const.MY_UUID)
    }

    private fun blueToothConnectObserver(address: String, uuid: UUID) {
        Log.i("beerLog", "init blueToothConnect")
        bluetoothAdapter?.cancelDiscovery()
        val device = bluetoothAdapter?.getRemoteDevice(address)
        device?.let {
            thread {
                try {
                    Log.i("beerLog", "init try Observer")
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
                    btSocket.connect()
                    Log.i("beerLog", "finish try Observer")
                    bottleFillerControlActivity.blueToothConnected(true, btSocket)
                } catch (e: IOException) {
                    bottleFillerControlActivity.blueToothConnected(false, btSocket)
                    Log.i("beerLog", "exception try Observer")
                }
            }
        }
    }

}