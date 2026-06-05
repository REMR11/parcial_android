Aquí tienes el documento analizado y estructurado de forma limpia y legible en formato Markdown. Se han eliminado las cabeceras repetitivas de cada página de la Universidad Francisco Gaviria para que el agente de Antigravity pueda procesar los requerimientos técnicos directamente y sin ruido visual.

---

# Sistema Integral de Gestión Clínica con Historial Médico y Reportes Estadísticos

**Evaluación:** Parcial 4 (Ciclo 01-2026, Unidad 03) **Asignatura:** Desarrollo de Aplicaciones Móviles Básicas **Nivel:** Desarrollo de Aplicaciones Android - Nivel Avanzado **Docente:** Wilfredo Benjamin Magaña **Modalidad / Valor:** Individual / 100 puntos 

---

## 📋 Objetivo General

Desarrollar una aplicación Android profesional que permita administrar pacientes, gestionar historiales clínicos, generar estadísticas y exportar información utilizando arquitectura moderna, persistencia avanzada y navegación dinámica. 

## 🔍 Situación Problema

Una red de clínicas privadas necesita una aplicación móvil para gestionar la información de pacientes y sus consultas médicas, la cual debe cumplir con los siguientes módulos y requerimientos técnicos: 

---

## 🛠️ Especificaciones del Proyecto por Partes

### Parte 1: Autenticación con Roles

Crear una pantalla de acceso con control de usuarios basado en roles: 

* 
**Usuarios Válidos:** 


* 
`admin` / `12345` -> Rol: **Administrador** 


* 
`medico` / `med2026` -> Rol: **Médico** 




* 
**Reglas de Negocio:** 


* Si las credenciales son incorrectas: Mostrar el mensaje `"Acceso denegado"`. 


* Si son correctas: Mostrar mensaje de bienvenida indicando el rol (Ej: `"Bienvenido Administrador"` o `"Bienvenido Médico"`). 





### Parte 2: Registro Avanzado de Pacientes

El formulario de captura debe incluir la siguiente estructura de datos: 

* 
**Datos Personales:** Nombre completo, Edad, DUI, Teléfono, Dirección y Correo electrónico. 


* 
**Información Médica:** Tipo de sangre (mediante un `Spinner`), Peso, Estatura, Síntomas y Diagnóstico preliminar. 


* 
**Prioridad Médica:** Selección única entre *Baja*, *Media*, *Alta* o *Crítica*. 


* 
**Opciones Adicionales (`Checkbox`):** 


* Requiere hospitalización 


* Requiere exámenes 


* Paciente con enfermedades crónicas 





### Parte 3: Validaciones Complejas

Implementar lógica de validación estricta que impida guardar registros incompletos: 

* 
**Nombre:** Solo letras y espacios. 


* 
**Edad:** Rango admitido entre 0 y 120 años. 


* 
**DUI:** Formato estricto con guion `00000000-0`. 


* 
**Correo:** Formato de email válido. 


* 
**Peso y Estatura:** Valores numéricos estrictamente mayores que 0. 



### Parte 4: Pantalla de Confirmación

Enviar la información desde el formulario mediante un `Intent` explícito para mostrar un resumen antes del guardado definitivo: 

* 
**Datos a Visualizar:** Personales, médicos, prioridad y estado clínico. 


* 
**Generación Automatizada de Resumen:** Crear un string dinámico descriptivo. 


* 
*Ejemplo:* `"Paciente Ana López, prioridad crítica, requiere hospitalización y presenta antecedentes crónicos."` 




* 
**Controles Disponibles:** Botones para **Confirmar**, **Editar** y **Cancelar**. 


* 
**Flujo de Navegación:** Retornar la respuesta al formulario de origen utilizando la **Activity Result API**. 



### Parte 5: Room Database (Entidad Paciente)

Persistir la información utilizando Room en base al patrón arquitectónico MVVM con `LiveData`. 

* 
**Entidad `Paciente` (Campos):** 


* ID (Llave primaria autogenerada) 


* Nombre, Edad, DUI, Teléfono, Dirección, Correo 


* Tipo de sangre, Peso, Estatura, Síntomas, Diagnóstico, Prioridad 


* Hospitalización, Exámenes, Enfermedad crónica (`Boolean`) 


* Fecha de registro 




* 
**Componentes Requeridos:** DAO, Clase Database, Repository, ViewModel y LiveData. 



### Parte 6: Historial de Consultas Médicas (Relación One-To-Many)

Añadir soporte para un historial de consultas médicas vinculadas al paciente. 

* 
**Entidad `Consulta` (Campos):** 


* ID Consulta (Llave primaria) 


* ID Paciente (Firma/Llave foránea para la relación) 


* Fecha, Observaciones, Tratamiento, Próxima cita 




* 
**Relación Room:** Implementar un mapeo de **Uno a Muchos** (Un paciente puede registrar múltiples consultas). 



### Parte 7: Visualización con RecyclerView

Diseñar un listado principal optimizado para mostrar los registros guardados. 

* 
**Items del Layout:** Deben reflejar a primera vista el *Nombre*, *Prioridad*, *Fecha* y *Tipo de sangre*. 


* 
**Acciones por Registro:** Cada celda debe proveer opciones o interacciones para: 


* Ver detalle 


* Editar 


* Eliminar 


* Ver historial clínico (Consultas) 





### Parte 8: Búsqueda y Filtros Avanzados

Incluir un componente `SearchView` en la parte superior del listado que soporte: 

* 
**Búsqueda dinámica por texto:** Nombre, DUI o Correo. 


* **Filtros de categorización:**
* Por prioridades: Todas, Baja, Media, Alta, Crítica. 


* Por condición clínica: Hospitalizados o No hospitalizados. 





### Parte 9: Dashboard Estadístico

Crear una **Activity independiente** enfocada en métricas globales que integre componentes visuales como `CardView` (Material Design), `ProgressBar` y gráficos simples para desplegar: 

* Total de pacientes registrados 


* Cantidad de pacientes críticos 


* Cantidad de pacientes hospitalizados 


* Cantidad de pacientes con enfermedades crónicas 


* Total de consultas médicas registradas 



### Parte 10: Exportación Avanzada

Implementar lógica de lectura/escritura de archivos para generar reportes locales descargables: 

* 
**`pacientes.csv`:** Archivo delimitado por comas con las columnas de *Nombre, Edad, DUI, Prioridad, Fecha*. 


* 
**`consultas.txt`:** Archivo de texto plano con un resumen formateado y completo de las consultas guardadas. 


* 
**Mensaje de confirmación:** Notificar mediante un aviso en pantalla la cadena: `"Exportación completada exitosamente"`. 



### Parte 11: Funcionalidad Extra (Cálculo del IMC)

Desarrollar un algoritmo utilitario que calcule automáticamente el Índice de Masa Corporal. 

$$IMC = \frac{\text{Peso}}{\text{Estatura}^2}$$

* 
**Clasificación:** Debe categorizar el resultado en *Bajo peso*, *Normal*, *Sobrepeso* u *Obesidad*. 


* 
**Integración:** El valor numérico y su categoría deben mostrarse de forma automática al abrir la pantalla de detalle de cualquier paciente. 



---

## 🏗️ Requerimientos Técnicos Obligatorios

La construcción del software debe regirse exclusivamente por el siguiente stack de desarrollo: 

* 
**IDE:** Android Studio 


* 
**Lenguajes:** Java o Kotlin 


* 
**Arquitectura y Persistencia:** MVVM, Room Database, ViewModel, LiveData 


* 
**Componentes de UI:** RecyclerView, Material Design 3, CardView, ProgressBar 


* 
**Navegación / Flujo:** Intents explícitos, Activity Result API 



## 📦 Entregable

1. 
**Proyecto Android completo:** Empaquetado en un único archivo comprimido con extensión **`.zip`**.