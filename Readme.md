# 🎬 Gestión de Series de Televisión en Plataformas

Aplicación Java para administrar una base de datos de series disponibles en plataformas de streaming. Integra **Programación Orientada a Objetos (POO)**, conexión con **Oracle DB mediante JDBC** y una interfaz gráfica moderna con **Java Swing** y **FlatLaf**.

---

## 🎯 Objetivo General

Desarrollar una aplicación Java completa que permita **gestionar series de televisión** mediante una base de datos relacional, aplicando los pilares del desarrollo orientado a objetos, arquitectura **MVC**, y diseño de interfaz gráfica amigable.

---

## ✅ Requisitos Funcionales

### 🔹 Programación Orientada a Objetos (POO)
- Uso de **encapsulamiento**, **herencia**, **polimorfismo** y **abstracción**.
- Clases de dominio como `Serie`, `Plataforma`, etc.
- Aplicación del **patrón MVC** (Modelo-Vista-Controlador).

### 🔹 Conexión con Oracle DB
- Conexión JDBC.
- Las tablas se crean automáticamente si no existen (con permisos adecuados).

### 🔹 Base de Datos (Modelo Relacional mínimo)
- **Tabla `Serie`**:
  - `id` (PK), `titulo`, `genero`, `temporadas`, `año`, `id_plataforma` (FK)
- **Tabla `Plataforma`**:
  - `id` (PK), `nombre`, `pais_origen`
- Relación: Una plataforma puede contener muchas series (1:N).

### 🔹 Interfaz Gráfica con Swing
- Formularios para alta y edición de series/plataformas con **validaciones**.
- Operaciones **CRUD completas** desde la interfaz.
- Estilo visual moderno con la librería [FlatLaf](https://www.formdev.com/flatlaf/).

---

## 📦 Estructura del Proyecto (Paquetes)

```text
com.proyecto.gestionseriestv
├── app            → Clase principal (MainApp.java)
├── modelo         → POJOs: Serie, Plataforma
├── controlador    → Lógica de negocio (SerieController, PlataformaController)
├── persistencia   → Acceso a BD (GestorBaseDatos.java)
└── vista          → GUI (VentanaPrincipal, FormularioSerie, etc.)
```

---

## 🛠️ Tecnologías Utilizadas

| Herramienta        | Versión / Info                  |
|--------------------|---------------------------------|
| ☕ Java             | JDK 17 (recomendado)            |
| 🧱 Maven           | Gestor de dependencias          |
| 🗃️ Oracle Database | Conexión vía JDBC (ojdbc11)     |
| 🖥️ Swing + FlatLaf | Interfaz gráfica moderna        |
| 🐙 Git + GitHub    | Control de versiones            |

---

## 🚀 Cómo Ejecutar el Proyecto

### 1. 📋 Prerrequisitos
- Java JDK instalado (v17+ recomendado)
- Maven instalado
- Oracle Database accesible
- Usuario Oracle con permisos de creación de tablas

### 2. 📦 Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/gestion-series-tv.git
cd gestion-series-tv
```

### 3. ⚙️ Compilar el proyecto

```bash
mvn clean install
```

### 4. ▶️ Ejecutar la aplicación

```bash
java -jar target/gestion-series-tv-0.0.1-SNAPSHOT.jar
```

_O bien ejecutar `MainApp.java` desde Eclipse._

---



## 👤 Autor

- **[Adrian Muñoz]**  


---
