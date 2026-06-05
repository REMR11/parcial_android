package ufg.edu.sv.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ufg.edu.sv.data.AppDatabase
import ufg.edu.sv.data.Paciente
import ufg.edu.sv.data.PacienteRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PacienteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PacienteRepository
    
    // LiveData for the search query and priority filter
    private val searchQuery = MutableLiveData<String>("")
    private val priorityFilter = MutableLiveData<String>("Todos")

    // The combined LiveData that updates when search query or priority changes
    val pacientes: LiveData<List<Paciente>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PacienteRepository(database.pacienteDao())

        // Combine filter triggers
        val combinedTriggers = MediatorLiveData<Pair<String, String>>()
        combinedTriggers.addSource(searchQuery) { query ->
            combinedTriggers.value = Pair(query ?: "", priorityFilter.value ?: "Todos")
        }
        combinedTriggers.addSource(priorityFilter) { filter ->
            combinedTriggers.value = Pair(searchQuery.value ?: "", filter ?: "Todos")
        }

        pacientes = combinedTriggers.switchMap { pair ->
            val query = pair.first
            val filter = pair.second
            if (filter == "Todos") {
                repository.searchPacientes(query)
            } else {
                repository.searchAndFilterPacientes(query, filter)
            }
        }
    }

    // Setter methods for queries
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setPriorityFilter(priority: String) {
        priorityFilter.value = priority
    }

    // Database CRUD Operations in ViewModelScope
    fun insertPaciente(paciente: Paciente, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insertPaciente(paciente)
            onResult(id)
        }
    }

    fun updatePaciente(paciente: Paciente, onResult: () -> Unit) {
        viewModelScope.launch {
            repository.updatePaciente(paciente)
            onResult()
        }
    }

    fun deletePaciente(paciente: Paciente) {
        viewModelScope.launch {
            repository.deletePaciente(paciente)
        }
    }

    // Dashboard metrics
    fun getTotalPacientesCount(): LiveData<Int> = repository.getTotalPacientesCount()
    fun getHighPriorityCount(): LiveData<Int> = repository.getHighPriorityCount()
    fun getHospitalizedCount(): LiveData<Int> = repository.getHospitalizedCount()
    
    fun getTodayCount(): LiveData<Int> {
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        return repository.getTodayCount(currentDate)
    }
}

