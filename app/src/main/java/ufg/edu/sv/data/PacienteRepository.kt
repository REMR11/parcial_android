package ufg.edu.sv.data

import androidx.lifecycle.LiveData

class PacienteRepository(private val pacienteDao: PacienteDao) {

    fun getAllPacientes(): LiveData<List<Paciente>> = pacienteDao.getAllPacientes()

    fun searchPacientes(query: String): LiveData<List<Paciente>> {
        return pacienteDao.searchPacientes("%$query%")
    }

    fun searchAndFilterPacientes(query: String, prioridad: String): LiveData<List<Paciente>> {
        return pacienteDao.searchAndFilterPacientes("%$query%", prioridad)
    }

    suspend fun insertPaciente(paciente: Paciente): Long {
        return pacienteDao.insertPaciente(paciente)
    }

    suspend fun updatePaciente(paciente: Paciente) {
        pacienteDao.updatePaciente(paciente)
    }

    suspend fun deletePaciente(paciente: Paciente) {
        pacienteDao.deletePaciente(paciente)
    }

    suspend fun getPacienteById(id: Int): Paciente? {
        return pacienteDao.getPacienteById(id)
    }

    // Dashboard data
    fun getTotalPacientesCount(): LiveData<Int> = pacienteDao.getTotalPacientesCount()
    fun getHighPriorityCount(): LiveData<Int> = pacienteDao.getHighPriorityCount()
    fun getHospitalizedCount(): LiveData<Int> = pacienteDao.getHospitalizedCount()
    fun getTodayCount(dateQuery: String): LiveData<Int> = pacienteDao.getTodayCount("%$dateQuery%")
}
