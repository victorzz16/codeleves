# CodeLevels Spring Boot

Plataforma profesional de aprendizaje de lógica de programación desarrollada con:

- Java 21
- Spring Boot 3
- Spring Security
- Thymeleaf
- JavaScript
- PostgreSQL / pgAdmin
- JPA / Hibernate

## Roles del sistema

### Alumno
- Dashboard de aprendizaje.
- Retos interactivos con bloques.
- Validación de lógica.
- Traducción a código Java.
- Laboratorio libre.
- Registro de progreso en PostgreSQL.

### Docente
- Dashboard docente diferente al alumno.
- Métricas del aula.
- Lista de estudiantes y roles.
- Banco de retos.
- Seguimiento de progreso académico.

### Administrador
- Panel administrador propio.
- Gestión general de usuarios y roles.
- Auditoría de retos y progreso.
- Vista del estado técnico del sistema.
- Control global de la plataforma.

## Cuentas demo

Alumno:
- correo: estudiante@utp.edu.pe
- contraseña: 123456

Docente:
- correo: profesor@utp.edu.pe
- contraseña: 123456

Administrador:
- correo: admin@codelevels.pe
- contraseña: 123456

## Retos incluidos

El sistema carga más de 20 retos iniciales en PostgreSQL, organizados por dificultad y tema:

- Entrada y salida
- Condicionales
- Operaciones
- Bucles
- Switch
- Arreglos
- Métodos
- Matrices

## Configuración de base de datos

1. Abre pgAdmin.
2. Crea una base de datos llamada:

```sql
CREATE DATABASE codelevels_db;
```

3. Revisa el archivo:

```text
src/main/resources/application.properties
```

Por defecto usa:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/codelevels_db
spring.datasource.username=postgres
spring.datasource.password=admin
```

Cambia la contraseña si tu PostgreSQL usa otra.

## Cómo ejecutar

Desde la raíz del proyecto:

```bash
mvn spring-boot:run
```

Luego abre:

```text
http://localhost:8080
```

## Estructura principal

```text
src/main/java/com/codelevels
├── config          # Seguridad y datos iniciales
├── controller      # Controladores MVC y API REST
├── model           # Entidades JPA
├── repository      # Repositorios Spring Data JPA
└── service         # Servicio de usuarios para Spring Security

src/main/resources
├── templates       # Thymeleaf HTML
│   ├── auth
│   ├── alumno
│   ├── docente
│   └── admin
└── static
    ├── css
    └── js
```

## Nota importante

Si ya ejecutaste una versión anterior y solo te aparecen pocos retos, puedes borrar las tablas desde pgAdmin o eliminar la base `codelevels_db` y crearla nuevamente. Esta versión carga automáticamente los retos faltantes por título, pero si hiciste cambios manuales en la base, reiniciar la base garantiza una carga limpia.

## Nuevo módulo: creación de retos desde el administrador

El rol **ADMIN** ahora puede agregar retos nuevos desde la interfaz web.

Ruta:

```text
http://localhost:8080/admin/dashboard#nuevo-reto
```

Cuenta demo:

```text
admin@codelevels.pe
123456
```

Para crear un reto se deben completar:

- Título del reto.
- Dificultad.
- Categoría.
- Descripción.
- Bloques disponibles, un bloque por línea.
- Orden correcto usando índices separados por coma. Ejemplo: `0,1,2,3,4`.
- Pistas, una pista por línea.
- Código Java que se mostrará al alumno cuando resuelva el reto.

Al guardar, el reto queda registrado en PostgreSQL y aparece automáticamente en el panel de retos del alumno.


## Perfil profesional por rol

El sistema incluye el módulo `/perfil`, disponible para ALUMNO, DOCENTE y ADMIN. Cada usuario puede editar nombre visible, descripción, carrera/área, sede, URL de foto y URL de portada. El perfil muestra estadísticas y accesos rápidos según el rol activo.

## Accesibilidad agregada

La plataforma incluye un botón flotante **Accesibilidad** disponible en login, panel alumno, docente, administrador, perfil, retos, tutor, progreso y exámenes.

Opciones disponibles:

- **Modo daltónico:** usa una paleta más segura para daltonismo y agrega indicadores no basados solo en color.
- **Alto contraste:** mejora lectura para usuarios con baja visión.
- **Texto grande:** aumenta el tamaño de lectura.
- **Reducir animaciones:** evita movimientos innecesarios.
- **Subrayar enlaces:** facilita reconocer enlaces y foco del teclado.

Atajo de teclado: `Alt + A` para abrir el panel de accesibilidad.

## Nuevos módulos agregados

### Panel docente
El docente ahora puede trabajar desde `/docente/dashboard` con nuevos módulos:

- **Asignar tareas** a todo el salón o a un alumno específico.
- **Crear exámenes** con dificultad Básico, Intermedio o Avanzado.
- **Enviar solicitudes de nuevos retos** al administrador.
- **Consultar el estado de sus solicitudes**: Pendiente, Aprobada o Rechazada.

### Panel administrador
El administrador ahora cuenta con el apartado **Solicitudes de retos** en `/admin/dashboard#solicitudes-retos`.
Desde allí puede revisar las solicitudes enviadas por docentes, aprobarlas, rechazarlas y dejar una respuesta.

### Modo oscuro
El panel flotante de **Accesibilidad** ahora incluye la opción **Modo oscuro**. Esta preferencia se guarda en el navegador usando `localStorage`.

## Módulo de exámenes con revisión docente y archivos

Se agregó un flujo completo para exámenes creados por docentes:

- El docente puede crear exámenes por dificultad: Básico, Intermedio y Avanzado.
- Al crear el examen puede adjuntar un PDF o imagen con preguntas.
- El alumno puede ver el examen, abrir el archivo de preguntas y enviar su desarrollo.
- El alumno puede subir su examen resuelto en Word (`.doc`, `.docx`) o PDF.
- El docente recibe las entregas en el apartado **Revisar exámenes**.
- El docente coloca calificación de 0 a 20 y retroalimentación.
- El alumno recibe una notificación cuando el examen está calificado.
- El resultado calificado puede exportarse en PDF usando el botón **Exportar PDF**.

Los archivos cargados se guardan en la carpeta local `uploads/` del proyecto y se sirven desde `/uploads/**`.

## Plan Premium Pro (simulación académica)
- Precio mensual: S/ 30.00.
- Métodos simulados: Yape, Plin, Pago Efectivo y tarjeta.
- Endpoint interno: `POST /api/pagos/simular`.
- Al confirmar, se registra la operación en PostgreSQL, se activa Premium por un mes y se desbloquean retos Pro.
- No consume una pasarela real ni realiza cobros reales; es una API interna para demostración académica.


## Retos Premium alineados al sílabo UTP
Se incorporaron retos adicionales basados en las tres unidades del curso Principios de Algoritmos: estructura secuencial, estructuras condicionales y estructuras repetitivas. Incluyen lógica proposicional, tipos de datos, variables, diagramas de flujo, condicional simple/doble/anidada/múltiple, PARA, MIENTRAS y REPETIR-HASTA QUE.
