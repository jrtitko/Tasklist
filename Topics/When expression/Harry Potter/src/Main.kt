import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    val house = scanner.nextLine()
    println(
        when (house.lowercase()) {
            "gryffindor" -> "bravery"
            "hufflepuff" -> "loyalty"
            "slytherin" -> "cunning"
            "ravenclaw" -> "intellect"
            else -> "not a valid house"
        }
    )
}