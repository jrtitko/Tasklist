package tasklist

import java.io.File
import java.time.LocalDate
import java.util.*
import java.time.LocalTime
import com.squareup.moshi.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.io.FileNotFoundException

val scanner = Scanner(System.`in`)

val jsonFile = File("tasklist.json")
val type = Types.newParameterizedType(List::class.java, Task::class.java)
val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
val taskListAdapter = moshi.adapter<List<Task?>>(type)

fun main() {

    val taskList = mutableListOf<Task>()
    taskList.loadTasks()

    do {
        println("Input an action (add, print, edit, delete, end):")
        when(scanner.nextLine().lowercase()) {
            "add" -> taskList.addTask()
            "edit" -> taskList.editTask()
            "delete" -> taskList.deleteTask()
            "print" -> taskList.printTasks()
            "end" -> {
                taskList.saveTasks()
                println("Tasklist exiting!")
                break
            }
            else -> println("The input action is invalid")
        }

    } while (true)
}


// ---------------------

class Task {
    private val priorityList = listOf( "C", "H", "N", "L" )
    private val dateFormat = "\\d{1,4}-\\d{1,2}-\\d{1,2}".toRegex()
    private val timeFormat = "([01]?[0-9]|2[0-3]):[0-5]?[0-9]".toRegex()

    var priority: String? = null
        set(value) {
            if (value?.uppercase() in priorityList) field = value
        }
    var date: String = ""
        set(value) {
            if (dateFormat.matches(value)) {
                val dateParts = value.split("-").map { it.toInt() }
                field = LocalDate.of(dateParts[0], dateParts[1], dateParts[2]).toString()
            } else throw IllegalArgumentException()
        }

    var time: String = ""
        set(value) {
            if (timeFormat.matches(value)) {
                field = value.split(":").map { it.toInt() }.let {
                    LocalTime.of(it[0], it[1]).toString()
                }
            } else throw IllegalArgumentException()
        }
    var task: MutableList<String>? = null

    fun dueTag(): String {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date   // got to be an easier way
//        val daysUntil = currentDate.daysUntil(taskDate)
        val daysUntil = currentDate.daysUntil(date.toLocalDate())
        return when {
            daysUntil == 0 -> "T"
            daysUntil > 0 -> "I"
            daysUntil < 0 -> "O"
            else -> ""
        }
    }
}

fun Task.requestPriority() {
    do {
        println("Input the task priority (C, H, N, L):")
        priority = scanner.nextLine().trim()
    } while (priority == null)
}

fun Task.requestDate() {
    do {
        println("Input the date (yyyy-mm-dd):")
        try {
            date = scanner.nextLine()!!.trim()
            break
        } catch (e: Exception) {
            println("The input date is invalid")
        }
    } while (true)
}

fun Task.requestTime() {
    do {
        println("Input the time (hh:mm):")
        try {
            time = scanner.nextLine()!!.trim()
            break
        } catch (e: IllegalArgumentException) {
            println("The input time is invalid")
        }
    } while (true)
}

fun Task.requestTask() {
    println("Input a new task (enter a blank line to end):")

    val taskList = mutableListOf<String>()

    while (scanner.hasNextLine()) {
        if (!taskList.addTask(scanner.nextLine())) {
            if (taskList.size == 0) println("The task is blank")
            break
        }
    }

    task = taskList
}

fun MutableList<String>.addTask(task: String): Boolean {
    task.trim().let { if (it.isNotBlank()) return add(it) }
    return false
}

fun MutableList<Task>.addTask() {
    val task = Task()
    task.requestPriority()
    task.requestDate()
    task.requestTime()
    task.requestTask()
    add(task)
}

fun MutableList<Task>.editTask() {
    printTasks()

    var index: Int
    while (size > 0) {
        println("Input the task number (1-${size}):")
        try {
            index = scanner.nextLine()!!.trim().toInt()
            if (index in 1..size) {
                selectField(index)
                println("The task is changed")
                break
            } else throw NumberFormatException()
        } catch (e: NumberFormatException) {
            println("Invalid task number")
            continue
        }
    }
}

private fun MutableList<Task>.selectField(index: Int) {
    while (index > 0) {
        println("Input a field to edit (priority, date, time, task):")
        when (scanner.nextLine()!!.trim()) {
            "priority" -> this[index - 1].requestPriority()
            "date"     -> this[index - 1].requestDate()
            "time"     -> this[index - 1].requestTime()
            "task"     -> this[index - 1].requestTask()
            else       -> {
                println("Invalid field")
                continue
            }
        }
        break
    }
}

fun MutableList<Task>.deleteTask() {
    printTasks()

    var index: Int
    while (size > 0) {
        println("Input the task number (1-${size}):")
        try {
            index = scanner.nextLine()!!.trim().toInt()
            if (index <= 0 || index > size) throw NumberFormatException()
            removeAt(index - 1)
            println("The task is deleted")
            break
        } catch (e: NumberFormatException) {
            println("Invalid task number")
            continue
        }
    }
}

fun MutableList<Task>.printTasks() {
    if (isEmpty()) {
        println("No tasks have been input")
        return
    }

    println("+----+------------+-------+---+---+--------------------------------------------+")
    println("| N  |    Date    | Time  | P | D |                   Task                     |")
    println("+----+------------+-------+---+---+--------------------------------------------+")

    forEachIndexed { index, task ->
        var firstLine = true
        task.task?.forEachIndexed{ taskLineIndex, taskLine ->
            taskLine.chunked(44).forEach { tableLine ->
                if (firstLine) {
                    println("| ${String.format("%-2d", index + 1)} | ${task.date} | ${task.time} | ${convertPriorityToColor(task.priority)} | ${convertDueTagToColor(task.dueTag())} |${String.format("%-44s", tableLine)}|")
                    firstLine = false
                } else
                    println("|    |            |       |   |   |${String.format("%-44s", tableLine)}|")
            }
        }
        println("+----+------------+-------+---+---+--------------------------------------------+")
    }
}

fun convertPriorityToColor(priority: String?): String {
    return when(priority?.uppercase()) {
        "C" -> "\u001b[101m \u001b[0m"
        "H" -> "\u001b[103m \u001b[0m"
        "N" -> "\u001b[102m \u001b[0m"
        "L" -> "\u001b[104m \u001b[0m"
        else -> " "
    }
}

fun convertDueTagToColor(dueTag: String?): String {
    return when(dueTag?.uppercase()) {
        "I" -> "\u001b[102m \u001b[0m"
        "T" -> "\u001b[103m \u001b[0m"
        "O" -> "\u001b[101m \u001b[0m"
        else -> " "
    }
}

fun MutableList<Task>.loadTasks() {
    try {
        addAll(taskListAdapter.fromJson(jsonFile.readText())!!.filterNotNull().toMutableList())
    } catch (e: FileNotFoundException) {
        // New list from scratch
    }
}

fun MutableList<Task>.saveTasks() {
    jsonFile.writeText(taskListAdapter.toJson(this))
}
