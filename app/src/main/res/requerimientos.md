# Requerimientos — Trabajo Práctico en Grupo (Nivel Avanzado)
**Curso:** Desarrollo de Aplicaciones Android  
**Universidad:** Francisco Gavidia (UFG)  
**Tema:** Gestión Integral de Pacientes con Persistencia Avanzada y Navegación Dinámica

---

## Objetivo General

Desarrollar una aplicación Android robusta para administrar registros clínicos, aplicando interacción avanzada entre actividades, persistencia local estructurada, edición dinámica y exportación de información.

---

## Competencias a Desarrollar

- Diseñar aplicaciones Android modulares.
- Implementar navegación avanzada entre múltiples actividades.
- Capturar y retornar resultados usando **Activity Result API**.
- Aplicar persistencia avanzada con **Room Database**.
- Implementar **RecyclerView** con filtros dinámicos.
- Exportar información almacenada.
- Aplicar validaciones complejas de entrada de datos.
- Diseñar interfaces siguiendo **Material Design 3**.

---

## Arquitectura Requerida

**Patrón:** MVVM (Model-View-ViewModel)  
**Capas:** Entity → DAO → Database → Repository → ViewModel → UI

---

## Partes del Trabajo

### PARTE 1 — Pantalla de Acceso (LoginActivity)

**Componentes UI:**
- Campo de texto: Usuario
- Campo de texto: Contraseña
- Botón: Ingresar

**Lógica de validación:**

| Campo | Valor válido |
|---|---|
| Usuario | `admin` |
| Contraseña | `12345` |

- Si las credenciales son incorrectas → mostrar mensaje: `"Credenciales incorrectas"`
- Si son correctas → redirigir a la pantalla principal

---

### PARTE 2 — Registro Avanzado de Pacientes (MainActivity / RegistroActivity)

**Datos personales (campos de texto):**
- Nombre completo
- Edad
- DUI
- Teléfono
- Dirección

**Información médica:**
- Síntomas (campo de texto)
- Diagnóstico preliminar (campo de texto)
- Nivel de prioridad (Spinner):
    - Baja
    - Media
    - Alta

**Opciones adicionales (CheckBox):**
- Requiere hospitalización
- Requiere exámenes

**Botón de acción:**
- `Enviar para validación` → lanza la actividad de confirmación

---

### PARTE 3 — Validación en Segunda Actividad (ConfirmacionActivity)

**Flujo:**
- Recibe todos los datos vía **Intent explícito**
- Muestra todos los datos del paciente
- Genera automáticamente un **resumen clínico**

**Ejemplo de resumen:**
> "Paciente Juan Pérez, prioridad alta, requiere hospitalización."

**Botones disponibles:**

| Botón | Acción |
|---|---|
| Confirmar registro | Guarda en Room y retorna resultado OK |
| Editar datos | Retorna para modificar datos |
| Cancelar | Descarta el registro |

**Retorno de resultado:** usar `ActivityResultLauncher` (Activity Result API)

---

### PARTE 4 — Persistencia Avanzada (Room Database)

**Entidad:** `Paciente`

| Campo | Tipo | Detalle |
|---|---|---|
| id | Int | Autogenerado (PrimaryKey) |
| nombre | String | — |
| edad | Int | — |
| dui | String | — |
| telefono | String | — |
| direccion | String | — |
| sintomas | String | — |
| diagnostico | String | — |
| prioridad | String | Baja / Media / Alta |
| hospitalizacion | Boolean | — |
| examenes | Boolean | — |
| fechaHoraRegistro | String | Timestamp de creación |

**Componentes Room a implementar:**
- `PacienteDao` — operaciones CRUD + consultas de búsqueda/filtrado
- `AppDatabase` — instancia singleton de la base de datos
- `PacienteRepository` — abstracción entre DAO y ViewModel
- `PacienteViewModel` — expone LiveData a la UI

---

### PARTE 5 — Visualización Profesional (RecyclerView)

**Lista de pacientes mostrando:**
- Nombre
- Edad
- Prioridad (con color diferenciado recomendado)
- Fecha de registro

**Acciones por cada ítem:**
- Editar
- Eliminar
- Ver detalle completo

---

### PARTE 6 — Búsqueda y Filtrado Dinámico

**SearchView** para buscar en tiempo real por:
- Nombre
- DUI

**Filtros por prioridad** (Chips o Spinner):
- Todos
- Baja
- Media
- Alta

---
admi
### PARTE 7 — Exportación de Datos

**Botón:** `Exportar registros`

**Genera archivo:** `pacientes.csv`

**Estructura del CSV:**
```
Nombre,Edad,DUI,Prioridad,Fecha
```

- Guardar en almacenamiento interno de la app
- Mostrar Toast/Snackbar: `"Archivo exportado correctamente"`

---

### PARTE 8 — Dashboard de Estadísticas (DashboardActivity)

**Estadísticas a mostrar:**
- Total de pacientes registrados
- Pacientes con prioridad alta
- Pacientes que requieren hospitalización
- Pacientes registrados hoy

**Componentes UI sugeridos:**
- Cards de Material Design 3
- ProgressBar
- Gráficos simples

---

## Requerimientos Técnicos Obligatorios

| Tecnología / Concepto | Uso |
|---|---|
| Kotlin o Java | Lenguaje de desarrollo |
| Android Studio | IDE |
| Intents explícitos | Navegación entre Activities |
| Activity Result API | Retorno de resultados |
| Room Database | Persistencia local |
| RecyclerView + Adapter | Listado de pacientes |
| ViewModel | Gestión del estado de UI |
| LiveData | Observación reactiva de datos |
| Material Design 3 | Componentes y estilos de UI |
| Arquitectura MVVM | Separación de responsabilidades |

---

## Actividades del Proyecto

| # | Actividad | Responsabilidad |
|---|---|---|
| 1 | `LoginActivity` | Autenticación local |
| 2 | `RegistroActivity` | Formulario de nuevo paciente |
| 3 | `ConfirmacionActivity` | Revisión y confirmación de datos |
| 4 | `MainActivity` (lista) | RecyclerView + búsqueda + filtros |
| 5 | `DetalleActivity` | Vista completa de un paciente |
| 6 | `DashboardActivity` | Estadísticas y métricas |

---

## Entregables

### 1. Código Fuente
- Proyecto Android Studio comprimido en `.zip`

### 2. Documento Técnico
Debe incluir:
- Portada
- Introducción
- Objetivos
- Arquitectura implementada (diagrama MVVM)
- Explicación de cada Activity
- Explicación de Room Database
- Flujo de navegación
- Capturas de pantalla
- Problemas encontrados y soluciones aplicadas
- Conclusiones grupales

### 3. Video Demostrativo (5–8 minutos)
Debe mostrar en funcionamiento:
- Login
- Registro de paciente
- Validación en segunda actividad
- Guardado en base de datos
- Edición de registro
- Eliminación de registro
- Búsqueda y filtrado
- Exportación CSV
- Dashboard de estadísticas

---

## Resultado Esperado

Al finalizar, los estudiantes serán capaces de desarrollar aplicaciones Android similares a sistemas reales empresariales, aplicando arquitectura moderna (MVVM), persistencia avanzada con Room y una experiencia de usuario profesional con Material Design 3.