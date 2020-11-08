package com.maseletrico.beerbottlefilling.userInterface

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.maseletrico.beerbottlefilling.BlueToothObserver
import com.maseletrico.beerbottlefilling.R
import com.maseletrico.beerbottlefilling.viewModel.FirebaseViewModel
import kotlinx.android.synthetic.main.activity_bottle_filler_control.*

class BottleFillerControlActivity : AppCompatActivity() {

    lateinit var blueToothAddress: String
    lateinit var blueToothName: String
    private val firebaseViewModel by lazy { ViewModelProvider(this).get(FirebaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottle_filler_control)

        ivBlueTooth.visibility = View.VISIBLE
        pbBlueToothLoading.visibility = View.VISIBLE
        tvBlueToothName.visibility = View.GONE

        MobileAds.initialize(this) {}
        Log.i("beerLog", "onCreate()")

        ///mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        intent.getStringExtra("BLUETOOTH_NAME")?.let {
            blueToothName = it
        }
        intent.getStringExtra("BLUETOOTH_ADDRESS")?.let {
            blueToothAddress = it
        }
        Log.i("beerLog", "After intent extras")
        swFillerByTime.setOnClickListener {
            if (swFillerByTime.isChecked) {
                swTimeSetup.visibility = View.VISIBLE
                tilFillerTime.visibility = View.VISIBLE
            } else {
                swTimeSetup.visibility = View.GONE
                tilFillerTime.visibility = View.GONE
            }
        }

        lifecycle.addObserver(BlueToothObserver(this))

        observeData()
        Log.i("beerLog", "return observe data")
    }

    override fun onResume() {
        super.onResume()
        Log.i("beerLog", "btActivity on resume")
    }

    private fun observeData() {
        firebaseViewModel.fetchFillingTimes().observe(this, Observer {
            var bdTitleMutableList: MutableList<String> = ArrayList()

            it.forEachIndexed { index, fillingTimes ->
                bdTitleMutableList.add(index, fillingTimes.firebaseDocument!!)
            }

            val adapter =
                ArrayAdapter(applicationContext, R.layout.bottle_list_item, bdTitleMutableList)
            (tilBottleType.editText as? AutoCompleteTextView)?.setAdapter(adapter)

            actvBeerBottleType.setOnItemClickListener { _, _, itemIndex, _ ->
                adapter.getItem(itemIndex)?.let { clickedItem ->

                    it.forEach { itemFromRepository ->
                        if (itemFromRepository.firebaseDocument == clickedItem) {
                            tvTVolumeValue.text = it[itemIndex].fillingVol
                            tfCO2InPurge.setText(it[itemIndex].co2InPurge)
                            tfOutPurge.setText(it[itemIndex].co2OutPurge)
                            tfPressureTime.setText(it[itemIndex].co2Pressure)
                            tfCO2_ResidualTime.setText(it[itemIndex].co2Residual)
                            tfFillerTime.setText(it[itemIndex].fillingTime)
                        }
                    }
                }
            }
        })
    }

    private fun blueToothConnectedActions(blueToothConnected: Boolean) {
        if (blueToothConnected) {
            blueToothName?.let {
                //ivBlueTooth.visibility = View.VISIBLE
                runOnUiThread(Runnable {
                    pbBlueToothLoading.visibility = View.GONE
                    tvBlueToothName.text = blueToothName
                    tvBlueToothName.visibility = View.VISIBLE
                })
            }
        } else {
            //ivBlueTooth.visibility = View.VISIBLE
            runOnUiThread(Runnable {
                pbBlueToothLoading.visibility = View.VISIBLE
                tvBlueToothName.visibility = View.GONE
            })
        }
    }

    fun blueToothConnected(btConnected: Boolean) {
        blueToothConnectedActions(btConnected)
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO turn off socket
    }
}