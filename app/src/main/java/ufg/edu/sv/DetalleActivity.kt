package ufg.edu.sv

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ufg.edu.sv.data.Paciente
import ufg.edu.sv.databinding.ActivityDetalleBinding

class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // Retrieve patient extra
        val paciente = intent.getSerializableExtra("paciente") as? Paciente
        if (paciente == null) {
            Toast.makeText(this, "Error al cargar la ficha del paciente", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Display details
        bindDetails(paciente)
    }

    private fun bindDetails(p: Paciente) {
        binding.tvDetailNombre.text = p.nombre
        binding.tvDetailEdad.text = "Edad: ${p.edad} años"
        binding.tvDetailDui.text = "DUI: ${p.dui}"
        binding.tvDetailTelefono.text = "Teléfono: ${p.telefono}"
        binding.tvDetailDireccion.text = "Dirección: ${p.direccion}"
        binding.tvDetailSintomas.text = "Síntomas: ${p.sintomas}"
        binding.tvDetailDiagnostico.text = "Diagnóstico Preliminar: ${p.diagnostico}"
        binding.tvDetailHospitalizacion.text = "Hospitalización: ${if (p.hospitalizacion) "Sí (Requiere Inmediata)" else "No requiere"}"
        binding.tvDetailExamenes.text = "Exámenes de Laboratorio: ${if (p.examenes) "Sí (Requerido)" else "No requiere"}"
        binding.tvDetailFecha.text = "Registrado el: ${p.fechaHoraRegistro}"
        
        binding.tvDetailPrioridad.text = "PRIORIDAD: ${p.prioridad.uppercase()}"

        // Stylize priority banner
        when (p.prioridad.lowercase()) {
            "baja" -> {
                binding.viewPriorityBanner.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.priority_low_bg)
                )
                binding.tvDetailPrioridad.setTextColor(
                    ContextCompat.getColor(this, R.color.priority_low_text)
                )
            }
            "media" -> {
                binding.viewPriorityBanner.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.priority_medium_bg)
                )
                binding.tvDetailPrioridad.setTextColor(
                    ContextCompat.getColor(this, R.color.priority_medium_text)
                )
            }
            "alta" -> {
                binding.viewPriorityBanner.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.priority_high_bg)
                )
                binding.tvDetailPrioridad.setTextColor(
                    ContextCompat.getColor(this, R.color.priority_high_text)
                )
            }
            else -> {
                binding.viewPriorityBanner.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.priority_low_bg)
                )
                binding.tvDetailPrioridad.setTextColor(
                    ContextCompat.getColor(this, R.color.priority_low_text)
                )
            }
        }
    }
}
