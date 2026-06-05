package ufg.edu.sv.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ufg.edu.sv.R
import ufg.edu.sv.data.Paciente
import ufg.edu.sv.databinding.ItemPacienteBinding

class PacienteAdapter(
    private val onDetalleClick: (Paciente) -> Unit,
    private val onEditarClick: (Paciente) -> Unit,
    private val onEliminarClick: (Paciente) -> Unit
) : ListAdapter<Paciente, PacienteAdapter.PacienteViewHolder>(PacienteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val binding = ItemPacienteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PacienteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = getItem(position)
        holder.bind(paciente)
    }

    inner class PacienteViewHolder(private val binding: ItemPacienteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(paciente: Paciente) {
            binding.tvPacienteNombre.text = paciente.nombre
            binding.tvPacienteInfo.text = "Edad: ${paciente.edad} años  •  DUI: ${paciente.dui}"
            binding.tvPacienteFecha.text = "Registrado: ${paciente.fechaHoraRegistro}"
            binding.tvPacientePrioridad.text = paciente.prioridad.uppercase()

            // Visual Styling depending on priority
            val context = binding.root.context
            when (paciente.prioridad.lowercase()) {
                "baja" -> {
                    binding.cardPriorityBadge.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.priority_low_bg)
                    )
                    binding.tvPacientePrioridad.setTextColor(
                        ContextCompat.getColor(context, R.color.priority_low_text)
                    )
                }
                "media" -> {
                    binding.cardPriorityBadge.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.priority_medium_bg)
                    )
                    binding.tvPacientePrioridad.setTextColor(
                        ContextCompat.getColor(context, R.color.priority_medium_text)
                    )
                }
                "alta" -> {
                    binding.cardPriorityBadge.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.priority_high_bg)
                    )
                    binding.tvPacientePrioridad.setTextColor(
                        ContextCompat.getColor(context, R.color.priority_high_text)
                    )
                }
                else -> {
                    binding.cardPriorityBadge.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.priority_low_bg)
                    )
                    binding.tvPacientePrioridad.setTextColor(
                        ContextCompat.getColor(context, R.color.priority_low_text)
                    )
                }
            }

            // Click Listeners
            binding.btnItemDetalle.setOnClickListener { onDetalleClick(paciente) }
            binding.btnItemEditar.setOnClickListener { onEditarClick(paciente) }
            binding.btnItemEliminar.setOnClickListener { onEliminarClick(paciente) }
        }
    }

    class PacienteDiffCallback : DiffUtil.ItemCallback<Paciente>() {
        override fun areItemsTheSame(oldItem: Paciente, newItem: Paciente): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Paciente, newItem: Paciente): Boolean {
            return oldItem == newItem
        }
    }
}
