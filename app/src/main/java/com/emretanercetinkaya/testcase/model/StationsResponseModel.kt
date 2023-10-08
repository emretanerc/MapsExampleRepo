package com.emretanercetinkaya.testcase.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize


@Parcelize
data class StationsResponseModel (
    val center_coordinates: String,
    val id: Long,
    val name: String,
    val trips: List<Trip>,
    val trips_count: Long
) : Parcelable {
     fun getLatLng(): LatLng {
        val parts = center_coordinates.split(",")
        val latitude = parts[0].toDouble()
        val longitude = parts[1].toDouble()
        return LatLng(latitude, longitude)
    }
}

@Parcelize
data class Trip (
    val bus_name: String,
    val id: Long,
    val time: String
) : Parcelable
