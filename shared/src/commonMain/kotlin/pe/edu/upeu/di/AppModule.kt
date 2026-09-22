package pe.edu.upeu.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import pe.edu.upeu.data.repository.CitaRepositoryFake
import pe.edu.upeu.domain.repository.CitaRepository
import pe.edu.upeu.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.domain.usecase.ObtenerCitasUseCase
import pe.edu.upeu.domain.usecase.ObtenerDatosInicialesUseCase
import pe.edu.upeu.domain.usecase.SolicitarCitaUseCase
import pe.edu.upeu.presentation.citas.CitasViewModel
import pe.edu.upeu.presentation.detalle.DetalleCitaViewModel
import pe.edu.upeu.presentation.solicitud.SolicitudViewModel

val dataModule = module {
    single<CitaRepository> { CitaRepositoryFake() }
}

val domainModule = module {
    factory { ObtenerCitasUseCase(get()) }
    factory { ObtenerDatosInicialesUseCase(get()) }
    factory { SolicitarCitaUseCase(get()) }
    factory { CancelarCitaUseCase(get()) }
}

val presentationModule = module {
    viewModelOf(::CitasViewModel)
    viewModelOf(::DetalleCitaViewModel)
    viewModelOf(::SolicitudViewModel)
}

expect val platformModule: Module

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(
            platformModule,
            dataModule,
            domainModule,
            presentationModule
        )
    }
}
