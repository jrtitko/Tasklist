fun main() {
    val r1_start = readln().toInt()
    val r1_end = readln().toInt()
    val r2_start = readln().toInt()
    val r2_end = readln().toInt()
    val value = readln().toInt()

    println(value in r1_start..r1_end && value in r2_start..r2_end)
}