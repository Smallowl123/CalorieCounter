package com.example.calorie_counter.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calorie_counter.R
import com.example.calorie_counter.adapters.FoodListAdapter
import com.example.calorie_counter.room.CCApplication
import com.example.calorie_counter.room.CCViewModel
import com.example.calorie_counter.room.CCViewModelFactory
import com.example.calorie_counter.room.entity.Food
import com.example.calorie_counter.room.entity.Meal

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val adapter = FoodListAdapter()

    private val ccViewModel: CCViewModel by viewModels {
        CCViewModelFactory((application as CCApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.action_toolbar_search))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_search)

        adapter.setOnItemClickListener(object : FoodListAdapter.OnItemClickListener {
            override fun onItemClick(food: Food) {
                addMealToDB(food)
            }

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        ccViewModel.popularFood.observe(
            this
        ) { foods ->
            foods?.let { adapter.submitList(it) }
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(this)

        val addFoodButton: ImageView = findViewById(R.id.add_food_button)
        addFoodButton.setOnClickListener {
            addFoodToDB()
        }
    }

    private fun addFoodToDB() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewInflated: View = LayoutInflater.from(this)
            .inflate(
                R.layout.new_food_dialog,
                findViewById(R.id.constraint_layout_search),
                false
            )
        builder.setView(viewInflated)
        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            val name: String = viewInflated.findViewById<EditText>(R.id.input_food_name).text.toString()
            val protein: Float =viewInflated.findViewById<EditText>(R.id.input_food_protein).text.toString().toFloat()
            val fat: Float = viewInflated.findViewById<EditText>(R.id.input_food_fat).text.toString().toFloat()
            val carboh: Float =
                viewInflated.findViewById<EditText>(R.id.input_food_carboh).text.toString().toFloat()
            val calories: Float =
                viewInflated.findViewById<EditText>(R.id.input_food_calories).text.toString().toFloat()
            ccViewModel.insertFood(Food(0, name, protein, fat, carboh, calories, 0, true))
        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun createMeal(food: Food, weight: Int): Meal {
        return Meal(
            0,
            food.name,
            weight,
            food.protein * (weight / 100f),
            food.fat * (weight / 100f),
            food.carboh * (weight / 100f),
            food.calories * (weight / 100f)
        )
    }

    fun addMealToDB(food: Food) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewInflated: View = LayoutInflater.from(this)
            .inflate(
                R.layout.weight_dialog,
                findViewById(R.id.constraint_layout_search),
                false
            )
        val input = viewInflated.findViewById<View>(R.id.input_weight) as EditText
        builder.setView(viewInflated)
        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            val weight = Integer.parseInt(input.text.toString())
            if (weight != 0) {
                ccViewModel.insertMeal(createMeal(food, weight))
                ccViewModel.updateFood(
                    Food(
                        food.id,
                        food.name,
                        food.protein,
                        food.fat,
                        food.carboh,
                        food.calories,
                        food.lastInIndex + 1,
                        food.userMade
                    )
                )
                finish()
            }
        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchFood(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchFood(query)
        }
        return true
    }

    private fun searchFood(query: String) {
        val searchQuery = "%$query%"
        ccViewModel.getSearchedFood(searchQuery).observe(this) { foods ->
            foods?.let { adapter.submitList(foods) }
        }

    }
}