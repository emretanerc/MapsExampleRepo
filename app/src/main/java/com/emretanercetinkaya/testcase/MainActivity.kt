package com.emretanercetinkaya.testcase

import android.content.Context
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emretanercetinkaya.testcase.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.preferencesDataStore
import com.emretanercetinkaya.testcase.model.CustomMarkerData
import com.emretanercetinkaya.testcase.model.toCustomMarkerData
import com.emretanercetinkaya.testcase.model.toJson
import com.google.android.gms.maps.model.Marker

open class MainActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStoreSettings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }


    suspend fun saveBookTrip(markerData: CustomMarkerData) {
        val jsonString = markerData.toJson()
        dataStore.edit { preferences ->
            preferences[Custom_Marker_Data] = jsonString
        }
    }

    suspend fun getBookedTrip(): CustomMarkerData? {
        val jsonString = dataStore.data.first()[Custom_Marker_Data]
        return jsonString?.toCustomMarkerData()
    }

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val Custom_Marker_Data = stringPreferencesKey("custom_marker_data")
    }


}