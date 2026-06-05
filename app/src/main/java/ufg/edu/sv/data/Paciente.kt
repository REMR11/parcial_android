package ufg.edu.sv.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "pacientes")
data class Paciente(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val edad: Int,
    val dui: String,
    val telefono: String,
    val direccion: String,
    val correo: String,
    val tipoSangre: String,
    val peso: Double,
    val estatura: Double,
    val sintomas: String,
    val diagnostico: String,
    val prioridad: String, // Baja, Media, Alta, Crítica
    val hospitalizacion: Boolean,
    val examenes: Boolean,
    val enfermedadCronica: Boolean,
    val fechaHoraRegistro: String
) : Serializable
