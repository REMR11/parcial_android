package ufg.edu.sv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ufg.edu.sv.databinding.ActivityDashboardBinding
import ufg.edu.sv.ui.PacienteViewModel
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: PacienteViewModel

    private var totalCount = 0
    private var highCount = 0
    private var hospitalizedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewModel
        viewModel = ViewModelProvider(this)[PacienteViewModel::class.java]

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // Observe stats LiveData and update screen dynamically
        setupObservers()
    }

    private fun setupObservers() {
        // We observe all count values to update UI in real-time
        viewModel.getTotalPacientesCount().observe(this) { total ->
            totalCount = total ?: 0
            binding.tvTotalPacientes.text = totalCount.toString()
            updateProportions()
        }

        viewModel.getHighPriorityCount().observe(this) { hc ->
            highCount = hc ?: 0
            binding.tvPrioridadAlta.text = highCount.toString()
            updateProportions()
        }

        viewModel.getHospitalizedCount().observe(this) { hospCount ->
            hospitalizedCount = hospCount ?: 0
            binding.tvHospitalizados.text = hospitalizedCount.toString()
            updateProportions()
        }

        viewModel.getTodayCount().observe(this) { todayCount ->
            binding.tvRegistradosHoy.text = (todayCount ?: 0).toString()
        }
    }

    private fun updateProportions() {
        val total = totalCount
        val high = highCount
        val hospitalized = hospitalizedCount

        if (total > 0) {
            // Calculate ratios
            val hospRatio = (hospitalized.toDouble() / total.toDouble() * 100).toInt()
            val highRatio = (high.toDouble() / total.toDouble() * 100).toInt()

            // Update text labels
            binding.tvHospitalizationRatio.text = String.format(Locale.getDefault(), "Pacientes Hospitalizados: %d%% (%d/%d)", hospRatio, hospitalized, total)
            binding.tvHighPriorityRatio.text = String.format(Locale.getDefault(), "Pacientes Prioridad Alta: %d%% (%d/%d)", highRatio, high, total)

            // Update indicators with animation
            binding.progressHospitalizacion.setProgress(hospRatio, true)
            binding.progressPrioridadAlta.setProgress(highRatio, true)
        } else {
            binding.tvHospitalizationRatio.text = "Pacientes Hospitalizados: 0% (0/0)"
            binding.tvHighPriorityRatio.text = "Pacientes Prioridad Alta: 0% (0/0)"
            binding.progressHospitalizacion.setProgress(0, true)
            binding.progressPrioridadAlta.setProgress(0, true)
        }
    }
}
