package ufg.edu.sv

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ufg.edu.sv.data.Paciente
import ufg.edu.sv.databinding.ActivityRegistroBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private var editingPaciente: Paciente? = null

    // Register Activity Result API for Confirmation flow
    private val confirmationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Confirmation saved data successfully in Room, return OK to MainActivity
            setResult(RESULT_OK)
            finish()
        } else if (result.resultCode == 99) {
            // Discard everything, return to MainActivity
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // Setup Priority Spinner
        val priorities = arrayOf("Baja", "Media", "Alta")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        binding.spinnerPrioridad.adapter = spinnerAdapter

        // Check if editing existing patient
        editingPaciente = intent.getSerializableExtra("paciente") as? Paciente
        if (editingPaciente != null) {
            binding.toolbar.title = "Editar Paciente"
            populateFields(editingPaciente!!)
        }

        // Add auto-formatting text watchers for DUI and Phone to enhance user experience
        setupFormatters()

        // Submit Button Click
        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                val currentTimestamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                
                // Pack details into a Paciente instance
                val patientData = Paciente(
                    id = editingPaciente?.id ?: 0, // Keep existing ID if editing
                    nombre = binding.etNombre.text.toString().trim(),
                    edad = binding.etEdad.text.toString().trim().toInt(),
                    dui = binding.etDui.text.toString().trim(),
                    telefono = binding.etTelefono.text.toString().trim(),
                    direccion = binding.etDireccion.text.toString().trim(),
                    sintomas = binding.etSintomas.text.toString().trim(),
                    diagnostico = binding.etDiagnostico.text.toString().trim(),
                    prioridad = binding.spinnerPrioridad.selectedItem.toString(),
                    hospitalizacion = binding.cbHospitalizacion.isChecked,
                    examenes = binding.cbExamenes.isChecked,
                    fechaHoraRegistro = editingPaciente?.fechaHoraRegistro ?: currentTimestamp
                )

                // Launch ConfirmationActivity
                val intent = Intent(this, ConfirmacionActivity::class.java).apply {
                    putExtra("paciente", patientData)
                    putExtra("isEditing", editingPaciente != null)
                }
                confirmationLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Por favor complete los campos obligatorios marcados con *", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateFields(p: Paciente) {
        binding.etNombre.setText(p.nombre)
        binding.etEdad.setText(p.edad.toString())
        binding.etDui.setText(p.dui)
        binding.etTelefono.setText(p.telefono)
        binding.etDireccion.setText(p.direccion)
        binding.etSintomas.setText(p.sintomas)
        binding.etDiagnostico.setText(p.diagnostico)
        
        val priorityIndex = when (p.prioridad) {
            "Baja" -> 0
            "Media" -> 1
            "Alta" -> 2
            else -> 0
        }
        binding.spinnerPrioridad.setSelection(priorityIndex)
        binding.cbHospitalizacion.isChecked = p.hospitalizacion
        binding.cbExamenes.isChecked = p.examenes
    }

    private fun setupFormatters() {
        // DUI Auto-formatting (XXXXXXXX-X)
        binding.etDui.addTextChangedListener(object : TextWatcher {
            var isDeleting = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (!isDeleting && text.length == 8) {
                    binding.etDui.append("-")
                }
            }
        })

        // Phone Auto-formatting (XXXX-XXXX)
        binding.etTelefono.addTextChangedListener(object : TextWatcher {
            var isDeleting = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (!isDeleting && text.length == 4) {
                    binding.etTelefono.append("-")
                }
            }
        })
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Nombre Validation
        val nombre = binding.etNombre.text.toString().trim()
        if (nombre.isEmpty()) {
            binding.tilNombre.error = "El nombre es obligatorio"
            isValid = false
        } else {
            binding.tilNombre.error = null
        }

        // Edad Validation
        val edadText = binding.etEdad.text.toString().trim()
        if (edadText.isEmpty()) {
            binding.tilEdad.error = "Requerido"
            isValid = false
        } else {
            val edad = edadText.toIntOrNull()
            if (edad == null || edad <= 0 || edad > 125) {
                binding.tilEdad.error = "Edad inválida"
                isValid = false
            } else {
                binding.tilEdad.error = null
            }
        }

        // DUI Validation (El Salvador DUI format: 8 digits + dash + 1 digit)
        val dui = binding.etDui.text.toString().trim()
        val duiRegex = Regex("^\\d{8}-\\d{1}$")
        if (dui.isEmpty()) {
            binding.tilDui.error = "Requerido"
            isValid = false
        } else if (!dui.matches(duiRegex)) {
            binding.tilDui.error = "Formato: XXXXXXXX-X"
            isValid = false
        } else {
            binding.tilDui.error = null
        }

        // Telefono Validation (El Salvador phone format: 4 digits + dash + 4 digits)
        val telefono = binding.etTelefono.text.toString().trim()
        val phoneRegex = Regex("^\\d{4}-\\d{4}$")
        if (telefono.isEmpty()) {
            binding.tilTelefono.error = "El teléfono es obligatorio"
            isValid = false
        } else if (!telefono.matches(phoneRegex)) {
            binding.tilTelefono.error = "Formato: XXXX-XXXX"
            isValid = false
        } else {
            binding.tilTelefono.error = null
        }

        // Direccion Validation
        val direccion = binding.etDireccion.text.toString().trim()
        if (direccion.isEmpty()) {
            binding.tilDireccion.error = "La dirección es obligatoria"
            isValid = false
        } else {
            binding.tilDireccion.error = null
        }

        // Sintomas Validation
        val sintomas = binding.etSintomas.text.toString().trim()
        if (sintomas.isEmpty()) {
            binding.tilSintomas.error = "Los síntomas son obligatorios"
            isValid = false
        } else {
            binding.tilSintomas.error = null
        }

        // Diagnostico Validation
        val diagnostico = binding.etDiagnostico.text.toString().trim()
        if (diagnostico.isEmpty()) {
            binding.tilDiagnostico.error = "El diagnóstico es obligatorio"
            isValid = false
        } else {
            binding.tilDiagnostico.error = null
        }

        return isValid
    }
}
