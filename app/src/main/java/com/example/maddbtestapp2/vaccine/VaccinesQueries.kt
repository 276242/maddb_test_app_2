package com.example.maddbtestapp2.vaccine

import java.sql.Connection
import java.sql.Date
import java.sql.SQLException

/**
 * Class for performing database operations related to vaccines.
 */
class VaccinesQueries(private val connection : Connection) : VaccinesDAO {
    /**
     * Retrieves a vaccine by its id.
     */
    override fun getVaccineById(id: Int): Vaccines? {
        val query = "SELECT * FROM vaccine_table WHERE vaccine_id = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, id)
        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            return Vaccines(
                id = resultSet.getInt("vaccine_id"),
                vaccineName = resultSet.getString("vaccine_name"),
                administeredDate = resultSet.getDate("date_administered"),
                nextDoseDate = resultSet.getDate("date_next_dose")
            )
        }
        return null
    }

    /**
     * Retrieves a vaccine by its name.
     */
    override fun getVaccineByName(vaccineName: String): Vaccines? {
        val query = "SELECT * FROM vaccine_table WHERE vaccine_name = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, vaccineName)
        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            return Vaccines(
                id = resultSet.getInt("vaccine_id"),
                vaccineName = resultSet.getString("vaccine_name"),
                administeredDate = resultSet.getDate("date_administered"),
                nextDoseDate = resultSet.getDate("date_next_dose")
            )
        }
        return null
    }

    /**
     * Checks if a vaccine exists by its name.
     */
    override fun doesVaccineExist(vaccineName: String): Boolean {
        val query = "SELECT * FROM `vaccine_table` WHERE `vaccine_name` = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, vaccineName)
        val resultSet = preparedStatement.executeQuery()
        return resultSet.next()
    }

    /**
     * Retrieves a vaccine id by its name.
     */
    override fun getVaccineIdByVaccineName(vaccineName: String): Int {
        val query = "SELECT `vaccine_id` FROM `vaccine_table` WHERE `vaccine_name` = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, vaccineName)
        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            return resultSet.getInt("vaccine_id")
        }
        return -1
    }

    /**
     * Retrieves all vaccines.
     */
    override fun getAllVaccines(): Set<Vaccines?>? {
        val query = "SELECT * FROM `vaccine_table`"
        val preparedStatement = connection.prepareStatement(query)
        val resultSet = preparedStatement.executeQuery()
        val vaccines = mutableSetOf<Vaccines>()
        while (resultSet.next()) {
            vaccines.add(
                Vaccines(
                    id = resultSet.getInt("vaccine_id"),
                    vaccineName = resultSet.getString("vaccine_name"),
                    administeredDate = resultSet.getDate("date_administered"),
                    nextDoseDate = resultSet.getDate("date_next_dose")
                )
            )
        }
        return if (vaccines.isEmpty()) {
            null
        } else {
            vaccines
        }
    }

    /**
     * Retrieves all vaccine names.
     */
    fun getAllVaccineNames(): List<String>? {
        val query = "SELECT `vaccine_name` FROM `vaccine_table`"
        val preparedStatement = connection.prepareStatement(query)
        val resultSet = preparedStatement.executeQuery()
        val vaccineNames = mutableListOf<String>()
        while (resultSet.next()) {
            vaccineNames.add(resultSet.getString("vaccine_name"))
        }
        return if (vaccineNames.isEmpty()) {
            null
        } else {
            vaccineNames
        }
    }

    /**
     * Retrieves the date a vaccine was administered by its id.
     */
    override fun getDateAdministeredByVaccineId(id: Int): Date? {
        val query = "SELECT `date_administered` FROM `vaccine_table` WHERE `vaccine_id` = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, id)
        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            return resultSet.getDate("date_administered")
        }
        return null
    }

    /**
     * Inserts a new vaccine.
     */
    override fun insertVaccine(vaccine: Vaccines): Boolean {
        try {
            if (doesVaccineExist(vaccine.vaccineName)) {
                return false
            }

            val query = "INSERT INTO vaccine_table (vaccine_name, date_administered, date_next_dose) VALUES (?, ?, ?)"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setString(1, vaccine.vaccineName)
            preparedStatement.setDate(2, vaccine.administeredDate)
            preparedStatement.setDate(3, vaccine.nextDoseDate)

            println("Executing query: $query")
            val result = preparedStatement.executeUpdate()
            println("Result of executeUpdate: $result")

            return result > 0
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Updates an existing vaccine.
     */
    override fun updateVaccine(id: Int, vaccine: Vaccines): Boolean {
        val query = "UPDATE `vaccine_table` SET `date_administered = ? WHERE `vaccine_name = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setDate(1, vaccine.administeredDate)
        preparedStatement.setString(2, vaccine.vaccineName)
        return preparedStatement.executeUpdate() > 0
    }

    /**
     * Updates the name of an existing vaccine.
     */
    override fun updateVaccineName(id: Int, newName: String): Boolean {
        val query = "UPDATE `vaccine_table` SET `vaccine_name` = ? WHERE `vaccine_id` = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, newName)
        preparedStatement.setInt(2, id)
        return preparedStatement.executeUpdate() > 0
    }

    /**
     * Deletes a vaccine by its id.
     */
    override fun deleteVaccine(id: Int): Boolean {
        val query = "DELETE FROM `vaccine_table` WHERE 'vaccine_id' = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, id)
        return preparedStatement.executeUpdate() > 0
    }
}