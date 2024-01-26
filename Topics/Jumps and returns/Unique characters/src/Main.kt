fun main() {
    val inputLine = readln()!!
    var count = 0
    inputLine.forEach { c ->
        if (inputLine.count { it == c } == 1) count++
    }
    println(count)
}