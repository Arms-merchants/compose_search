package com.example.composesearchexample

import android.app.Application

/**
 * Created by heyueyang on 2021/6/7
 */
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.appContext = this
    }
}

