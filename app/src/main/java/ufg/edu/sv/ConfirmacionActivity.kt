package ufg.edu.sv

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ufg.edu.sv.data.Paciente
import ufg.edu.sv.databinding.ActivityConfirmacionBinding
import ufg.edu.sv.ui.PacienteViewModel

class ConfirmacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmacionBinding
    private lateinit var viewModel: PacienteViewModel
    private var paciente: Paciente? = null
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewModel
        viewModel = ViewModelProvider(this)[PacienteViewModel::class.java]

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)

        // Retrieve patient extra
        paciente = intent.getSerializableExtra("paciente") as? Paciente
        isEditing = intent.getBooleanExtra("isEditing", false)

        if (paciente == null) {
            Toast.makeText(this, "Error al cargar los datos del paciente", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Bind data to screen
        bindPatientData(paciente!!)

        // Generate Clinical Summary
        generateClinicalSummary(paciente!!)

        // Buttons Action Listeners
        
        // 1. Confirm: Save in Room (Insert or Update) and return RESULT_OK to close everything
        binding.btnConfirm.setOnClickListener {
            val p = paciente!!
            if (isEditing) {
                viewModel.updatePaciente(p) {
                    runOnUiThread {
                        Toast.makeText(this, "Expediente actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            } else {
                viewModel.insertPaciente(p) { newId ->
                    // Done inserting
                    runOnUiThread {
                        Toast.makeText(this, "Expediente guardado exitosamente", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }

        // 2. Edit: Return to RegistroActivity. RegistroActivity is still in the stack, so just finish()
        binding.btnEdit.setOnClickListener {
            finish() // This returns back to RegistroActivity with all inputs intact
        }

        // 3. Cancel: Discard everything. Return a specific result code so RegistroActivity also finishes
        binding.btnCancel.setOnClickListener {
            setResult(99) // Special result code for discarding
            finish()
        }
    }

    private fun bindPatientData(p: Paciente) {
        binding.tvConfirmNombre.text = "Nombre: ${p.nombre}"
        binding.tvConfirmEdad.text = "Edad: ${p.edad} años"
        binding.tvConfirmDui.text = "DUI: ${p.dui}"
        binding.tvConfirmTelefono.text = "Teléfono: ${p.telefono}"
        binding.tvConfirmCorreo.text = "Correo: ${p.correo}"
        binding.tvConfirmDireccion.text = "Dirección: ${p.direccion}"
        binding.tvConfirmTipoSangre.text = "Tipo de Sangre: ${p.tipoSangre}"
        binding.tvConfirmPeso.text = "Peso: ${p.peso} kg"
        binding.tvConfirmEstatura.text = "Estatura: ${p.estatura} m"
        binding.tvConfirmSintomas.text = "Síntomas: ${p.sintomas}"
        binding.tvConfirmDiagnostico.text = "Diagnóstico: ${p.diagnostico}"
        binding.tvConfirmPrioridad.text = "Prioridad: ${p.prioridad.uppercase()}"
        binding.tvConfirmHospitalizacion.text = "Hospitalización: ${if (p.hospitalizacion) "Sí" else "No"}"
        binding.tvConfirmExamenes.text = "Exámenes de Laboratorio: ${if (p.examenes) "Sí" else "No"}"
        binding.tvConfirmCronico.text = "Enf. Crónica: ${if (p.enfermedadCronica) "Sí" else "No"}"
    }

    private fun generateClinicalSummary(p: Paciente) {
        val hospText = if (p.hospitalizacion) "requiere hospitalización inmediata" else "no requiere hospitalización"
        val examText = if (p.examenes) "requiere exámenes de laboratorio" else "no requiere exámenes adicionales"
        val cronicoText = if (p.enfermedadCronica) " y presenta antecedentes de enfermedades crónicas." else "."
        
        val summary = "Paciente ${p.nombre}, de ${p.edad} años. Registra prioridad ${p.prioridad.lowercase()} con síntomas de (${p.sintomas}) y diagnóstico preliminar de (${p.diagnostico}). Se determina que el paciente $hospText, $examText$cronicoText"
        
        binding.tvResumenClinico.text = summary
    }
}
