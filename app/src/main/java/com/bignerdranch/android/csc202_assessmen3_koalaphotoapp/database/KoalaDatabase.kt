package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.Koala

@Database(entities = [ Koala::class ], version=2)
@TypeConverters(KoalaTypeConverters::class)
abstract class KoalaDatabase : RoomDatabase() {

    abstract fun koalaDao(): KoalaDao
}
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Koala ADD COLUMN Koala NOT NULL DEFAULT ''"
        )
    }
}