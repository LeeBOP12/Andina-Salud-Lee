package pe.edu.upeu

import android.app.Application
import org.koin.android.ext.koin.androidContext
import pe.edu.upeu.di.initKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MainApplication)
        }
    }
}
