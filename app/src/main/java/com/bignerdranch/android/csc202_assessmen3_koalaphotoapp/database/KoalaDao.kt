package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bignerdranch.android.csc202_assessmen3_koalaphotoapp.Koala
import java.util.*

@Dao
interface KoalaDao {

    @Query("SELECT * FROM koala")
    fun getKoalas(): LiveData<List<Koala>>

    @Query("SELECT * FROM koala WHERE id=(:id)")
    fun getKoala(id: UUID): LiveData<Koala?>
}
// might change to photo where koala is mentioned for easier clarification