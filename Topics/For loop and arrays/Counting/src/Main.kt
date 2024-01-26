import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    val nbrArray = IntArray(scanner.nextInt()) { scanner.nextInt() }
    val find = scanner.nextInt()
    println(nbrArray.count { it == find })
}