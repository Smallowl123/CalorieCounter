package com.example.calorie_counter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calorie_counter.R
import com.example.calorie_counter.room.entity.Meal

class MealsListAdapter : ListAdapter<Meal, MealsListAdapter.MealsViewHolder>(MealsComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        return MealsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class MealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealNameView: TextView = itemView.findViewById(R.id.rv_name)
        private val mealWeightView: TextView = itemView.findViewById(R.id.rv_meal_weigth)
        private val mealProteinView: TextView = itemView.findViewById(R.id.rv_protein)
        private val mealFatView: TextView = itemView.findViewById(R.id.rv_fat)
        private val mealCarbohView: TextView = itemView.findViewById(R.id.rv_carboh)
        private val mealCaloriesView: TextView = itemView.findViewById(R.id.rv_calories)

        fun bind(meal: Meal) {
            //TODO Добавить всплывающее меню по лонг клику с удалением/изменением веса
            mealNameView.text = meal.name

            var s = meal.weight.toString()
            var t = "Вес: $s"
            mealWeightView.text = t

            s = meal.protein.toString()
            t = "Б: $s"
            mealProteinView.text = t

            s = meal.fat.toString()
            t = "Ж: $s"
            mealFatView.text = t

            s = meal.calories.toString()
            t = "К: $s"
            mealCaloriesView.text = t

            s = meal.carboh.toString()
            t = "У: $s"
            mealCarbohView.text = t
        }

        companion object {
            fun create(parent: ViewGroup): MealsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_card, parent, false)
                return MealsViewHolder(view)
            }
        }
    }

    class MealsComparator : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.name == newItem.name
        }
    }
}