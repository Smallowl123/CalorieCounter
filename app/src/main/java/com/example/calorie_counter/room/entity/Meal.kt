package com.example.calorie_counter.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_table")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val weight: Int,
    @ColumnInfo(name = "protein") val protein: Float,
    @ColumnInfo(name = "fat") val fat: Float,
    @ColumnInfo(name = "carboh") val carboh: Float,
    @ColumnInfo(name = "calories") val calories: Float
)
