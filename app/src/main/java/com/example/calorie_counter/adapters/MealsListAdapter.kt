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

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(meal: Meal)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_card, parent, false)

        return MealsViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class MealsViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val mealNameView: TextView = itemView.findViewById(R.id.rv_name)
        private val mealWeightView: TextView = itemView.findViewById(R.id.rv_meal_weight)
        private val mealProteinView: TextView = itemView.findViewById(R.id.rv_protein)
        private val mealFatView: TextView = itemView.findViewById(R.id.rv_fat)
        private val mealCarbohView: TextView = itemView.findViewById(R.id.rv_carboh)
        private val mealCaloriesView: TextView = itemView.findViewById(R.id.rv_calories)

        private lateinit var currentMeal: Meal

        fun bind(meal: Meal) {

            currentMeal = meal

            mealNameView.text = meal.name

            var s = meal.weight.toString()
            var t = "Вес: $s"
            mealWeightView.text = t

            s = meal.protein.format(1)
            t = "Б: $s"
            mealProteinView.text = t

            s = meal.fat.format(1)
            t = "Ж: $s"
            mealFatView.text = t

            s = meal.calories.format(0)
            t = "К: $s"
            mealCaloriesView.text = t

            s = meal.carboh.format(1)
            t = "У: $s"
            mealCarbohView.text = t
        }


        init {
            itemView.setOnClickListener {
                listener.onItemClick(currentMeal)
            }
        }

        private fun Float.format(digits: Int) = "%.${digits}f".format(this)
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