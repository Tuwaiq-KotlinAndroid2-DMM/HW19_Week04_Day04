package com.example.day19practice.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.day19practice.Database.Task
import com.example.day19practice.MainActivityDisplayTask
import com.example.day19practice.R

class TaskAdapter(var context: Context, var data: MutableList<Task>): RecyclerView.Adapter<TaskHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var rowLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_row_layout, parent, false)
        return TaskHolder(rowLayout)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.taskTitle.text = data[position].title
        holder.taskDesc.text = data[position].description
        holder.taskDueDate.text = data[position].dueDate
        var first = data[position].member?.get(0)?.uppercase() ?: "A"
        var lastIndex = data[position].member?.lastIndexOf(' ')?.plus(1)
        var last = lastIndex?.let { data[position].member?.get(it)?.uppercase() } ?: "A"
        holder.taskMember.text = first + last

        holder.itemView.setOnClickListener {
            var intent = Intent(context, MainActivityDisplayTask::class.java)
            intent.putExtra("task",data[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = data.size

}

class TaskHolder(v: View): RecyclerView.ViewHolder(v) {
    //list_row_list components
    var taskTitle = v.findViewById<TextView>(R.id.textViewTaskTitle)
    var taskDesc = v.findViewById<TextView>(R.id.textViewTaskDesc)
    var taskDueDate = v.findViewById<TextView>(R.id.textViewTaskDueDate)
    var taskMember = v.findViewById<TextView>(R.id.textViewTaskMember)
}