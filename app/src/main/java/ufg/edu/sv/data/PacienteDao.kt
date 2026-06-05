package ufg.edu.sv.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PacienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaciente(paciente: Paciente): Long

    @Update
    suspend fun updatePaciente(paciente: Paciente)

    @Delete
    suspend fun deletePaciente(paciente: Paciente)

    @Query("SELECT * FROM pacientes WHERE id = :id")
    suspend fun getPacienteById(id: Int): Paciente?

    @Query("SELECT * FROM pacientes ORDER BY id DESC")
    fun getAllPacientes(): LiveData<List<Paciente>>

    @Query("SELECT * FROM pacientes WHERE nombre LIKE :query OR dui LIKE :query ORDER BY id DESC")
    fun searchPacientes(query: String): LiveData<List<Paciente>>

    @Query("SELECT * FROM pacientes WHERE (nombre LIKE :query OR dui LIKE :query) AND prioridad = :prioridad ORDER BY id DESC")
    fun searchAndFilterPacientes(query: String, prioridad: String): LiveData<List<Paciente>>

    // Queries for Dashboard
    @Query("SELECT COUNT(*) FROM pacientes")
    fun getTotalPacientesCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM pacientes WHERE prioridad = 'Alta'")
    fun getHighPriorityCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM pacientes WHERE hospitalizacion = 1")
    fun getHospitalizedCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM pacientes WHERE fechaHoraRegistro LIKE :dateQuery")
    fun getTodayCount(dateQuery: String): LiveData<Int>
}
