# AndinaSalud

Aplicacion Kotlin Multiplatform para la red de centros medicos AndinaSalud. El proyecto permite validar el flujo de gestion de citas medicas en Android e iOS usando datos simulados en memoria, sin consumir servicios web ni bases de datos.

## Funcionalidades

- Inicio con saludo al paciente, proxima cita y accesos rapidos.
- Lista de citas ordenada por fecha y hora.
- Filtro por estado: Programada, Atendida y Cancelada.
- Busqueda por especialidad o nombre del medico, sin distinguir mayusculas ni tildes.
- Detalle de cita con datos completos y dialogo de cancelacion.
- Solicitud de cita con validacion por campo.
- Perfil del paciente y cambio de tema claro/oscuro.
- Estados de carga, contenido, vacio y error en pantallas con datos.

## Arquitectura

El proyecto usa Clean Architecture + MVVM en `shared/src/commonMain`.

```text
pe/edu/upeu/
  domain/
    model/
    repository/
    usecase/
  data/
    local/
    repository/
  di/
  presentation/
    citas/
    component/
    detalle/
    inicio/
    navigation/
    perfil/
    solicitud/
    theme/
```

### Domain

Contiene las entidades y reglas de negocio del caso:

- `Cita`
- `EstadoCita`
- `Paciente`
- `Medico`
- `Sede`
- `SolicitudCita`
- `CitaRepository`
- `ObtenerCitasUseCase`
- `ObtenerDatosInicialesUseCase`
- `SolicitarCitaUseCase`
- `CancelarCitaUseCase`

Las reglas RN-01 a RN-05 se resuelven en los casos de uso, no en los composables.

### Data

`CitasSimuladas.kt` contiene el paciente, sedes, especialidades, medicos y citas iniciales. `CitaRepositoryFake.kt` implementa `CitaRepository` con datos en memoria y retardo simulado de 800 ms.

No se usa Ktor, Retrofit, Room, SQLDelight ni ninguna libreria de red o persistencia.

### Presentation

Cada modulo de UI tiene `UiState`, `ViewModel` y `Screen` cuando corresponde:

- `inicio`: saludo, proxima cita y accesos rapidos.
- `citas`: listado, filtros y busqueda.
- `detalle`: detalle y cancelacion de cita.
- `solicitud`: formulario y validaciones.
- `perfil`: datos del paciente y tema oscuro.
- `navigation`: destinos y `AppNavHost`.
- `theme`: paleta Material 3 propia.
- `component`: componentes reutilizables.

Los ViewModel exponen `StateFlow` y mantienen el estado de pantalla encapsulado.

## Inyeccion de dependencias

Koin se configura en:

```text
shared/src/commonMain/kotlin/pe/edu/upeu/di/AppModule.kt
```

La app registra el repositorio por interfaz:

```kotlin
single<CitaRepository> { CitaRepositoryFake() }
```

Cuando exista una API REST, se reemplazara la implementacion fake por una implementacion remota sin modificar pantallas ni casos de uso.

## Ejecucion

### Android

Abrir el proyecto en Android Studio, esperar la sincronizacion de Gradle y ejecutar `androidApp` en un emulador o dispositivo fisico.

Tambien se puede generar el APK de depuracion con:

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

### iOS

Abrir `iosApp` en Xcode desde macOS y ejecutar la aplicacion en simulador o dispositivo iOS.

## Flujo Git

Ramas usadas:

- `main`: rama estable.
- `develop`: rama de integracion.
- `feature/base-andinasalud-lee`: desarrollo base del caso.

Mensajes de commit usados segun la convencion del caso:

- `feat(...)`: funcionalidad nueva.
- `style(...)`: tema o interfaz.
- `refactor(...)`: reorganizacion sin cambio funcional.

Para la Parte II del examen se debe crear una rama:

```text
sc-<letra>-lee
```

Ejemplo:

```text
sc-a-lee
```

## Evidencias sugeridas

Para el documento de evidencias se recomienda capturar:

- Estructura `domain`, `data`, `di` y `presentation`.
- Pantalla Inicio.
- Pantalla Citas.
- Pantalla Detalle.
- Pantalla Solicitud.
- Pantalla Perfil.
- Tema oscuro aplicado.
- `git log --graph --oneline --all --decorate`.
- `git shortlog -sne`.
- Ramas `main`, `develop` y `feature/base-andinasalud-lee`.

## Reparto de trabajo

Integrante responsable:

- Lee Brandon: estructura Clean + MVVM, dominio, datos simulados, Koin, pantallas principales, navegacion, tema y README.

## Estado actual

La version actual corresponde al producto base de Unidad 1 con datos simulados en memoria. El cambio a backend real queda preparado mediante la interfaz `CitaRepository`.
