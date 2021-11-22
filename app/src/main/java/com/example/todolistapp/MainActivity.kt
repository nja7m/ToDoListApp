package com.example.todolistapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), DatePickerListener {

    lateinit var tasklist: MutableList<Task>
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskNumberText: TextView


    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tasklist = mutableListOf<Task>()

        var greeting = findViewById<TextView>(R.id.textViewGreeting)
//        var name = findViewById<TextView>(R.id.textViewName)
        taskNumberText = findViewById<TextView>(R.id.textViewNumberofTasks)
        var progressBar = findViewById<ProgressBar>(R.id.mainProgressBar)
        var datePicker = findViewById<HorizontalPicker>(R.id.datePicker)
        var addButton = findViewById<FloatingActionButton>(R.id.floatingActionButtonAdded)
        var dateCardView = findViewById<CardView>(R.id.cardViewDate)
        var toolBar = findViewById<Toolbar>(R.id.taskToolbar)
//        var mySearch = findViewById<ImageView>(R.id.imageViewSearch)
        taskRecyclerView = findViewById<RecyclerView>(R.id.TaskRecyclerView)


        toolBar.title = "Home"
        setSupportActionBar(toolBar)
        taskAdapter = TaskAdapter(tasklist, db)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)


        greeting.text = getGreetingMessage()
        datePicker.setDateSelectedColor(ContextCompat.getColor(this, R.color.light_pink))


        datePicker
            .showTodayButton(true)
            .setListener(this)
            .init()



        addButton.setOnClickListener {
            var intent = Intent(this, NewTask::class.java)
            startActivity(intent)

        }
        db.collection("tasks") // get data from firebase
            .get()
            .addOnSuccessListener { task ->
                progressBar.visibility = View.INVISIBLE // make progress bar invisible
                for (document in task.documents) { // add task to recycler view one by one

                    tasklist.add(
                        Task(
                            document.id,
                            document.data!!.get("Title").toString(),
                            document.data!!.get("Time") as Long,
                            document.data!!.get("Date") as Long,
                            document.data!!.get("Description").toString(),
                            document.data!!.get("Status") as Boolean
                        )
                    )


                }

                var filteredList = tasklist.filter {
                    var taskConvertedDate =
                        convertLongToDate(it.date) //convert task date from date to long
                    var currentConvertedDate =
                        convertLongToDate(System.currentTimeMillis()) // convert system time to long
                    taskConvertedDate == currentConvertedDate
                } as MutableList<Task>  //

                taskRecyclerView.adapter =
                    TaskAdapter(filteredList, db) // refresh recycler view with filter
                taskNumberText.text = "(${filteredList.size})"
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show()
            }

    }

    fun getGreetingMessage(): String {
        var c = Calendar.getInstance()
        var timeOfDay = c[Calendar.HOUR_OF_DAY]

        return when (timeOfDay) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            else -> "Good Night"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        var filteredList = tasklist.filter {
            it.date == dateSelected!!.millis
        } as MutableList<Task>

        taskRecyclerView.adapter = TaskAdapter(filteredList, db)
        taskNumberText.text = "(${filteredList.size})"
    }


    fun convertLongToDate(time: Long): String {
        var date = Date(time)
        var format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

}


