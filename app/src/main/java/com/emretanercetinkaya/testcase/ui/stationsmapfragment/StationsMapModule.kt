package com.emretanercetinkaya.testcase.ui.stationsmapfragment

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class StationsMapModule {
    @get:ContributesAndroidInjector
    abstract val mapFragment: StationsMapFragment?
}