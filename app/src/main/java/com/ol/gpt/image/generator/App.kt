package com.ol.gpt.image.generator

import android.app.Application
import com.ol.gpt.image.generator.di.coroutineScopeModule
import com.ol.gpt.image.generator.util.FileUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import java.io.File

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        intKoin()
        ensureImagesPath()
        Timber.plant(Timber.DebugTree())
    }

    private fun intKoin() {
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    coroutineScopeModule,
                )
            )
        }
    }

    private fun ensureImagesPath() {
        val imagesDir = File(this.filesDir.absolutePath + FileUtils.IMAGES_DIRECTORY_PATH_SUFFIX)
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
            Timber.i("created ${imagesDir.absolutePath}")
        }
    }
}