package com.test.opentelemetryrandomsupplier.application

import android.app.Application
import android.util.Log
import com.test.opentelemetryrandomsupplier.configuration.OpenTelemetryConfigurable
import kotlin.system.measureTimeMillis

/**
 * Main Application class that extends from Application to execute the start method only once.
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        measureTimeMillis {
            OpenTelemetryConfigurable().configure()
        }.also { time ->
            Log.d("## MAIN APPLICATION ##", "Time elapsed: $time")
        }
    }
}
