package com.example.calorie_counter.room

import androidx.annotation.WorkerThread
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.flow.Flow

class CCRepository(private val dao: CCDao) {
    val allMeals: Flow<List<Meal>> = dao.getAllMeals()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(meal: Meal) {
        dao.insert(meal)
    }
}