package pe.edu.upeu.domain.usecase

import kotlinx.coroutines.CancellationException

inline fun <T> resultadoDe(operacion: () -> T): Result<T> {
    return try {
        Result.success(operacion())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        Result.failure(error)
    }
}
