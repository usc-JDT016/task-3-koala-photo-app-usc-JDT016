package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.Koala
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.KoalaRepository
import java.io.File
import java.util.*

class KoalaDetailViewModel : ViewModel() {

    private val koalaRepository = KoalaRepository.get()
    private val koalaIdLiveData = MutableLiveData<UUID>()

    var koalaLiveData: LiveData<Koala?> =
        Transformations.switchMap(koalaIdLiveData) { koalaId ->
            koalaRepository.getKoala(koalaId)
        }

    fun loadKoala(koalaId: UUID) {
       koalaIdLiveData.value = koalaId
    }

    fun saveKoala(koala: Koala) {
        koalaRepository.updateKoala(koala)
    }


    fun getPhotoFile(koala: Koala): File {
        return koalaRepository.getPhotoFile(koala)
    }
}