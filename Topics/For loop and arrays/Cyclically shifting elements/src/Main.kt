import java.util.*

fun main() {

    val scanner = Scanner(System.`in`)
    val intArray = IntArray(scanner.nextInt()) { scanner.nextInt() }
    val shiftArray = IntArray(intArray.size)
    for (i in 0..intArray.lastIndex - 1) {
        shiftArray[i + 1] = intArray[i]
    }
    shiftArray[0] = intArray.last()
    println(shiftArray.joinToString(" "))
}