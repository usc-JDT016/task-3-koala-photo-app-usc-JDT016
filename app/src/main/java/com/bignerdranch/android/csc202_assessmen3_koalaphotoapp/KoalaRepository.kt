package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database.KoalaDatabase
import java.util.*

private const val DATABASE_NAME = "koala-database"
class KoalaRepository private constructor(context: Context) {

    private val database : KoalaDatabase = Room.databaseBuilder(
        context.applicationContext,
        KoalaDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val koalaDao = database.koalaDao()

    fun getKoalas(): LiveData<List<Koala>> = koalaDao.getKoalas()

    fun getkoala(id: UUID): LiveData<Koala?> = koalaDao.getKoala(id)



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