import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    val count = scanner.nextInt()
    var nbrList = mutableListOf<Int>(count)

    for (i in 0 until count) {
        nbrList.add(scanner.nextInt())
    }

    val find = scanner.nextInt()

    println(if (find in nbrList) "YES" else "NO")
}