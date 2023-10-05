package com.emretanercetinkaya.testcase.model

import com.google.android.gms.maps.model.LatLng


data class StationsResponseModel (
    val center_coordinates: String,
    val id: Long,
    val name: String,
    val trips: List<Trip>,
    val tripsCount: Long
) {
     fun getLatLng(): LatLng {
        val parts = center_coordinates.split(",")
        val latitude = parts[0].toDouble()
        val longitude = parts[1].toDouble()
        return LatLng(latitude, longitude)
    }
}

data class Trip (
    val busName: String,
    val id: Long,
    val time: String
)
