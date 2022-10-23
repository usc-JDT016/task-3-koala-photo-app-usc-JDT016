package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database.KoalaDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "koala-database"
class KoalaRepository private constructor(context: Context) {

    private val database : KoalaDatabase = Room.databaseBuilder(
        context.applicationContext,
        KoalaDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val koalaDao = database.koalaDao()
    private val executor = Executors.newSingleThreadExecutor()



    fun getKoalas(): LiveData<List<Koala>> = koalaDao.getKoalas()

    fun getKoala(id: UUID): LiveData<Koala?> = koalaDao.getKoala(id)

    fun updateKoala(koala: Koala) {
        executor.execute {
            koalaDao.updateKoala(koala)
        }
    }

    fun addKoala(koala: Koala) {
        executor.execute {
            koalaDao.addKoala(koala)
        }
    }


    companion object {
        private var INSTANCE: KoalaRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = KoalaRepository(context)
            }
        }

        fun get(): KoalaRepository {
            return INSTANCE ?:
            throw IllegalStateException("KoalaRepository must be initialized")
        }
    }
}