package com.alidevs.daynineteen_firebase

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.daynineteen_firebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
	private var binding: ActivityMainBinding? = null
	private lateinit var firestoreDatabase: FirebaseFirestore
	private lateinit var todoList: MutableList<TodoModel>
	private lateinit var recyclerView: RecyclerView
	private lateinit var todosAdapter: TodoRecyclerAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding?.root)
		setSupportActionBar(binding!!.toolbar)

		firestoreDatabase = FirebaseFirestore.getInstance()

		todoList = ArrayList()
		recyclerView = binding!!.todosRecyclerView
		recyclerView.layoutManager = LinearLayoutManager(this)
		todosAdapter = TodoRecyclerAdapter(todoList)
		recyclerView.adapter = todosAdapter

		readData()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main_menu, menu)
		val searchItem = menu?.findItem(R.id.app_bar_search)
		val searchView = searchItem?.actionView as SearchView

		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				return true
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				todosAdapter.filter.filter(newText)
				return true
			}

		})
		return super.onCreateOptionsMenu(menu)
	}

	fun addTodoButtonPressed(view: View) {
		val dialog = AddTodoDialog(this, firestoreDatabase)

		val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
		dialog.show(supportFragmentManager, "AddTodoDialog")
		supportFragmentManager.executePendingTransactions()
		dialog.dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
	}

	fun readData() {
		binding!!.progressBar.visibility = View.VISIBLE

		firestoreDatabase
			.collection("todos")
			.orderBy("timestamp", Query.Direction.DESCENDING)
			.get()
			.addOnSuccessListener { querySnapshot ->
				if (querySnapshot.isEmpty) {
					binding!!.emptyStateImageView.visibility = View.VISIBLE
					Toast.makeText(this, "You have nothing to do ..", Toast.LENGTH_SHORT).show()
				} else {
					binding!!.emptyStateImageView.visibility = View.INVISIBLE
				}
				binding!!.progressBar.visibility = View.INVISIBLE
				todoList.clear()
				querySnapshot.forEach { document ->
					val todoTitle = document.data["title"] as String
					val todoDescription = document.data["description"] as String
					val todoColor = document.data["color"] as Long
					val retrievedTodo = TodoModel(todoTitle, todoDescription, todoColor.toInt())
					Log.d("Firebase GET", retrievedTodo.toString())
					todoList.add(retrievedTodo)
					binding!!.progressBar.visibility = View.INVISIBLE
					todosAdapter.notifyDataSetChanged()
				}
			}
			.addOnFailureListener { exception ->
				Toast.makeText(this, "Failed to get data from Firebase", Toast.LENGTH_SHORT).show()
				Log.d("Firebase GET", exception.message.toString())
			}
	}

}