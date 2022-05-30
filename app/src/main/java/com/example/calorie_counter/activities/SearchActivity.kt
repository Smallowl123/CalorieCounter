package com.example.calorie_counter.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calorie_counter.R
import com.example.calorie_counter.adapters.FoodListAdapter
import com.example.calorie_counter.room.CCApplication
import com.example.calorie_counter.room.CCViewModel
import com.example.calorie_counter.room.CCViewModelFactory

class SearchActivity : AppCompatActivity() {

    private val ccViewModel: CCViewModel by viewModels {
        CCViewModelFactory((application as CCApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.action_toolbar_search))

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_search)
        val adapter = FoodListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        ccViewModel.allFood.observe(
            this,
            Observer { foods ->
                foods?.let { adapter.submitList(it) }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}