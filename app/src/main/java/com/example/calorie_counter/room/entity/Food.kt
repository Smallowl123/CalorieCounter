package com.example.calorie_counter.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    @ColumnInfo(name = "protein") val protein: Float,
    @ColumnInfo(name = "fat") val fat: Float,
    @ColumnInfo(name = "carbon") val carboh: Float,
    @ColumnInfo(name = "calories") val calories: Float,
    val lastInIndex: Int,
    val userMade: Boolean
)