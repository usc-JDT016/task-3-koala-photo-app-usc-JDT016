package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.Koala

@Database(entities = [ Koala::class ], version=1)
@TypeConverters(KoalaTypeConverters::class)
abstract class KoalaDatabase : RoomDatabase() {

    abstract fun koalaDao(): KoalaDao
}
