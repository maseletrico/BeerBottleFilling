package com.maseletrico.beerbottlefilling.userInterface

import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maseletrico.beerbottlefilling.*
import com.maseletrico.beerbottlefilling.adapter.PairedAdapter
import com.maseletrico.beerbottlefilling.extensions.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), PairedAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var adapter: PairedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PairedAdapter(this,this)

        rvPairedList.layoutManager = LinearLayoutManager(this)
        rvPairedList.adapter = adapter

        if(isBlueToothAdapter()){
            startBlueTooth()

            adapter.setListData(findPairedDevices())
            adapter.notifyDataSetChanged()

            //AcceptThread().run()
        }else{
            Toast.makeText(this,getString(R.string.deviceSupportBlueToothMessage),Toast.LENGTH_LONG).show()
        }
    }



//    private inner class AcceptThread : Thread() {
//        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
//            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("Cherokee", MY_UUID)
//        }
//
//        override fun run() {
//            // Keep listening until exception occurs or a socket is returned.
//            var shouldLoop = true
//            while (shouldLoop) {
//                val socket: BluetoothSocket? = try {
//                    mmServerSocket?.accept()
//                } catch (e: IOException) {
//                    Log.e(TAG, "Socket's accept() method failed", e)
//                    shouldLoop = false
//                    null
//                }
//                socket?.also {
//                    //manageMyConnectedSocket(it)
//                    mmServerSocket?.close()
//                    shouldLoop = false
//                }
//            }
//        }
//
//        // Closes the connect socket and causes the thread to finish.
//        fun cancel() {
//            try {
//                mmServerSocket?.close()
//            } catch (e: IOException) {
//                Log.e(TAG, "Could not close the connect socket", e)
//            }
//        }
//    }

    override fun onItemClick(bluToothName: String, blueToothAddress: String) {
        super.onItemClick(bluToothName,blueToothAddress)
        val intent = Intent(this, BottleFillerControlActivity::class.java).apply {
            putExtra("BLUETOOTH_ADDRESS", blueToothAddress)
            putExtra("BLUETOOTH_NAME", bluToothName)

        }
        startActivity(intent)
        //blueToothConnect(blueToothAddress, MY_UUID)
    }

    companion object{

        const val TAG = "BLUETOOTH_TAG"
        // SPP UUID service - this should work for most devices
         val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}