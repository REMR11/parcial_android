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

    private val confirmationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        } else if (result.resultCode == 99) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // Ajustado a los requerimientos: Baja, Media, Alta
        val priorities = arrayOf("Baja", "Media", "Alta")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        binding.spinnerPrioridad.adapter = spinnerAdapter

        val bloodTypes = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val bloodAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bloodTypes)
        binding.spinnerTipoSangre.adapter = bloodAdapter

        editingPaciente = intent.getSerializableExtra("paciente") as? Paciente
        if (editingPaciente != null) {
            binding.toolbar.title = "Editar Paciente"
            populateFields(editingPaciente!!)
        }

        setupFormatters()

        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                val currentTimestamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                
                val patientData = Paciente(
                    id = editingPaciente?.id ?: 0,
                    nombre = binding.etNombre.text.toString().trim(),
                    edad = binding.etEdad.text.toString().trim().toInt(),
                    dui = binding.etDui.text.toString().trim(),
                    telefono = binding.etTelefono.text.toString().trim(),
                    direccion = binding.etDireccion.text.toString().trim(),
                    correo = binding.etCorreo.text.toString().trim(),
                    tipoSangre = binding.spinnerTipoSangre.selectedItem.toString(),
                    peso = binding.etPeso.text.toString().trim().toDouble(),
                    estatura = binding.etEstatura.text.toString().trim().toDouble(),
                    sintomas = binding.etSintomas.text.toString().trim(),
                    diagnostico = binding.etDiagnostico.text.toString().trim(),
                    prioridad = binding.spinnerPrioridad.selectedItem.toString(),
                    hospitalizacion = binding.cbHospitalizacion.isChecked,
                    examenes = binding.cbExamenes.isChecked,
                    enfermedadCronica = binding.cbCronico.isChecked,
                    fechaHoraRegistro = editingPaciente?.fechaHoraRegistro ?: currentTimestamp
                )

                val intent = Intent(this, ConfirmacionActivity::class.java).apply {
                    putExtra("paciente", patientData)
                    putExtra("isEditing", editingPaciente != null)
                }
                confirmationLauncher.launch(intent)
            }
        }
    }

    private fun populateFields(p: Paciente) {
        binding.etNombre.setText(p.nombre)
        binding.etEdad.setText(p.edad.toString())
        binding.etDui.setText(p.dui)
        binding.etTelefono.setText(p.telefono)
        binding.etDireccion.setText(p.direccion)
        binding.etCorreo.setText(p.correo)
        binding.etPeso.setText(p.peso.toString())
        binding.etEstatura.setText(p.estatura.toString())
        binding.etSintomas.setText(p.sintomas)
        binding.etDiagnostico.setText(p.diagnostico)
        
        val priorityIndex = when (p.prioridad) {
            "Baja" -> 0
            "Media" -> 1
            "Alta" -> 2
            else -> 0
        }
        binding.spinnerPrioridad.setSelection(priorityIndex)

        val bloodTypes = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val bloodIndex = bloodTypes.indexOf(p.tipoSangre).takeIf { it >= 0 } ?: 0
        binding.spinnerTipoSangre.setSelection(bloodIndex)

        binding.cbHospitalizacion.isChecked = p.hospitalizacion
        binding.cbExamenes.isChecked = p.examenes
        binding.cbCronico.isChecked = p.enfermedadCronica
    }

    private fun setupFormatters() {
        binding.etDui.addTextChangedListener(object : TextWatcher {
            private var isRunning = false
            private var isDeleting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isRunning) return
                isRunning = true

                val text = s.toString()
                if (!isDeleting && text.length == 8) {
                    s?.append("-")
                } else if (isDeleting && text.length == 9 && text.endsWith("-")) {
                    // Si se borra el guion, dejar que se borre
                }

                isRunning = false
            }
        })

        binding.etTelefono.addTextChangedListener(object : TextWatcher {
            private var isRunning = false
            private var isDeleting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isRunning) return
                isRunning = true

                val text = s.toString()
                if (!isDeleting && text.length == 4) {
                    s?.append("-")
                }

                isRunning = false
            }
        })
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val nombre = binding.etNombre.text.toString().trim()
        if (nombre.isEmpty()) {
            binding.tilNombre.error = "El nombre es obligatorio"
            isValid = false
        } else {
            binding.tilNombre.error = null
        }

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

        val direccion = binding.etDireccion.text.toString().trim()
        if (direccion.isEmpty()) {
            binding.tilDireccion.error = "La dirección es obligatoria"
            isValid = false
        } else {
            binding.tilDireccion.error = null
        }

        val correo = binding.etCorreo.text.toString().trim()
        if (correo.isEmpty()) {
            binding.tilCorreo.error = "El correo es obligatorio"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.tilCorreo.error = "Correo inválido"
            isValid = false
        } else {
            binding.tilCorreo.error = null
        }

        val pesoText = binding.etPeso.text.toString().trim()
        if (pesoText.isEmpty()) {
            binding.tilPeso.error = "Requerido"
            isValid = false
        } else {
            val peso = pesoText.toDoubleOrNull()
            if (peso == null || peso <= 0) {
                binding.tilPeso.error = "Peso > 0"
                isValid = false
            } else {
                binding.tilPeso.error = null
            }
        }

        val estaturaText = binding.etEstatura.text.toString().trim()
        if (estaturaText.isEmpty()) {
            binding.tilEstatura.error = "Requerido"
            isValid = false
        } else {
            val estatura = estaturaText.toDoubleOrNull()
            if (estatura == null || estatura <= 0) {
                binding.tilEstatura.error = "Estatura > 0"
                isValid = false
            } else {
                binding.tilEstatura.error = null
            }
        }

        val sintomas = binding.etSintomas.text.toString().trim()
        if (sintomas.isEmpty()) {
            binding.tilSintomas.error = "Los síntomas son obligatorios"
            isValid = false
        } else {
            binding.tilSintomas.error = null
        }

        val diagnostico = binding.etDiagnostico.text.toString().trim()
        if (diagnostico.isEmpty()) {
            binding.tilDiagnostico.error = "El diagnóstico es obligatorio"
            isValid = false
        } else {
            binding.tilDiagnostico.error = null
        }

        if (!isValid) {
            Toast.makeText(this, "Por favor complete los campos obligatorios marcados con *", Toast.LENGTH_SHORT).show()
        }

        return isValid
    }
}