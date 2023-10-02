package com.emretanercetinkaya.testcase.di

import com.emretanercetinkaya.testcase.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity?

}