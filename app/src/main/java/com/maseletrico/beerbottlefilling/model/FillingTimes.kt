package com.maseletrico.beerbottlefilling.model

data class FillingTimes (
    val firebaseDocument: String?,
    val co2InPurge: String?,
    val co2OutPurge: String?,
    val co2Pressure: String?,
    val co2Residual: String?,
    val fillingTime: String?,
    val fillingVol: String?,
    val interval: String?
)