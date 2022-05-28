package com.example.calorie_counter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface CCDao {

    //TODO Поиск по части name

    @Query("SELECT * FROM meal_table")
    fun getAllMeals(): Flow<List<Meal>>

    @Insert(onConflict = 5)
    suspend fun insert(meal: Meal)

    @Query("DELETE FROM meal_table")
    suspend fun deleteAll()

}