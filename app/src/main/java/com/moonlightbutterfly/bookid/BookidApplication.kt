package com.moonlightbutterfly.bookid

import android.app.Application
import com.moonlightbutterfly.bookid.di.AppComponent
import com.moonlightbutterfly.bookid.di.DaggerAppComponent

open class BookidApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}