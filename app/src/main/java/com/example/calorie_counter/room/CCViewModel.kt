package com.example.calorie_counter.room

import androidx.lifecycle.*
import com.example.calorie_counter.room.entity.Food
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.launch

class CCViewModel(private val repository: CCRepository) : ViewModel() {
    val allMeals: LiveData<List<Meal>> = repository.allMeals.asLiveData()
    val allFood: LiveData<List<Food>> = repository.allFood.asLiveData()
    val popularFood: LiveData<List<Food>> = repository.popularFood.asLiveData()

    fun insertMeal(meal: Meal) = viewModelScope.launch {
        repository.insertMeal(meal)
    }

    fun insertFood(food: Food) = viewModelScope.launch {
        repository.insertFood(food)
    }

    fun deleteAllMeals() = viewModelScope.launch {
        repository.deleteAllMeals()
    }

    fun updateFood(food: Food) = viewModelScope.launch {
        repository.updateFood(food)
    }

    fun updateMeal(meal: Meal) = viewModelScope.launch {
        repository.updateMeal(meal)
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch {
        repository.deleteMeal(meal)
    }

    fun getSearchedFood(searchQuery: String): LiveData<List<Food>> {
        return repository.getSearchedFood(searchQuery).asLiveData()
    }
}

class CCViewModelFactory(private val repository: CCRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CCViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CCViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}