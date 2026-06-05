package ufg.edu.sv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ufg.edu.sv.data.Paciente
import ufg.edu.sv.databinding.ActivityMainBinding
import ufg.edu.sv.ui.PacienteAdapter
import ufg.edu.sv.ui.PacienteViewModel
import java.io.File
import java.io.FileWriter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PacienteViewModel
    private lateinit var adapter: PacienteAdapter

    // Register Activity Result API for Add and Edit Patient flows
    private val startPatientFlowLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "Registro procesado exitosamente", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewModel
        viewModel = ViewModelProvider(this)[PacienteViewModel::class.java]

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)

        // Setup RecyclerView
        adapter = PacienteAdapter(
            onDetalleClick = { paciente ->
                val intent = Intent(this, DetalleActivity::class.java).apply {
                    putExtra("paciente", paciente)
                }
                startActivity(intent)
            },
            onEditarClick = { paciente ->
                val intent = Intent(this, RegistroActivity::class.java).apply {
                    putExtra("paciente", paciente)
                }
                startPatientFlowLauncher.launch(intent)
            },
            onEliminarClick = { paciente ->
                showDeleteConfirmationDialog(paciente)
            }
        )

        binding.rvPacientes.layoutManager = LinearLayoutManager(this)
        binding.rvPacientes.adapter = adapter

        // Observe patients LiveData
        viewModel.pacientes.observe(this) { list ->
            if (list.isNullOrEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvPacientes.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvPacientes.visibility = View.VISIBLE
                adapter.submitList(list)
            }
        }

        // Setup SearchView in real-time
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        // Setup Priority Filter Chips
        binding.chipGroupPriorities.setOnCheckedStateChangeListener { _, checkedIds ->
            val priority = when (checkedIds.firstOrNull()) {
                binding.chipLow.id -> "Baja"
                binding.chipMedium.id -> "Media"
                binding.chipHigh.id -> "Alta"
                else -> "Todos"
            }
            viewModel.setPriorityFilter(priority)
        }

        // Click listeners for toolbar actions
        binding.btnStats.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        binding.btnExport.setOnClickListener {
            exportPatientsToCSV()
        }

        // Click listener for FloatingActionButton (FAB)
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startPatientFlowLauncher.launch(intent)
        }
    }

    private fun showDeleteConfirmationDialog(paciente: Paciente) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Paciente")
            .setMessage("¿Está seguro de que desea eliminar el expediente de ${paciente.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deletePaciente(paciente)
                Toast.makeText(this, "Paciente eliminado correctamente", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun exportPatientsToCSV() {
        val patientsList = viewModel.pacientes.value ?: emptyList()
        if (patientsList.isEmpty()) {
            Toast.makeText(this, "No hay registros de pacientes para exportar", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Internal application directory
            val csvFile = File(filesDir, "pacientes.csv")
            val writer = FileWriter(csvFile)

            // Header line
            writer.append("Nombre,Edad,DUI,Prioridad,Fecha\n")

            // Append data rows
            for (p in patientsList) {
                // Escape commas in names if any
                val escapedName = p.nombre.replace(",", " ")
                writer.append("$escapedName,${p.edad},${p.dui},${p.prioridad},${p.fechaHoraRegistro}\n")
            }

            writer.flush()
            writer.close()

            // Success feedback
            Toast.makeText(this, "Archivo exportado correctamente (pacientes.csv)", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al exportar archivo: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}