package com.maseletrico.beerbottlefilling.extensions

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import com.maseletrico.beerbottlefilling.BlueToothInfo
import kotlinx.android.synthetic.main.activity_bottle_filler_control.*
import java.io.IOException


val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
const val REQUEST_ENABLE_BT = 101
private var blueToothInfo = mutableListOf<BlueToothInfo>()
private var dataIncomming = ""


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

fun Activity.readData(btSocket: BluetoothSocket): String {

    val inputStream = btSocket.inputStream
    val mBuffer: ByteArray = ByteArray(256) // mmBuffer store for the stream
    var numBytes: Int = 0// bytes returned from read()

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
        if (numBytes > 1) {
            inputStream.read(mBuffer)
        }
        break
    }
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

fun Activity.formatCommand(): String{

    val volume = tfVolume.text
    val co2InPurge = tfCO2InPurge.text
    val outPurge = tfOutPurge.text
    val pressureTime = tfPressureTime.text
    val co2ResidualTime = tfCO2_ResidualTime.text
    val fillerTime = tfFillerTime.text
    val interval = tfInterval.text

    return "CIC,$volume,$co2InPurge,$outPurge,$pressureTime,$co2ResidualTime,$fillerTime,$interval"
}