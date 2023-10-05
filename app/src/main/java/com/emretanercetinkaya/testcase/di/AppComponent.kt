package com.emretanercetinkaya.testcase.di

import com.emretanercetinkaya.testcase.ui.stationsmapfragment.StationsMapModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ActivityBindingModule::class,StationsMapModule::class])
interface AppComponent : AndroidInjector<MainApplication?> {

    override fun inject(application: MainApplication?)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MainApplication): Builder?
        fun build(): AppComponent?
    }
}