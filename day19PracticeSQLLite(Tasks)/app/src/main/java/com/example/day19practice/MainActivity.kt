package com.example.day19practice

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.day19practice.Adapter.TaskAdapter
import com.example.day19practice.Database.AppDatabase
import com.example.day19practice.Database.DatabaseClient
import com.example.day19practice.Database.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    var dbClient: DatabaseClient? = null
    var db: AppDatabase? = null

    val currentList: MutableList<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbClient = DatabaseClient(applicationContext)
        db = dbClient?.getInstance(applicationContext)?.getAppDatabase()

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddTask)
        val recycledViewTasks = findViewById<RecyclerView>(R.id.recycledViewTasks)
        recycledViewTasks.layoutManager = LinearLayoutManager(this)

        if (db != null) {
            currentList.addAll(db!!.TaskDao().getAll())
        }
        val adapter = TaskAdapter(this, currentList)
        recycledViewTasks.adapter = adapter

        fabAdd.setOnClickListener { db?.let { onAddTask(it, recycledViewTasks) } }
    }

    override fun onStart() {
        super.onStart()
        db?.TaskDao()?.let { refresh(it.getAll(),findViewById(R.id.recycledViewTasks)) }
    }

    fun onAddTask(db: AppDatabase, recycledViewTasks: RecyclerView) {
        val customAlertDialog = AlertDialog.Builder(this).create()
        customAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = layoutInflater.inflate(R.layout.task_add_layout,null)

        val editTextTitle = view.findViewById<EditText>(R.id.EditTextTaskTitle)
        val editTextDescription = view.findViewById<EditText>(R.id.EditTextTaskDescription)
        val editTextMember = view.findViewById<EditText>(R.id.EditTextTaskMember)
        val editTextDate = view.findViewById<EditText>(R.id.EditTextTaskDueDate)
        val btnAddItem = view.findViewById<Button>(R.id.buttonAddTask)

        btnAddItem.setOnClickListener {
            db.TaskDao().insertAll(Task(
                null,
                editTextTitle.text.toString(),
                editTextDate.text.toString(),
                editTextDescription.text.toString(),
                editTextMember.text.toString()
            ))
            refresh(db.TaskDao().getAll(),recycledViewTasks)
        }

        customAlertDialog.setView(view)

        customAlertDialog.setCanceledOnTouchOutside(true)
        customAlertDialog.show()
    }

    fun refresh(data: List<Task>, recycledViewTasks: RecyclerView) {
        currentList.clear()
        currentList.addAll(data)
        recycledViewTasks.adapter?.notifyDataSetChanged()
    }

    fun shortenDateString(year: Int, month: Int, date: Int): String{
        val calender = LocalDate.of(year,month,date)
        var day = calender.dayOfMonth
        var month = calender.month.name.substring(0,3)
        var year = calender.year
        return "$month $day $year"
    }
}

//    fun addTask(db: AppDatabase, recycledViewTasks: RecyclerView){
//        val taskdoa = db?.TaskDao()
//        taskdoa.insertAll(Task(null,"task1", shortenDateString(2021,12,12),"complete tasks in time", "Abbas Ali"))
//        taskdoa.insertAll(Task(null,"task2", shortenDateString(2021,12,14),"complete tasks in time", "Mohammed Ibrahim"))
//        taskdoa.insertAll(Task(null,"task3", shortenDateString(2021,12,16),"complete tasks in time", "Dana Khalid"))
//        refresh(taskdoa.getAll(),recycledViewTasks)
//    }