import kotlinx.datetime.*

fun nextMonth(date: String): String = Instant.parse(date).plus(DateTimeUnit.MONTH, TimeZone.UTC).toString()

fun main() {
    val date = readln()
    println(nextMonth(date))
}