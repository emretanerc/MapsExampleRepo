package com.emretanercetinkaya.testcase.ui.triplistfragment

import com.emretanercetinkaya.testcase.ui.stationsmapfragment.StationsMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class TripListModule {
    @get:ContributesAndroidInjector
    abstract val tripListFragment: TripListFragment?
}