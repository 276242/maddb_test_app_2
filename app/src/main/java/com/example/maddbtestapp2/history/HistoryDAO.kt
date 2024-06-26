package com.example.maddbtestapp2.history
/**
 * AppointmentDAO is an interface that defines the operations for managing appointments.
 *
 * @property getAppointmentById Function to get an appointment by its id.
 * @property getAllAppointmentsByUserId Function to get all appointments for a specific user.
 * @property getAllAppointmentsByVaccineId Function to get all appointments for a specific vaccine.
 * @property getAllAppointments Function to get all appointments.
 * @property insertAppointment Function to insert a new appointment.
 * @property updateAppointment Function to update an existing appointment.
 * @property deleteAppointment Function to delete an appointment.
 */
import java.sql.Date

interface HistoryDAO {
    fun getHistoryById(id: Int): History?
    fun getHistoryByVaccineId(vaccineId: Int): List<History>?
    fun getHistoryIdByVaccineId(vaccineId: Int): Int
    fun getDateAdministeredByVaccineId(vaccineId: Int): Date?
    fun getAllHistories(): Set<History?>?
    fun insertHistory(history: History): Boolean
    fun updateAdministrationDate(id: Int, newDate: Date): Boolean
    fun updateHistory(id: Int, history: History): Boolean
    fun deleteHistory(id: Int): Boolean
}
