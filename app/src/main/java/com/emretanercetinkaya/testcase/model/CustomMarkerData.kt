package com.emretanercetinkaya.testcase.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomMarkerData(val title: String, val position:LatLng, val stationId:Long,val trips: List<Trip>,var isBooked:Boolean) : Parcelable

fun CustomMarkerData.toJson(): String {
    return Gson().toJson(this)
}

fun String.toCustomMarkerData(): CustomMarkerData {
    return Gson().fromJson(this, CustomMarkerData::class.java)
}