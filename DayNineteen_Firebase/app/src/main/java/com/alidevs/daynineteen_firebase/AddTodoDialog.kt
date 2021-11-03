package com.alidevs.daynineteen_firebase

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.alidevs.daynineteen_firebase.databinding.AddTodoDialogBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp

class AddTodoDialog (var activity: MainActivity, var firestoreDatabase: FirebaseFirestore) : DialogFragment() {
	private var _binding: AddTodoDialogBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = AddTodoDialogBinding.inflate(inflater, container, false)

		binding.addTodoButton.setOnClickListener { addTodoButtonPressed() }
		binding.dialogProgressBar.visibility = View.INVISIBLE
		return binding.root
	}

	private fun addTodoButtonPressed() {
		val colors = arrayOf(
			Color.parseColor("#FFE699"),
			Color.parseColor("#CDF2CA"),
			Color.parseColor("#C9CBFF"),
			Color.parseColor("#FCD8D4"),
			Color.parseColor("#B5EAEA"),
		)
		val titleEditText = binding.dialogTitleTextField.editText
		val descriptionEditText = binding.dialogDescriptionTextField.editText
		val progressBar = binding.dialogProgressBar

		val title = titleEditText?.text.toString()
		val description = descriptionEditText?.text.toString()

		val hashMap = hashMapOf<String, Any>(
			"title" to title,
			"description" to description,
			"color" to colors.random(),
			"timestamp" to Timestamp(System.currentTimeMillis())
		)

		progressBar.visibility = View.VISIBLE
		firestoreDatabase
			.collection("todos")
			.add(hashMap)
			.addOnSuccessListener {
				progressBar.visibility = View.INVISIBLE
				titleEditText?.text?.clear()
				descriptionEditText?.text?.clear()
				activity.readData()
				dialog?.dismiss()
				Toast.makeText(context, "Todo added successfully", Toast.LENGTH_SHORT).show()
			}
			.addOnFailureListener {
				Toast.makeText(context, "Failed to add todo", Toast.LENGTH_SHORT).show()
				Log.d("Firebase error", it.message.toString())
			}
	}

}