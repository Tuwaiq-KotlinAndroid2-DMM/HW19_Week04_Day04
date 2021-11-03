package com.example.day19practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.day19practice.Database.AppDatabase
import com.example.day19practice.Database.DatabaseClient
import com.example.day19practice.Database.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivityDisplayTask : AppCompatActivity() {

    var dbClient: DatabaseClient? = null
    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_display_task)

        dbClient = DatabaseClient(applicationContext)
        db = dbClient?.getInstance(applicationContext)?.getAppDatabase()

        var data = intent.getSerializableExtra("task") as Task
        if (data == null){
            Toast.makeText(this, getString(R.string.warning_task_id), Toast.LENGTH_SHORT).show()
            finish()
        }

        setTask(data)

        var deleteBtn = findViewById<FloatingActionButton>(R.id.fabDeleteTask)

        deleteBtn.setOnClickListener {
            if (data != null) {
                db?.TaskDao()?.delete(data)
                Toast.makeText(this, getString(R.string.task_delete_success), Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, getString(R.string.task_delete_failure), Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setTask(data: Task?) {
        if (data != null) {
            findViewById<TextView>(R.id.textViewDisplayTitle).text = data.title
            findViewById<TextView>(R.id.textViewDisplayDesciption).text = data.description
            findViewById<TextView>(R.id.textViewDisplayDueDate).text = data.dueDate
            var first = data.member?.get(0)?.uppercase() ?: "A"
            var lastIndex = data.member?.lastIndexOf(' ')?.plus(1)
            var last = lastIndex?.let { data.member?.get(it)?.uppercase() } ?: "A"
            findViewById<TextView>(R.id.textViewDisplayMember).text = first + last
        }
    }

}