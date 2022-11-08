package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import androidx.lifecycle.ViewModel

class KoalaListViewModel : ViewModel() {

    private val koalaRepository = KoalaRepository.get()
    val koalaListLiveData = koalaRepository.getKoalas()

    fun addKoala(koala: Koala) {
        koalaRepository.addKoala(koala)
    }


}

