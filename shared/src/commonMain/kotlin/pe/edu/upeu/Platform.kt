package pe.edu.upeu

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform