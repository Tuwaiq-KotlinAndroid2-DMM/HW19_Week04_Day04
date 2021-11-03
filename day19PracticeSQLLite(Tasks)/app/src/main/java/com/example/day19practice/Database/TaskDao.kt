package com.example.day19practice.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM Task WHERE uid IN (:taskIds)")
    fun loadAllByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM Task WHERE uid = (:taskID) LIMIT 1")
    fun findByID(taskID: Int): Task

    @Query("SELECT * FROM Task WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): Task

    @Insert
    fun insertAll(vararg users: Task)

    @Delete
    fun delete(task: Task)
}