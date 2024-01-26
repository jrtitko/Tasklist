import kotlin.math.absoluteValue

fun main() {
    val number = readln().toInt()

    if (number == number.absoluteValue && number != 0) {
        println("YES")
    } else {
        println("NO")
    }
}