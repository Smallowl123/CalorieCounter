package com.example.calorie_counter.room

import androidx.annotation.WorkerThread
import com.example.calorie_counter.room.entity.Food
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.flow.Flow

class CCRepository(private val dao: CCDao) {
    val allMeals: Flow<List<Meal>> = dao.getAllMeals()
    val allFood: Flow<List<Food>> = dao.getAllFood()
    val popularFood: Flow<List<Food>> = dao.getMostPopularFood()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertMeal(meal: Meal) {
        dao.insertMeal(meal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFood(food: Food) {
        dao.insertFood(food)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllMeals() {
        dao.deleteAllMeals()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateFood(food: Food) {
        dao.updateFood(food)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateMeal(meal: Meal) {
        dao.updateMeal(meal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteMeal(meal: Meal) {
        dao.deleteMeal(meal)
    }

    fun getSearchedFood(searchQuery: String): Flow<List<Food>> {
        return dao.getSearchedFood(searchQuery)
    }
}