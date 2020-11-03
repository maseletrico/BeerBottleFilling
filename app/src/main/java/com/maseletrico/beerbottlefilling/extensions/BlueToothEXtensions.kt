package com.maseletrico.beerbottlefilling.extensions

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.maseletrico.beerbottlefilling.BlueToothInfo
import com.maseletrico.beerbottlefilling.userInterface.MainActivity
import java.io.IOException
import java.util.*



val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
const val REQUEST_ENABLE_BT = 101
private var blueToothInfo = mutableListOf<BlueToothInfo>()
private var dataIncomming = ""
lateinit var  btSocket: BluetoothSocket

fun isBlueToothAdapter(): Boolean {

    bluetoothAdapter?.let {
        return true
    } ?: return false
}

fun Activity.startBlueTooth() {
    bluetoothAdapter?.let {
        if (!it.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}

//fun Activity.blueToothConnect(address: String, uuid: UUID): Boolean {
//    val device = bluetoothAdapter?.getRemoteDevice(address)
//    //val btSocket: BluetoothSocket
//    bluetoothAdapter?.cancelDiscovery()
//    var socketCreateResponse = false
//
//
//    device?.let {
//        try {
//            btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
//            btSocket.connect()
//            socketCreateResponse = true
//
////            Log.d(MainActivity.TAG, "Socket creation success")
////            writeData(btSocket,"CIC")
////            var data = readData(btSocket)
////            Log.d(MainActivity.TAG, "Socket data $data")
////            if(data.contains("CIC_OK",true)){
////                Log.d(MainActivity.TAG, "Comando CIC Aceito!")
////            }
//        } catch (e: IOException) {
//
//            Log.d(MainActivity.TAG, "Socket creation failed")
//            Toast.makeText(applicationContext, "Socket creation failed", Toast.LENGTH_LONG).show()
//        }
//    }
//    return socketCreateResponse
//}

fun Activity.readData(btSocket: BluetoothSocket): String {

    val inputStream = btSocket.inputStream
    val mBuffer: ByteArray = ByteArray(256) // mmBuffer store for the stream
    var numBytes: Int = 0// bytes returned from read()
    //val handler = Handler()

    while (true) {
        numBytes = try {
            inputStream.read(mBuffer)
            //Toast.makeText(applicationContext, dataIncomming, Toast.LENGTH_LONG).show()
            Log.d("BLUETOOTH_TAG", "Reading buffer..")
        } catch (e: IOException) {
            //Toast.makeText(applicationContext, "inputStream error", Toast.LENGTH_LONG).show()
            Log.d("BLUETOOTH_TAG", "Input stream was disconnected", e)
            break
        }
        // Send the obtained bytes to the UI activity.
        //readMsg.sendToTarget()
        if(numBytes > 1) {
            inputStream.read(mBuffer)
            //val charset = Charsets.UTF_8
           // println(byteArray.contentToString()) // [72, 101, 108, 108, 111]
            //println(byteArray.toString(charset)) // Hello
        }
        break

    }
//    return handler.obtainMessage(
//        MESSAGE_READ, numBytes, -1,
//        mBuffer
//    ).toString()
    return mBuffer.toString(Charsets.UTF_8)
}

fun Activity.writeData(btSocket: BluetoothSocket, data: String) {
    var outStream = btSocket.outputStream
    try {
        outStream = btSocket.outputStream
    } catch (e: IOException) {
        Log.d("BLUETOOTH_TAG", "Bug BEFORE Sending stuff", e)
    }
    val msgBuffer = data.toByteArray()

    try {
        outStream.write(msgBuffer)
        //create line feed character
        val newLine = byteArrayOf(13, 10)
        outStream.write(newLine)
    } catch (e: IOException) {
        Log.d("BLUETOOTH_TAG", "Bug while sending stuff", e)
    }

}

fun findPairedDevices(): MutableList<BlueToothInfo> {
    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

    pairedDevices?.forEach { device ->
        val btList = BlueToothInfo(device.name, device.address)
        blueToothInfo.add(btList)
    }
    return blueToothInfo
}