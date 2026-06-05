package ufg.edu.sv.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "consultas",
    foreignKeys = [
        ForeignKey(
            entity = Paciente::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pacienteId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Consulta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pacienteId: Int,
    val fecha: String,
    val observaciones: String,
    val tratamiento: String,
    val proximaCita: String
) : Serializable
