package com.bignerdranch.android.csc202_assessmen3_koalaphotoapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Koala(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var details: String = "",
                 var description: String = ""){


val photoFileName
    get() = "IMG_$id.jpg"
}