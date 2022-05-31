package com.example.calorie_counter.room

import androidx.room.*
import com.example.calorie_counter.room.entity.Food
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface CCDao {

    @Query("SELECT * FROM meal_table")
    fun getAllMeals(): Flow<List<Meal>>

    @Insert(onConflict = 5)
    suspend fun insertMeal(meal: Meal)


    @Query("DELETE FROM meal_table")
    suspend fun deleteAllMeals()

    @Query("DELETE FROM food_table")
    suspend fun deleteAllFood()

    @Query("SELECT * FROM food_table")
    fun getAllFood(): Flow<List<Food>>

    @Query("SELECT * FROM food_table WHERE lastInIndex > 0 ORDER BY lastInIndex DESC limit 20")
    fun getMostPopularFood(): Flow<List<Food>>

    @Query("SELECT * FROM food_table WHERE name LIKE :searchQuery ORDER BY userMade DESC")
    fun getSearchedFood(searchQuery: String): Flow<List<Food>>

    @Insert(onConflict = 5)
    suspend fun insertFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)
}