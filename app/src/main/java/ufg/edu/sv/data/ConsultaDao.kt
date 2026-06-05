package ufg.edu.sv.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsulta(consulta: Consulta)

    @Update
    suspend fun updateConsulta(consulta: Consulta)

    @Delete
    suspend fun deleteConsulta(consulta: Consulta)

    @Query("SELECT * FROM consultas WHERE pacienteId = :pacienteId ORDER BY id DESC")
    fun getConsultasPorPaciente(pacienteId: Int): Flow<List<Consulta>>
    
    @Query("SELECT * FROM consultas")
    fun getAllConsultas(): Flow<List<Consulta>>
}
