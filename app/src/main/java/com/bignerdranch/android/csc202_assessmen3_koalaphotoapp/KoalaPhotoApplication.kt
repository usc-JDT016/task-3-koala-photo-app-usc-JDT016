package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.app.Application

class KoalaPhotoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoalaRepository.initialize(this)
    }
}