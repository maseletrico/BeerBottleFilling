package com.maseletrico.beerbottlefilling

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import java.util.ArrayList

val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
const val REQUEST_ENABLE_BT = 101

fun isBlueToothAdapter(): Boolean {

    bluetoothAdapter?.let {
        return true
    } ?: return false
}

fun Activity.startBlueTooth() {
    bluetoothAdapter?.let {
        if(!it.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}

fun findPairedDevices(): ArrayList<Pair<String,String>>{
    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
    val listOfDevices = ArrayList<Pair<String,String>>()
    pairedDevices?.forEach {device ->
        val devices = Pair(device.name,device.address)
        listOfDevices.add(devices)
    }
    return listOfDevices
}