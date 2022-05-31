package com.example.calorie_counter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calorie_counter.R
import com.example.calorie_counter.room.entity.Food


class FoodListAdapter : ListAdapter<Food, FoodListAdapter.FoodViewHolder>(FoodComparator()) {

    private lateinit var mListener: OnItemClickListener


    interface OnItemClickListener {
        fun onItemClick(food: Food)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_card, parent, false)

        return FoodViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class FoodViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.rv_name)
        private val proteinView: TextView = itemView.findViewById(R.id.rv_protein)
        private val fatView: TextView = itemView.findViewById(R.id.rv_fat)
        private val carbohView: TextView = itemView.findViewById(R.id.rv_carboh)
        private val caloriesView: TextView = itemView.findViewById(R.id.rv_calories)

        private lateinit var currentFood: Food

        fun bind(food: Food) {
            //TODO Добавить всплывающее меню по лонг клику с удалением, по клику - добавлением еды в лист основного экрана
            currentFood = food

            nameView.text = food.name

            var s = food.protein.format(1)
            var t = "Б: $s"
            proteinView.text = t

            s = food.fat.format(1)
            t = "Ж: $s"
            fatView.text = t

            s = food.calories.format(1)
            t = "К: $s"
            caloriesView.text = t

            s = food.carboh.format(1)
            t = "У: $s"
            carbohView.text = t
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(currentFood)
            }
        }

        private fun Float.format(digits: Int) = "%.${digits}f".format(this)
    }

    class FoodComparator : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.name == newItem.name
        }
    }
}

