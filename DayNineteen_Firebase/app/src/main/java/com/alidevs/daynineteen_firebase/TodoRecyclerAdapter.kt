package com.alidevs.daynineteen_firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoRecyclerAdapter(var todoList: MutableList<TodoModel>) :
	RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>(), Filterable {

	private var todoFilterList: MutableList<TodoModel> = todoList

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val layout = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
		return ViewHolder(layout)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindTodos(todoFilterList[position])
	}

	override fun getItemCount(): Int {
		return todoFilterList.size
	}

	override fun getFilter(): Filter {
		return object : Filter() {
			override fun performFiltering(constraint: CharSequence?): FilterResults {
				val charSearch = constraint.toString()
				todoFilterList = if (charSearch.isEmpty()) {
					todoList
				} else {
					val resultList = ArrayList<TodoModel>()
					todoList.forEach {
						if (it.title.lowercase()
								.contains(charSearch.lowercase()) || it.description.lowercase()
								.contains(charSearch.lowercase())
						) {
							resultList.add(it)
						}
					}
					resultList.toMutableList()
				}
				val filterResult = FilterResults()
				filterResult.values = todoFilterList
				return filterResult
			}

			override fun publishResults(constraint: CharSequence?, result: FilterResults?) {
				todoFilterList = result?.values as MutableList<TodoModel>
				notifyDataSetChanged()
			}

		}
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bindTodos(todo: TodoModel) {
			val nameTextView = itemView.findViewById<TextView>(R.id.rowNameTextView)
			nameTextView.text = todo.title

			val descriptionTextView = itemView.findViewById<TextView>(R.id.rowCityTextView)
			descriptionTextView.text = todo.description

			itemView.setBackgroundColor(Integer.valueOf(todo.color))
		}
	}

}