package com.example.maddbtestapp2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.maddbtestapp2.vaccine.Vaccines
import java.util.Calendar
import java.util.Date
import com.example.maddbtestapp2.databaseConfig.DbConnect
import com.example.maddbtestapp2.vaccine.VaccinesQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Activity responsible for adding a new vaccine to the database.
 * This activity allows the user to input the vaccine name, the date it was administered,
 * and the date of the next dose. It provides options to select dates using DatePickerDialog.
 * Upon saving, the vaccine information is inserted into the database.
 */
class AddNewVaccActivity : BaseActivity() {

    private lateinit var inputVaccName: EditText
    private lateinit var dateAdministered: Date
    private lateinit var nextDoseDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_vacc)

        // Initialize views
        inputVaccName = findViewById(R.id.editVaccName)
        val btnAdministeredDate = findViewById<Button>(R.id.btnSetUpDate)
        val btnNextDoseDate = findViewById<Button>(R.id.btnSetUpDate2)
        val homeButton = findViewById<ImageView>(R.id.homeButton2)

        // Set up DatePickerDialog for administered date
        btnAdministeredDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth, 0, 0, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    dateAdministered = calendar.time
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Set up DatePickerDialog for next dose date
        btnNextDoseDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth, 0, 0, 0)
                    nextDoseDate = calendar.time
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Save button click listener
        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val vaccName = inputVaccName.text.toString()

            // Create Vaccines object
            val vaccine = Vaccines(
                id = null,
                vaccineName = vaccName,
                administeredDate = java.sql.Date(dateAdministered.time),
                nextDoseDate = java.sql.Date(nextDoseDate.time)
            )

            // Insert vaccine into database
            CoroutineScope(Dispatchers.IO).launch {
                val connection = DbConnect.getConnection()
                val vaccinesQueries = VaccinesQueries(connection = connection)
                vaccinesQueries.insertVaccine(vaccine)

                // Navigate back to MainActivity
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(this@AddNewVaccActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        // Home button click listener
        homeButton.setOnClickListener {
            goToMainActivity()
        }
    }

    /**
     * Navigates to MainActivity.
     */
    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}